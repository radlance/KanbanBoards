package com.github.radlance.kanbanboards.auth.di

import androidx.lifecycle.SavedStateHandle
import com.github.radlance.kanbanboards.auth.data.AuthRemoteDataSource
import com.github.radlance.kanbanboards.auth.data.BaseAuthRepository
import com.github.radlance.kanbanboards.auth.data.HandleAuthRemoteDataSource
import com.github.radlance.kanbanboards.auth.data.HandleAuthResult
import com.github.radlance.kanbanboards.auth.domain.AuthRepository
import com.github.radlance.kanbanboards.auth.domain.SignInRepository
import com.github.radlance.kanbanboards.auth.domain.AuthResult
import com.github.radlance.kanbanboards.auth.domain.SignUpRepository
import com.github.radlance.kanbanboards.auth.presentation.common.MatchEmail
import com.github.radlance.kanbanboards.auth.presentation.common.ValidateAuth
import com.github.radlance.kanbanboards.auth.presentation.common.ValidateSignIn
import com.github.radlance.kanbanboards.auth.presentation.signin.CredentialResult
import com.github.radlance.kanbanboards.auth.presentation.signin.CredentialResultMapper
import com.github.radlance.kanbanboards.auth.presentation.signin.CredentialUiState
import com.github.radlance.kanbanboards.auth.presentation.signin.AuthResultMapper
import com.github.radlance.kanbanboards.auth.presentation.signin.AuthResultUiState
import com.github.radlance.kanbanboards.auth.presentation.signin.HandleSignIn
import com.github.radlance.kanbanboards.auth.presentation.signup.HandleSignUp
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
    fun provideSignInRepository(signInRepository: BaseAuthRepository): SignInRepository

    @Binds
    fun provideSignUpRepository(signUpRepository: BaseAuthRepository): SignUpRepository

    @Binds
    fun provideCredentialResultMapper(
        credentialResultMapper: CredentialResultMapper
    ): CredentialResult.Mapper<CredentialUiState>

    @Binds
    fun provideSignInResultMapper(
        authResultMapper: AuthResultMapper
    ): AuthResult.Mapper<AuthResultUiState>

    @Binds
    fun provideAuthRemoteDataSource(authRemoteDataSource: AuthRemoteDataSource.Base): AuthRemoteDataSource

    @Binds
    fun provideHandleAuthRemoteDataSource(handle: HandleAuthRemoteDataSource.Base): HandleAuthRemoteDataSource
    @Binds
    fun provideValidateAuth(validateAuth: ValidateAuth.Base): ValidateAuth

    @Binds
    fun provideValidateSignIn(validateAuth: ValidateAuth.Base): ValidateSignIn

    @Binds
    fun provideMatchEmail(matchEmail: MatchEmail.Base): MatchEmail
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

@Module
@InstallIn(ViewModelComponent::class)
class SignUpViewModelModule {
    @ViewModelScoped
    @Provides
    fun provideHandleSignUp(savedStateHandle: SavedStateHandle): HandleSignUp {
        return HandleSignUp.Base(savedStateHandle)
    }
}
