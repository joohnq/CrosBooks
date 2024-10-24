package com.joohnq.crosbooks.common

import com.joohnq.crosbooks.common.exceptions.CustomException
import com.joohnq.crosbooks.common.exceptions.EmailException
import com.joohnq.crosbooks.common.exceptions.PasswordConfirmException
import com.joohnq.crosbooks.common.exceptions.PasswordException

object FieldValidation {
    fun validateEmail(email: String): Boolean {
        if (email.trim().isEmpty()) throw EmailException.EmailEmpty()
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) throw EmailException.EmailInvalid()
        return true
    }

    fun validatePassword(password: String): Boolean {
        if (password.trim().isEmpty()) throw PasswordException.PasswordEmpty()
        if (password.length < 8) throw PasswordException.PasswordTooShort()
        return true
    }

    fun validateName(name: String): Boolean {
        if (name.trim().isEmpty()) throw CustomException.NameCannotBeEmpty()
        return true
    }

    fun validateSimpleString(src: String, exception: Exception): Boolean {
        if (src.trim().isEmpty()) throw exception
        return true
    }

    fun validatePasswordConfirm(password: String, passwordConfirm: String): Boolean {
        if (password.trim().isEmpty()) throw PasswordException.PasswordEmpty()
        if (passwordConfirm.trim().isEmpty()) throw PasswordConfirmException.PasswordConfirmEmpty()
        if (password != passwordConfirm) throw PasswordConfirmException.PasswordConfirmNotMatch()
        return true
    }
}