package com.joohnq.crosbooks.view.viewholders

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.joohnq.crosbooks.databinding.BookItemBinding
import com.joohnq.crosbooks.databinding.CategoryItemBinding
import com.joohnq.crosbooks.model.entities.Book
import com.joohnq.crosbooks.model.entities.Category

class BooksItemViewHolder(private val binding: BookItemBinding) : ViewHolder(binding.root) {
    fun bind(book: Book) {
        binding.book = book
    }
}