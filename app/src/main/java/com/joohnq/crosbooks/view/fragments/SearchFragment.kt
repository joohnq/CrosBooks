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
import com.joohnq.crosbooks.databinding.FragmentSearchBinding
import com.joohnq.crosbooks.view.adapter.BooksAdapter
import com.joohnq.crosbooks.view.helper.DialogHelper
import com.joohnq.crosbooks.view.helper.initVerticalWithScrollEvent
import com.joohnq.crosbooks.view.helper.showSnackBar
import com.joohnq.crosbooks.view.navigation.navigateToBookDetailActivity
import com.joohnq.crosbooks.view.state.UiState
import com.joohnq.crosbooks.view.state.UiState.Companion.onSuccess
import com.joohnq.crosbooks.view.state.UiState.Companion.toRecyclerViewState
import com.joohnq.crosbooks.viewmodel.BooksViewModel
import com.joohnq.crosbooks.viewmodel.SearchViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModel()
    private val ioDispatcher: CoroutineDispatcher by inject()
    private val booksViewModel: BooksViewModel by viewModel()
    private var isLoadingMore = false
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
                val search = binding.textEditTextSearch.text.toString()

                if (search.isEmpty()) return@launch
                searchViewModel.getAllSearchBooks(search)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun FragmentSearchBinding.getBooksByFilter() {
        searchViewModel.resetPageValues()
        val search = textEditTextSearch.text.toString()

        if (search.isEmpty()) return

        searchViewModel.getAllSearchBooks(search = search)
    }

    private fun FragmentSearchBinding.bindButtons() {
        textInputLayoutSearch.setStartIconOnClickListener { getBooksByFilter() }
        textInputLayoutSearch.setEndIconOnClickListener {
            textEditTextSearch.setText("")
            searchViewModel.setSearchBooksIdle()
        }
    }

    private fun FragmentSearchBinding.initRvs() {
        booksRecyclerView.initVerticalWithScrollEvent(
            booksAdapter
        ) {
            if (isLoadingMore) return@initVerticalWithScrollEvent
            val search = textEditTextSearch.text.toString()

            if (search.isEmpty()) return@initVerticalWithScrollEvent
            isLoadingMore = true
            searchViewModel.getBooksPagination(search)
        }
    }

    private fun FragmentSearchBinding.observers() {
        searchViewModel.searchBooks.observe(viewLifecycleOwner) { state ->
            state.onSuccess { isLoadingMore = false }
            if (state is UiState.Loading && isLoadingMore) return@observe

            booksAdapter.setState(state.toRecyclerViewState {
                root.showSnackBar(it)
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.observers()
        binding.initRvs()
        binding.bindButtons()
    }
}