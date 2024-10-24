package com.joohnq.crosbooks.view.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.joohnq.crosbooks.UiState.Companion.fold
import com.joohnq.crosbooks.UiState.Companion.onSuccess
import com.joohnq.crosbooks.common.FieldValidation
import com.joohnq.crosbooks.common.exceptions.CustomException
import com.joohnq.crosbooks.databinding.ActivityAddBookBinding
import com.joohnq.crosbooks.model.entities.BookPost
import com.joohnq.crosbooks.model.entities.Category
import com.joohnq.crosbooks.view.adapter.CategorySpinnerAdapter
import com.joohnq.crosbooks.view.contracts.GalleryImagePicker
import com.joohnq.crosbooks.view.helper.clearAllErrors
import com.joohnq.crosbooks.view.helper.onChange
import com.joohnq.crosbooks.view.helper.showSnackBar
import com.joohnq.crosbooks.view.permission.PermissionManager
import com.joohnq.crosbooks.view.permission.PermissionManager.Companion.GALLERY_PERMISSION_CODE
import com.joohnq.crosbooks.view.setOnApplyWindowInsetsListener
import com.joohnq.crosbooks.viewmodel.BooksViewModel
import com.joohnq.crosbooks.viewmodel.CategoriesViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddBookActivity : AppCompatActivity() {
    private var _binding: ActivityAddBookBinding? = null
    private val binding: ActivityAddBookBinding get() = _binding!!
    private val categoriesViewModel: CategoriesViewModel by viewModel()
    private val booksViewModel: BooksViewModel by viewModel()
    private var bookPost: BookPost? = null
    private var bookImageUri: Uri? = null
    private var selectedCategoryId: Int? = null
    private val permissionManager: PermissionManager by inject()
    private val ioDispatcher: CoroutineDispatcher by inject()
    private val activityResultLauncherGalleryImagePicker: ActivityResultLauncher<PickVisualMediaRequest> =
        GalleryImagePicker(this@AddBookActivity)
        { uri ->
            Glide
                .with(binding.bookImage)
                .load(uri)
                .into(binding.bookImage)
            bookImageUri = uri
        }

    private fun ActivityAddBookBinding.toggleIsLoading(state: Boolean) {
        isLoadingProgressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.onRequestPermissionsResult(
            requestCode,
            GALLERY_PERMISSION_CODE,
            grantResults,
            onAllowPermission = {
                activityResultLauncherGalleryImagePicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            onDenyPermission = {
                binding.root.showSnackBar("You only can add an image accepting the permission")
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        bookPost = null
    }

    private fun ActivityAddBookBinding.initCategoriesSpinner() {
        categoriesViewModel.categories.observe(this@AddBookActivity) { state ->
            state.onSuccess { categories ->
                selectedCategoryId = categories.first().id
                val adapter = CategorySpinnerAdapter(this@AddBookActivity, categories)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                categoriesSpinner.adapter = adapter

                categoriesSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                            val selectedCategory = parent.getItemAtPosition(position) as Category
                            selectedCategoryId = selectedCategory.id
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            root.setOnApplyWindowInsetsListener()
            initCategoriesSpinner()
            bindButtons()
            initTestFields()
            observers()
            whenInputValueChange()
        }
    }

    private fun ActivityAddBookBinding.initTestFields() {
        textEditTextTitle.setText("O Livro do João")
        textEditTextAuthor.setText("João")
        textEditTextSummary.setText("O livro do João é um clássico da literatura portuguesa")
    }

    private fun ActivityAddBookBinding.onAddBook() {
        listOf(
            textInputLayoutTitle,
            textInputLayoutAuthor,
            textInputLayoutSummary,
        ).clearAllErrors()

        try {
            if (this@AddBookActivity.bookImageUri == null) throw CustomException.BookImageNotAdded()

            val title = textEditTextTitle.text.toString()
            val summary = textEditTextSummary.text.toString()
            val author = textEditTextAuthor.text.toString()

            FieldValidation.validateSimpleString(title, CustomException.TitleCannotBeEmpty())
            FieldValidation.validateSimpleString(
                summary,
                CustomException.SummaryCannotBeEmpty()
            )
            FieldValidation.validateSimpleString(
                author,
                CustomException.AuthorCannotBeEmpty()
            )


            lifecycleScope.launch(ioDispatcher) {
                val url = booksViewModel.sendBookImage(bookImageUri!!)

                binding.root.showSnackBar("Success to add the book image")

                if (selectedCategoryId == null) throw CustomException.CategoryNotSelected()

                bookPost = BookPost(
                    imageUrl = url,
                    title = title,
                    summary = summary,
                    author = author,
                    categoryId = selectedCategoryId!!
                )

                val res = booksViewModel.addBook(bookPost!!)

                if (!res) throw CustomException.BookNotAdded()

                binding.root.showSnackBar("Success to add the book")
            }
        } catch (e: CustomException.TitleCannotBeEmpty) {
            textInputLayoutTitle.error = e.message
        } catch (e: CustomException.SummaryCannotBeEmpty) {
            textInputLayoutSummary.error = e.message
        } catch (e: CustomException.AuthorCannotBeEmpty) {
            textInputLayoutAuthor.error = e.message
        } catch (e: Exception) {
            binding.root.showSnackBar(e.message.toString())
        }
    }

    private fun ActivityAddBookBinding.bindButtons() {
        btnAddBook.setOnClickListener { onAddBook() }
        topAppBar.setNavigationOnClickListener { finish() }
        bookImageFrame.setOnClickListener {
            val isAllowed = permissionManager.verifyGalleryPermission()

            if (!isAllowed) {
                permissionManager.requestGalleryPermission(this@AddBookActivity)
                return@setOnClickListener
            }

            activityResultLauncherGalleryImagePicker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    private fun ActivityAddBookBinding.observers() {
        booksViewModel.addCurrentBookStatus.observe(this@AddBookActivity) { state ->
            state.fold(
                onLoading = { binding.toggleIsLoading(true) },
                onError = {
                    binding.toggleIsLoading(false)
                    binding.root.showSnackBar(it)
                },
                onSuccess = {
                    binding.root.showSnackBar("Successfully added book")
                    toggleIsLoading(false)
                }
            )
        }
    }

    private fun ActivityAddBookBinding.whenInputValueChange() {
        textEditTextTitle.onChange(textInputLayoutTitle)
        textEditTextAuthor.onChange(textInputLayoutAuthor)
        textEditTextSummary.onChange(textInputLayoutSummary)
    }
}