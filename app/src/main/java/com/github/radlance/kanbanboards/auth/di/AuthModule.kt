package com.github.radlance.kanbanboards.auth.di

import com.github.radlance.kanbanboards.auth.data.AuthRemoteDataSource
import com.github.radlance.kanbanboards.auth.data.BaseAuthRepository
import com.github.radlance.kanbanboards.auth.data.HandleAuthRemoteDataSource
import com.github.radlance.kanbanboards.auth.data.HandleAuthResult
import com.github.radlance.kanbanboards.auth.domain.SignInRepository
import com.github.radlance.kanbanboards.auth.domain.SignUpRepository
import com.github.radlance.kanbanboards.auth.presentation.common.MatchEmail
import com.github.radlance.kanbanboards.auth.presentation.common.ValidateAuth
import com.github.radlance.kanbanboards.auth.presentation.common.ValidateSignIn
import com.github.radlance.kanbanboards.auth.presentation.signin.AuthResultMapper
import com.github.radlance.kanbanboards.auth.presentation.signin.AuthResultUiState
import com.github.radlance.kanbanboards.auth.presentation.signin.CredentialResult
import com.github.radlance.kanbanboards.auth.presentation.signin.SignInCredentialMapper
import com.github.radlance.kanbanboards.auth.presentation.signin.SignInCredentialUiState
import com.github.radlance.kanbanboards.auth.presentation.signin.HandleSignIn
import com.github.radlance.kanbanboards.auth.presentation.signup.HandleSignUp
import com.github.radlance.kanbanboards.common.domain.UnitResult
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
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
        signInCredentialMapper: SignInCredentialMapper
    ): CredentialResult.Mapper<SignInCredentialUiState>

    @Binds
    fun provideSignInResultMapper(
        authResultMapper: AuthResultMapper
    ): UnitResult.Mapper<AuthResultUiState>

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

    @Binds
    fun provideHandleSignIn(handleSignIn: HandleSignIn.Base): HandleSignIn

    @Binds
    fun provideHandleSignUp(handleSignUp: HandleSignUp.Base): HandleSignUp
}