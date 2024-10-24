package com.joohnq.crosbooks.view.viewholders

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.joohnq.crosbooks.databinding.RecyclerViewErrorBinding

class ViewHolderError(private val binding: RecyclerViewErrorBinding) : ViewHolder(binding.root) {
    fun bind(message: String?) {
        binding.error = message.toString()
    }
}