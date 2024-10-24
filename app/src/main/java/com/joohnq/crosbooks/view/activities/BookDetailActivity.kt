package com.joohnq.crosbooks.view.activities

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.joohnq.crosbooks.databinding.ActivityBookDetailBinding
import com.joohnq.crosbooks.model.entities.Book
import com.joohnq.crosbooks.view.helper.showSnackBar
import com.joohnq.crosbooks.view.setOnApplyWindowInsetsListener
import com.joohnq.crosbooks.viewmodel.BooksViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookDetailActivity : AppCompatActivity() {
    private var _binding: ActivityBookDetailBinding? = null
    private val binding: ActivityBookDetailBinding get() = _binding!!
    private var id: Int? = null
    private val booksViewModel: BooksViewModel by viewModel()
    private val ioDispatcher: CoroutineDispatcher by inject()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.setOnApplyWindowInsetsListener()
        val book = getIntentBook()
        if (book == null) {
            binding.isError = true
        } else {
            binding.book = book
            id = book.id
        }
        binding.bindButtons()
    }

    private fun getIntentBook(): Book? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("book", Book::class.java)
        } else {
            intent.getParcelableExtra("book")
        }

    private fun getBook() {
        lifecycleScope.launch(ioDispatcher) {
            try {
                binding.book = booksViewModel.getBookDetail(id!!)
                binding.isLoading = false
            } catch (e: Exception) {
                binding.root.showSnackBar(e.message.toString())
                binding.isError = true
            }
        }
    }

    private fun ActivityBookDetailBinding.bindButtons() {
        swipeRefreshLayout.setOnRefreshListener {
            isLoading = true
            getBook()
        }
        topAppBar.setNavigationOnClickListener { finish() }
    }
}