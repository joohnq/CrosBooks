package com.joohnq.crosbooks.view.helper

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

fun RecyclerView.initHorizontal(adapter: Adapter<*>) {
    layoutManager = LinearLayoutManager(
        context,
        LinearLayoutManager.HORIZONTAL,
        false
    )
    this.adapter = adapter
}

    fun RecyclerView.initVerticalWithScrollEvent(adapter: Adapter<*>, onScroll: () -> Unit) {
        layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        this.adapter = adapter

        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val itemCount = recyclerView.adapter?.itemCount ?: return

                if (!recyclerView.canScrollVertically(1) && itemCount % 10 == 0 && itemCount != 0) {
                    onScroll()
                }
            }
        })
    }
