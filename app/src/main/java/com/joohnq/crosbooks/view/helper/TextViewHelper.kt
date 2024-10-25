package com.joohnq.crosbooks.view.helper

import android.content.Context
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.joohnq.crosbooks.R
import com.joohnq.crosbooks.model.entities.Category

object TextViewHelper {
    fun homeCategory(
        context: Context,
        category: Category,
    ): TextView =
        TextView(context).apply {
            id = category.id
            text = category.title
            layoutParams = RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                width = RadioGroup.LayoutParams.MATCH_PARENT
                height = RadioGroup.LayoutParams.WRAP_CONTENT
                setMargins(16, 0, 0, 0)
            }
            textSize = 16f
            setPadding(30, 10, 30, 10)
        }
}