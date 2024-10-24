package com.joohnq.crosbooks.view.viewholders

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.joohnq.crosbooks.databinding.CategoryItemBinding
import com.joohnq.crosbooks.model.entities.Category

class CategoryItemViewHolder(private val binding: CategoryItemBinding) : ViewHolder(binding.root) {
    fun bind(category: Category) {
        binding.category = category.title
    }
}