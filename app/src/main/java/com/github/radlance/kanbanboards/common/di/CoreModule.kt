package com.github.radlance.kanbanboards.common.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.common.data.DataStoreManager
import com.github.radlance.kanbanboards.common.data.HandleError
import com.github.radlance.kanbanboards.common.data.ProvideDatabase
import com.github.radlance.kanbanboards.common.presentation.DispatchersList
import com.github.radlance.kanbanboards.common.presentation.RunAsync
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
    fun provideDispatcherList(): DispatchersList = DispatchersList.Base()

    @Singleton
    @Provides
    fun provideDataStore(context: Context): DataStore<Preferences> = context.datastore

    @Singleton
    @Provides
    fun provideDataStoreManager(dataStore: DataStore<Preferences>): DataStoreManager {
        return DataStoreManager.Base(dataStore)
    }

    @Singleton
    @Provides
    fun provideRunAsync(dispatchersList: DispatchersList): RunAsync {
        return RunAsync.Base(dispatchersList)
    }

    companion object {
        private val Context.datastore by preferencesDataStore("settings")
    }
}