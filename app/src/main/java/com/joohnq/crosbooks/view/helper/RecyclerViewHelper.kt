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

fun RecyclerView.initVertical(adapter: Adapter<*>) {
    layoutManager = LinearLayoutManager(
        context,
        LinearLayoutManager.VERTICAL,
        false
    )
    this.adapter = adapter
}
