package com.github.radlance.auth.di

import com.github.radlance.auth.data.AuthRemoteDataSource
import com.github.radlance.auth.data.BaseAuthRepository
import com.github.radlance.auth.data.HandleAuthRemoteDataSource
import com.github.radlance.auth.data.HandleAuthResult
import com.github.radlance.auth.domain.SignInRepository
import com.github.radlance.auth.domain.SignUpRepository
import com.github.radlance.auth.presentation.common.BaseValidateAuth
import com.github.radlance.auth.presentation.common.MatchEmail
import com.github.radlance.auth.presentation.common.ValidateAuth
import com.github.radlance.auth.presentation.common.ValidateSignIn
import com.github.radlance.auth.presentation.signin.AuthResultMapper
import com.github.radlance.auth.presentation.signin.AuthResultUiState
import com.github.radlance.auth.presentation.signin.BaseHandleSignIn
import com.github.radlance.auth.presentation.signin.CredentialResult
import com.github.radlance.auth.presentation.signin.HandleSignIn
import com.github.radlance.auth.presentation.signin.SignInCredentialMapper
import com.github.radlance.auth.presentation.signin.SignInCredentialUiState
import com.github.radlance.auth.presentation.signup.BaseHandleSignUp
import com.github.radlance.auth.presentation.signup.HandleSignUp
import com.github.radlance.core.domain.UnitResult
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface AuthModule {

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
    fun provideValidateAuth(validateAuth: BaseValidateAuth): ValidateAuth

    @Binds
    fun provideValidateSignIn(validateAuth: BaseValidateAuth): ValidateSignIn

    @Binds
    fun provideMatchEmail(matchEmail: MatchEmail.Base): MatchEmail

    @Binds
    fun provideHandleSignIn(handleSignIn: BaseHandleSignIn): HandleSignIn

    @Binds
    fun provideHandleSignUp(handleSignUp: BaseHandleSignUp): HandleSignUp
}