package com.github.radlance.kanbanboards.common.di

import android.content.Context
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.common.data.HandleError
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.github.radlance.kanbanboards.common.data.RemoteDataSource
import com.github.radlance.kanbanboards.common.presentation.DispatchersList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoreModule {

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
    fun provideProvideDatabase(): ProvideDatabase = ProvideDatabase.Base()

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        handleError: HandleError,
        provideDatabase: ProvideDatabase
    ): RemoteDataSource = RemoteDataSource.FirebaseClient(
        handle = handleError,
        provideDatabase = provideDatabase
    )

    @Singleton
    @Provides
    fun provideDispatcherList(): DispatchersList = DispatchersList.Base()
}