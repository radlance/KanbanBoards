package com.github.radlance.kanbanboards.login.presentation

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.github.radlance.kanbanboards.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import java.util.UUID

interface AccountManager {

    suspend fun signIn(): CredentialResult

    class Google(
        private val activity: Activity,
        formatNonce: FormatNonce
    ) : AccountManager {

        private val credentialManager = CredentialManager.create(activity)
        private val rawNonce = UUID.randomUUID().toString()

        private val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
            .setNonce(formatNonce.format(rawNonce))
            .build()

        private val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        override suspend fun signIn(): CredentialResult {
            return try {
                val result = credentialManager.getCredential(request = request, context = activity)
                val credential = result.credential
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val googleIdToken = googleIdTokenCredential.idToken

                CredentialResult.Success(googleIdToken)
            } catch (e: Exception) {
                CredentialResult.Error
            }
        }
    }
}
