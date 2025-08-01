package com.github.radlance.login.presentation.signin

import java.security.MessageDigest

interface FormatNonce {

    fun format(rawNonce: String): String

    object DigestFold : FormatNonce {
        override fun format(rawNonce: String): String {
            val bytes = rawNonce.toByteArray()
            val messageDigest = MessageDigest.getInstance("SHA-256")
            val digest = messageDigest.digest(bytes)
            return digest.fold("") { str, it -> str + "%02x".format(it) }
        }
    }
}