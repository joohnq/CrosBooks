package com.joohnq.crosbooks.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.joohnq.crosbooks.model.entities.Category

class CategorySpinnerAdapter(context: Context, categories: List<Category>) :
    ArrayAdapter<Category>(context, android.R.layout.simple_spinner_item, categories) {
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View =
        getTitleInView(position, convertView, parent)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View =
        getTitleInView(position, convertView, parent)

    private fun getTitleInView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val category = getItem(position)
        (view as TextView).text = category?.title
        return view
    }
}