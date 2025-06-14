package com.github.radlance.kanbanboards.auth.di

import androidx.lifecycle.SavedStateHandle
import com.github.radlance.kanbanboards.auth.data.BaseAuthRepository
import com.github.radlance.kanbanboards.auth.data.HandleAuthResult
import com.github.radlance.kanbanboards.auth.domain.AuthRepository
import com.github.radlance.kanbanboards.auth.domain.AuthResult
import com.github.radlance.kanbanboards.auth.presentation.CredentialResult
import com.github.radlance.kanbanboards.auth.presentation.CredentialResultMapper
import com.github.radlance.kanbanboards.auth.presentation.CredentialUiState
import com.github.radlance.kanbanboards.auth.presentation.SignInResultMapper
import com.github.radlance.kanbanboards.auth.presentation.SignInResultUiState
import com.github.radlance.kanbanboards.auth.presentation.SignInViewModelWrapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {

    @Binds
    fun provideHandleAuthResult(handleAuthResult: HandleAuthResult.HandleAuth): HandleAuthResult

    @Binds
    fun provideAuthRepository(authRepository: BaseAuthRepository): AuthRepository

    @Binds
    fun provideCredentialResultMapper(
        credentialResultMapper: CredentialResultMapper
    ): CredentialResult.Mapper<CredentialUiState>

    @Binds
    fun provideSignInResultMapper(
        signInResultMapper: SignInResultMapper
    ): AuthResult.Mapper<SignInResultUiState>
}

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @ViewModelScoped
    @Provides
    fun provideSignInViewModelWrapper(savedStateHandle: SavedStateHandle): SignInViewModelWrapper {
        return SignInViewModelWrapper.Base(savedStateHandle)
    }
}
