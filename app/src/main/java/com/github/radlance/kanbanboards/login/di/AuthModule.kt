package com.github.radlance.kanbanboards.login.di

import androidx.lifecycle.SavedStateHandle
import com.github.radlance.kanbanboards.login.data.AuthRemoteDataSource
import com.github.radlance.kanbanboards.login.data.BaseAuthRepository
import com.github.radlance.kanbanboards.login.data.HandleAuthResult
import com.github.radlance.kanbanboards.login.domain.AuthRepository
import com.github.radlance.kanbanboards.login.domain.AuthResult
import com.github.radlance.kanbanboards.login.presentation.CredentialResult
import com.github.radlance.kanbanboards.login.presentation.CredentialResultMapper
import com.github.radlance.kanbanboards.login.presentation.CredentialUiState
import com.github.radlance.kanbanboards.login.presentation.SignInResultMapper
import com.github.radlance.kanbanboards.login.presentation.SignInResultUiState
import com.github.radlance.kanbanboards.login.presentation.HandleSignIn
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
    fun provideHandleAuthResult(baseResult: HandleAuthResult.Base): HandleAuthResult

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

    @Binds
    fun provideAuthRemoteDataSource(authRemoteDataSource: AuthRemoteDataSource.Base): AuthRemoteDataSource
}

@Module
@InstallIn(ViewModelComponent::class)
class SignInViewModelModule {

    @ViewModelScoped
    @Provides
    fun provideHandleSignIn(savedStateHandle: SavedStateHandle): HandleSignIn {
        return HandleSignIn.Base(savedStateHandle)
    }
}
