package com.joohnq.crosbooks.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.joohnq.crosbooks.UiState.Companion.toRecyclerViewState
import com.joohnq.crosbooks.databinding.ActivityHomeBinding
import com.joohnq.crosbooks.view.adapter.BooksAdapter
import com.joohnq.crosbooks.view.adapter.CategoriesAdapter
import com.joohnq.crosbooks.view.helper.RecyclerViewHelper
import com.joohnq.crosbooks.view.helper.SnackBarHelper
import com.joohnq.crosbooks.view.setOnApplyWindowInsetsListener
import com.joohnq.crosbooks.viewmodel.BooksViewModel
import com.joohnq.crosbooks.viewmodel.CategoriesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding: ActivityHomeBinding get() = _binding!!
    private val categoriesAdapter by lazy { CategoriesAdapter() }
    private val booksAdapter by lazy { BooksAdapter() }
    private val categoriesViewModel: CategoriesViewModel by viewModel()
    private val booksViewModel: BooksViewModel by viewModel()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun ActivityHomeBinding.initRvs() {
        RecyclerViewHelper.initHorizontal(
            categoriesRecyclerView,
            categoriesAdapter
        )
        RecyclerViewHelper.initVertical(
            booksRecyclerView,
            booksAdapter
        )
    }

    private fun ActivityHomeBinding.observers() {
        categoriesViewModel.categories.observe(this@HomeActivity) { state ->
            categoriesAdapter.setState(state.toRecyclerViewState {
                SnackBarHelper(binding.root, it)
            })
        }

        booksViewModel.books.observe(this@HomeActivity) { state ->
            booksAdapter.setState(state.toRecyclerViewState {
                SnackBarHelper(binding.root, it)
            })
        }
    }

    private fun ActivityHomeBinding.bindButtons() {
        btnAddBook.setOnClickListener {
            Intent(this@HomeActivity, AddBookActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.setOnApplyWindowInsetsListener()
        binding.bindButtons()
        binding.initRvs()
        binding.observers()
        categoriesViewModel.getCategories()
        booksViewModel.getBooks()
    }
}