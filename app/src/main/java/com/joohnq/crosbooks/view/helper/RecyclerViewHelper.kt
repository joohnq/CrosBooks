package com.joohnq.crosbooks.view.helper

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

object RecyclerViewHelper {
    fun initHorizontal(recyclerView: RecyclerView, adapter: Adapter<*>) {
        recyclerView.layoutManager = LinearLayoutManager(
            recyclerView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter
    }

    fun initVertical(recyclerView: RecyclerView, adapter: Adapter<*>) {
        recyclerView.layoutManager = LinearLayoutManager(
            recyclerView.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = adapter
    }
}