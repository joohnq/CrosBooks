package com.joohnq.crosbooks.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.joohnq.core.adapter.BaseCustomAdapter
import com.joohnq.crosbooks.databinding.BookItemBinding
import com.joohnq.crosbooks.databinding.CategoryItemBinding
import com.joohnq.crosbooks.di.repositoryModule
import com.joohnq.crosbooks.model.entities.Book
import com.joohnq.crosbooks.model.entities.Category
import com.joohnq.crosbooks.view.state.RecyclerViewState
import com.joohnq.crosbooks.view.viewholders.BooksItemViewHolder
import com.joohnq.crosbooks.view.viewholders.CategoriesItemViewHolder
import com.joohnq.crosbooks.view.viewholders.ViewHolderEmpty
import com.joohnq.crosbooks.view.viewholders.ViewHolderError
import com.joohnq.crosbooks.view.viewholders.ViewHolderLoading
import com.joohnq.crosbooks.view.viewholders.ViewHolderNothing

class CategoriesAdapter :
    BaseCustomAdapter<Category, ViewHolderNothing, ViewHolderLoading, ViewHolderEmpty, CategoriesItemViewHolder, ViewHolderError>() {
    override fun getJobDiffCallback(oldList: List<Category>, newList: List<Category>): DiffUtil.Callback {
        return object : DiffUtil.Callback() {
            override fun getOldListSize() = oldList.size
            override fun getNewListSize() = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].id == newList[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
        }
    }

    override fun createSuccessViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): CategoriesItemViewHolder = CategoriesItemViewHolder(
        CategoryItemBinding.inflate(inflater, parent, false)
    )

    override fun bindSuccessViewHolder(holder: CategoriesItemViewHolder, position: Int) {
        val item = (uiState as RecyclerViewState.Success<Category>).data[position]
        holder.bind(item)
    }

    override fun bindErrorViewHolder(holder: ViewHolderError) {
        val errorMessage = (uiState as RecyclerViewState.Error).error
        holder.bind(errorMessage)
    }
}
