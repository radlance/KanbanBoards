package com.github.radlance.login.di

import com.github.radlance.common.domain.UnitResult
import com.github.radlance.login.data.AuthRemoteDataSource
import com.github.radlance.login.data.BaseAuthRemoteDataSource
import com.github.radlance.login.data.BaseAuthRepository
import com.github.radlance.login.data.BaseHandleAuthRemoteDataSource
import com.github.radlance.login.data.BaseHandleAuthResult
import com.github.radlance.login.data.HandleAuthRemoteDataSource
import com.github.radlance.login.data.HandleAuthResult
import com.github.radlance.login.domain.SignInRepository
import com.github.radlance.login.domain.SignUpRepository
import com.github.radlance.login.presentation.common.BaseMatchEmail
import com.github.radlance.login.presentation.common.BaseValidateAuth
import com.github.radlance.login.presentation.common.MatchEmail
import com.github.radlance.login.presentation.common.ValidateAuth
import com.github.radlance.login.presentation.common.ValidateSignIn
import com.github.radlance.login.presentation.signin.AuthResultMapper
import com.github.radlance.login.presentation.signin.AuthResultUiState
import com.github.radlance.login.presentation.signin.BaseHandleSignIn
import com.github.radlance.login.presentation.signin.CredentialResult
import com.github.radlance.login.presentation.signin.HandleSignIn
import com.github.radlance.login.presentation.signin.SignInCredentialMapper
import com.github.radlance.login.presentation.signin.SignInCredentialUiState
import com.github.radlance.login.presentation.signup.BaseHandleSignUp
import com.github.radlance.login.presentation.signup.HandleSignUp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface AuthModule {

    @Binds
    fun provideHandleAuthResult(baseResult: BaseHandleAuthResult): HandleAuthResult

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
    fun provideAuthRemoteDataSource(authRemoteDataSource: BaseAuthRemoteDataSource): AuthRemoteDataSource

    @Binds
    fun provideHandleAuthRemoteDataSource(handle: BaseHandleAuthRemoteDataSource): HandleAuthRemoteDataSource

    @Binds
    fun provideValidateAuth(validateAuth: BaseValidateAuth): ValidateAuth

    @Binds
    fun provideValidateSignIn(validateAuth: BaseValidateAuth): ValidateSignIn

    @Binds
    fun provideMatchEmail(matchEmail: BaseMatchEmail): MatchEmail

    @Binds
    fun provideHandleSignIn(handleSignIn: BaseHandleSignIn): HandleSignIn

    @Binds
    fun provideHandleSignUp(handleSignUp: BaseHandleSignUp): HandleSignUp
}