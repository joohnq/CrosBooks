package com.joohnq.crosbooks.view.helper

import android.content.Context
import android.widget.RadioButton
import android.widget.RadioGroup
import com.joohnq.crosbooks.R
import com.joohnq.crosbooks.model.entities.Category

object RadioButtonHelper {
    fun searchCategory(
        context: Context,
        category: Category,
        onClick: (Boolean) -> Unit,
    ): RadioButton =
        RadioButton(context).apply {
            id = category.id
            text = category.title
            buttonDrawable = null
            setBackgroundResource(R.drawable.shape_categorie_radio_button)
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
            setOnClickListener {
                onClick(isChecked)
            }
        }
}