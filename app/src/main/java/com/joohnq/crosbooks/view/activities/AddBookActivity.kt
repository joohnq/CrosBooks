package com.joohnq.crosbooks.view.activities

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.joohnq.crosbooks.UiState.Companion.fold
import com.joohnq.crosbooks.UiState.Companion.onSuccess
import com.joohnq.crosbooks.common.FieldValidation
import com.joohnq.crosbooks.common.exceptions.SimpleException
import com.joohnq.crosbooks.constants.AppConstants
import com.joohnq.crosbooks.databinding.ActivityAddBookBinding
import com.joohnq.crosbooks.model.entities.Book
import com.joohnq.crosbooks.view.helper.SnackBarHelper
import com.joohnq.crosbooks.view.helper.clearAllErrors
import com.joohnq.crosbooks.view.helper.onChange
import com.joohnq.crosbooks.view.setOnApplyWindowInsetsListener
import com.joohnq.crosbooks.viewmodel.BooksViewModel
import com.joohnq.crosbooks.viewmodel.CategoriesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddBookActivity : AppCompatActivity() {
    private var _binding: ActivityAddBookBinding? = null
    private val binding: ActivityAddBookBinding get() = _binding!!
    private val categoriesViewModel: CategoriesViewModel by viewModel()
    private val booksViewModel: BooksViewModel by viewModel()
    private var book: Book? = null
    private val onLoading: () -> Unit = { binding.toggleIsLoading(true) }
    private val onError: (String) -> Unit = {
        binding.toggleIsLoading(false)
        SnackBarHelper(binding.root, it)
    }
    private var uri: Uri? = null

    private fun ActivityAddBookBinding.toggleIsLoading(state: Boolean) {
        isLoadingProgressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        book = null
    }

    private fun ActivityAddBookBinding.initCategoriesSpinner() {
        categoriesViewModel.categories.observe(this@AddBookActivity) { state ->
            Log.i(AppConstants.TAG, "categories: $state")
            state.onSuccess { categories ->
                Log.i(AppConstants.TAG, "categories2: $categories")
                val adapter = ArrayAdapter(
                    this@AddBookActivity,
                    android.R.layout.simple_spinner_item,
                    categories.map { it.title }
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                categoriesSpinner.adapter = adapter
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.setOnApplyWindowInsetsListener()
        binding.initCategoriesSpinner()
        binding.bindButtons()
        binding.textEditTextTitle.setText("O Livro do João")
        binding.textEditTextAuthor.setText("João")
        binding.textEditTextSummary.setText("O livro do João é um clássico da literatura portuguesa")
        binding.observers()
        binding.whenInputValueChange()
    }

    private fun ActivityAddBookBinding.clearAllErrors() {
        listOf(
            textInputLayoutTitle,
            textInputLayoutAuthor,
            textInputLayoutSummary,
        ).clearAllErrors()
    }

    private fun ActivityAddBookBinding.onAddBook() {
        clearAllErrors()
        try {
            if (uri == null) throw SimpleException.BookImageNotAdded()

            val title = textEditTextTitle.text.toString()
            val summary = textEditTextSummary.text.toString()
            val author = textEditTextAuthor.text.toString()

            FieldValidation.validateSimpleString(title, SimpleException.TitleCannotBeEmpty())
            FieldValidation.validateSimpleString(
                summary,
                SimpleException.SummaryCannotBeEmpty()
            )
            FieldValidation.validateSimpleString(
                author,
                SimpleException.AuthorCannotBeEmpty()
            )

//            booksViewModel.register(name, email, password, confirmPassword)
        } catch (e: SimpleException.TitleCannotBeEmpty) {
            textInputLayoutTitle.error = e.message
        } catch (e: SimpleException.SummaryCannotBeEmpty) {
            textInputLayoutSummary.error = e.message
        } catch (e: SimpleException.AuthorCannotBeEmpty) {
            textInputLayoutAuthor.error = e.message
        } catch (e: SimpleException.BookImageNotAdded) {
            SnackBarHelper(binding.root, e.message.toString())
        }
    }

    private fun ActivityAddBookBinding.bindButtons() {
        btnAddBook.setOnClickListener { onAddBook() }
    }

    private fun ActivityAddBookBinding.observers() {
        booksViewModel.addCurrentBookStatus.observe(this@AddBookActivity) { state ->
            state.fold(
                onLoading = onLoading,
                onError = onError,
                onSuccess = {
                    SnackBarHelper(binding.root, "Successfully added book")
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