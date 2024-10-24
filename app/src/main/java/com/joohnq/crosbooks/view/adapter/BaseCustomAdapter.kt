package com.joohnq.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.joohnq.crosbooks.view.viewholders.ViewHolderError
import com.joohnq.crosbooks.databinding.RecyclerViewEmptyBinding
import com.joohnq.crosbooks.databinding.RecyclerViewErrorBinding
import com.joohnq.crosbooks.databinding.RecyclerViewLoadingBinding
import com.joohnq.crosbooks.databinding.RecyclerViewNothingBinding
import com.joohnq.crosbooks.view.viewholders.ViewHolderEmpty
import com.joohnq.crosbooks.view.viewholders.ViewHolderLoading
import com.joohnq.crosbooks.view.viewholders.ViewHolderNothing

@Suppress("UNCHECKED_CAST")
abstract class BaseCustomAdapter<T : Any, VHNothing : ViewHolder, VHLoading : ViewHolder, VHEmpty : ViewHolder, VHSuccess : ViewHolder, VHError : ViewHolder> :
    CustomAbstractAdapter<T, VHNothing, VHLoading, VHEmpty, VHSuccess, VHError>() {

    abstract override fun createSuccessViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): VHSuccess

    override fun createLoadingViewHolder(inflater: LayoutInflater, parent: ViewGroup): VHLoading =
        ViewHolderLoading(
            RecyclerViewLoadingBinding.inflate(inflater, parent, false)
        ) as VHLoading

    override fun createEmptyViewHolder(inflater: LayoutInflater, parent: ViewGroup): VHEmpty =
        ViewHolderEmpty(
            RecyclerViewEmptyBinding.inflate(inflater, parent, false)
        ) as VHEmpty

    override fun createErrorViewHolder(inflater: LayoutInflater, parent: ViewGroup): VHError =
        ViewHolderError(
            RecyclerViewErrorBinding.inflate(inflater, parent, false)
        ) as VHError

    override fun createNothingViewHolder(inflater: LayoutInflater, parent: ViewGroup): VHNothing =
        ViewHolderNothing(
            RecyclerViewNothingBinding.inflate(inflater, parent, false)
        ) as VHNothing

    override fun bindLoadingViewHolder(holder: VHLoading) {}
    override fun bindEmptyViewHolder(holder: VHEmpty) {}
    override fun bindNothingViewHolder(holder: VHNothing) {}
    abstract override fun bindSuccessViewHolder(holder: VHSuccess, position: Int)
    abstract override fun bindErrorViewHolder(holder: VHError)
}