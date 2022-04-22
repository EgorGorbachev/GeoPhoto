package com.example.geophotoapp.util.validation

import com.example.domain.error.ApplicationError
import com.example.domain.model.result.Result
import com.example.domain.model.result.toSuccess


fun String.startCheck() = this.toSuccess()

fun Result<String>.checkEmpty(): Result<String> {
    return if (this is Result.Success) {
        if (value.isNotEmpty()) Result.Success(value)
        else Result.Error(ApplicationError.EmptyFiled)
    } else this
}

private fun String.matchTo(regExp: String) = matches(regExp.toRegex())

fun String.isEmailValid() = this.matchTo(EMAIL)

fun String.isPhoneValid() = this.matchTo(PHONE)



