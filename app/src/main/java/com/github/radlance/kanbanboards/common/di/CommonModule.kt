package com.github.radlance.kanbanboards.common.di

import android.content.Context
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.common.data.HandleError
import com.github.radlance.kanbanboards.common.data.RemoteDataSource
import com.github.radlance.kanbanboards.common.presentation.DispatchersList
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

    @Singleton
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context = context

    @Singleton
    @Provides
    fun provideManageResource(context: Context): ManageResource = ManageResource.Base(context)

    @Singleton
    @Provides
    fun provideHandleError(): HandleError = HandleError.Base()

    @Singleton
    @Provides
    fun provideFirebase(): Firebase = Firebase

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        firebase: Firebase,
        handleError: HandleError
    ): RemoteDataSource = RemoteDataSource.FirebaseClient(
        firebase = firebase,
        handle = handleError
    )

    @Singleton
    @Provides
    fun provideDispatcherList(): DispatchersList = DispatchersList.Base()
}