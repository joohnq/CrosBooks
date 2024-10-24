package com.joohnq.crosbooks.view.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.joohnq.crosbooks.R
import com.joohnq.crosbooks.UiState
import com.joohnq.crosbooks.UiState.Companion.onSuccess
import com.joohnq.crosbooks.UiState.Companion.toRecyclerViewState
import com.joohnq.crosbooks.databinding.ActivityHomeBinding
import com.joohnq.crosbooks.view.adapter.BooksAdapter
import com.joohnq.crosbooks.view.adapter.CategoriesAdapter
import com.joohnq.crosbooks.view.helper.DialogHelper
import com.joohnq.crosbooks.view.helper.initHorizontal
import com.joohnq.crosbooks.view.helper.initVertical
import com.joohnq.crosbooks.view.helper.showSnackBar
import com.joohnq.crosbooks.view.navigation.navigateToAddBookActivity
import com.joohnq.crosbooks.view.navigation.navigateToBookDetailActivity
import com.joohnq.crosbooks.view.setOnApplyWindowInsetsListener
import com.joohnq.crosbooks.viewmodel.BooksViewModel
import com.joohnq.crosbooks.viewmodel.CategoriesViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding: ActivityHomeBinding get() = _binding!!
    private val categoriesAdapter by lazy { CategoriesAdapter() }
    private val ioDispatcher: CoroutineDispatcher by inject()
    private val categoriesViewModel: CategoriesViewModel by viewModel()
    private val booksViewModel: BooksViewModel by viewModel()
    private val booksAdapter by lazy {
        BooksAdapter(
            onClick = { book -> navigateToBookDetailActivity(book) },
            onRemove = ::onRemoveBook
        )
    }

    private fun onRemoveBook(id: Int) {
        DialogHelper.areYourSure(
            this@HomeActivity,
            getString(R.string.are_your_sure_you_want_remove_this_book)
        ) {
            lifecycleScope.launch(ioDispatcher) {
                try {
                    booksViewModel.removeBook(id)
                    binding.root.showSnackBar("Successfully removed the book")
                    booksViewModel.removeBookFromList(id)
                } catch (e: Exception) {
                    binding.root.showSnackBar(e.message.toString())
                }
            }
        }
    }

    private fun ActivityHomeBinding.initRvs() {
        categoriesRecyclerView.initHorizontal(
            categoriesAdapter
        )
        booksRecyclerView.initVertical(
            booksAdapter
        )
    }

    private fun ActivityHomeBinding.filterBooks(text: String) {
        val state = booksViewModel.books.value ?: return
        if (text == "") {
            booksAdapter.setState(state.toRecyclerViewState {
                root.showSnackBar(it)
            })
            return
        }

        state.onSuccess { books ->
            val newBooks = books.filter { book ->
                book.title.lowercase().startsWith(text.lowercase())
            }
            val newState = UiState.Success(newBooks)
            booksAdapter.setState(newState.toRecyclerViewState {
                root.showSnackBar(it)
            })
        }
    }


    private fun ActivityHomeBinding.observers() {
        textEditTextSearch.doOnTextChanged { text, _, _, _ ->
            filterBooks(text.toString())
        }

        categoriesViewModel.categories.observe(this@HomeActivity) { state ->
            categoriesAdapter.setState(state.toRecyclerViewState {
                root.showSnackBar(it)
            })
        }

        booksViewModel.books.observe(this@HomeActivity) { state ->
            state.onSuccess {
                if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
            }
            val searchText = textEditTextSearch.text.toString()
            if (searchText.isNotEmpty()) {
                filterBooks(searchText)
            } else {
                booksAdapter.setState(state.toRecyclerViewState {
                    root.showSnackBar(it)
                })
            }
        }
    }

    private fun ActivityHomeBinding.initToolbar() {
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.searchAction -> {
                    isSearchInputVisible = true
                    topAppBar.setNavigationIcon(R.drawable.ic_close)
                    true
                }

                else -> false
            }
        }
    }

    private fun ActivityHomeBinding.bindButtons() {
        btnAddBook.setOnClickListener { navigateToAddBookActivity() }
        swipeRefreshLayout.setOnRefreshListener {
            booksViewModel.getBooks()
        }
        topAppBar.setNavigationOnClickListener {
            isSearchInputVisible = false
            topAppBar.navigationIcon = null
            textEditTextSearch.setText("")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        booksViewModel.getBooks()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            root.setOnApplyWindowInsetsListener()
            bindButtons()
            initToolbar()
            initRvs()
            observers()
            isSearchInputVisible = false
        }
        categoriesViewModel.getCategories()
    }
}