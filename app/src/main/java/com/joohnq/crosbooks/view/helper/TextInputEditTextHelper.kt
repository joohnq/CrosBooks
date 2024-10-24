package com.joohnq.crosbooks.view.helper

import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputEditText.onChange(textInputLayout: TextInputLayout){
    addTextChangedListener {
        textInputLayout.error = null
    }
}

fun List<TextInputLayout>.clearAllErrors() {
    forEach {
        it.error = null
    }
}