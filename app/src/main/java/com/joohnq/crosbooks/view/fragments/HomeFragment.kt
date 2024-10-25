package com.joohnq.crosbooks.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.joohnq.crosbooks.R
import com.joohnq.crosbooks.constants.AppConstants
import com.joohnq.crosbooks.databinding.FragmentHomeBinding
import com.joohnq.crosbooks.view.adapter.BooksAdapter
import com.joohnq.crosbooks.view.adapter.CategoriesAdapter
import com.joohnq.crosbooks.view.helper.DialogHelper
import com.joohnq.crosbooks.view.helper.initHorizontal
import com.joohnq.crosbooks.view.helper.initVerticalWithScrollEvent
import com.joohnq.crosbooks.view.helper.showSnackBar
import com.joohnq.crosbooks.view.navigation.navigateToAddBookActivity
import com.joohnq.crosbooks.view.navigation.navigateToBookDetailActivity
import com.joohnq.crosbooks.view.state.UiState
import com.joohnq.crosbooks.view.state.UiState.Companion.onSuccess
import com.joohnq.crosbooks.view.state.UiState.Companion.toRecyclerViewState
import com.joohnq.crosbooks.viewmodel.BooksViewModel
import com.joohnq.crosbooks.viewmodel.CategoriesViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
    private val categoriesViewModel: CategoriesViewModel by viewModel()
    private val booksViewModel: BooksViewModel by activityViewModel()
    private val ioDispatcher: CoroutineDispatcher by inject()
    private var isLoadingMore = false
    private val categoryAdapter: CategoriesAdapter by lazy { CategoriesAdapter() }
    private val booksAdapter by lazy {
        BooksAdapter(
            onClick = { book -> navigateToBookDetailActivity(book) },
            onRemove = ::onRemoveBook
        )
    }

    private fun onRemoveBook(id: Int) {
        DialogHelper.areYourSure(
            requireContext(),
            getString(R.string.are_your_sure_you_want_remove_this_book)
        ) {
            lifecycleScope.launch(ioDispatcher) {
                try {
                    booksViewModel.removeBook(id)
                    binding.root.showSnackBar("Successfully removed the book")
                } catch (e: Exception) {
                    binding.root.showSnackBar(e.message.toString())
                }
                booksViewModel.getAllBooks()
            }
        }
    }

    private fun FragmentHomeBinding.observers() {
        categoriesViewModel.categories.observe(viewLifecycleOwner) { state ->
            categoryAdapter.setState(state.toRecyclerViewState { root.showSnackBar(it) })
        }

        booksViewModel.booksQtd.observe(this@HomeFragment) { qtd ->
            booksSize.text = getString(R.string.books_size, qtd)
        }

        booksViewModel.books.observe(this@HomeFragment) { state ->
            state.onSuccess { isLoadingMore = false }
            if (state is UiState.Loading && isLoadingMore) return@observe

            booksAdapter.setState(state.toRecyclerViewState {
                root.showSnackBar(it)
            })
        }
    }

    private fun FragmentHomeBinding.initRvs() {
        categoriesRecyclerView.initHorizontal(
            categoryAdapter
        )
        booksRecyclerView.initVerticalWithScrollEvent(
            booksAdapter
        ) {
            if (isLoadingMore) return@initVerticalWithScrollEvent
            isLoadingMore = true
            booksViewModel.getBooksPagination()
        }
    }

    private fun FragmentHomeBinding.bindButtons() {
        btnAddBook.setOnClickListener { navigateToAddBookActivity() }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindButtons()
        binding.initRvs()
        binding.observers()
    }
}