package com.github.radlance.auth.presentation.common

import android.util.Patterns
import com.github.radlance.auth.R
import com.github.radlance.core.core.ManageResource
import javax.inject.Inject


interface ValidateSignIn {

    fun validEmail(value: String): String

    fun validPassword(value: String): String
}

internal interface MatchEmail {

    fun match(value: String): Boolean

    class Base @Inject constructor() : MatchEmail {
        override fun match(value: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(value).matches()
        }
    }
}

interface ValidateAuth : ValidateSignIn {

    fun validName(value: String): String

    fun validPasswordConfirm(confirm: String, password: String): String
}

internal class BaseValidateAuth @Inject constructor(
    private val manageResource: ManageResource,
    private val matchEmail: MatchEmail
) : ValidateAuth {

    override fun validName(value: String): String {
        return if (value.isBlank()) {
            manageResource.string(R.string.name_is_blank)
        } else ""
    }

    override fun validEmail(value: String): String = with(manageResource) {
        return when {
            value.isBlank() -> string(id = R.string.email_is_blank)
            !matchEmail.match(value) -> string(id = R.string.incorrect_email_format)
            else -> ""
        }
    }

    override fun validPassword(value: String): String = with(manageResource) {
        return when {
            value.isBlank() -> string(id = R.string.password_is_blank)
            value.trim().length < 6 -> string(id = R.string.min_password_length)
            else -> ""
        }
    }

    override fun validPasswordConfirm(confirm: String, password: String): String {
        with(manageResource) {
            return when {
                confirm.isBlank() -> string(id = R.string.confirm_password_is_blank)
                confirm.trim() != password.trim() -> string(id = R.string.passwords_dont_match)
                else -> ""
            }
        }
    }
}