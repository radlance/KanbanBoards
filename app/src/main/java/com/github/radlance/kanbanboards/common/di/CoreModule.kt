package com.github.radlance.kanbanboards.common.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.github.radlance.kanbanboards.common.core.ManageResource
import com.github.radlance.kanbanboards.common.data.DataStoreManager
import com.github.radlance.kanbanboards.common.data.HandleError
import com.github.radlance.kanbanboards.common.data.IgnoreHandle
import com.github.radlance.kanbanboards.service.ProvideDatabase
import com.github.radlance.kanbanboards.common.data.RemoteUsersRepository
import com.github.radlance.kanbanboards.common.data.UsersRemoteDataSource
import com.github.radlance.kanbanboards.common.domain.UsersRepository
import com.github.radlance.kanbanboards.common.presentation.DispatchersList
import com.github.radlance.kanbanboards.common.presentation.RunAsync
import com.github.radlance.kanbanboards.service.Auth
import com.github.radlance.kanbanboards.service.MyUser
import com.github.radlance.kanbanboards.service.Service
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

    @Provides
    @Singleton
    fun provideService(provideDatabase: ProvideDatabase): Service {
        return Service.Base(provideDatabase)
    }

    @Singleton
    @Provides
    fun provideRunAsync(dispatchersList: DispatchersList): RunAsync {
        return RunAsync.Base(dispatchersList)
    }

    @Provides
    @Singleton
    fun provideMyUser(): MyUser = MyUser.Base()

    @Provides
    @Singleton
    fun provideUsersRemoteDataSource(
        service: Service,
        myUser: MyUser
    ): UsersRemoteDataSource = UsersRemoteDataSource.Base(service, myUser)

    @Provides
    @Singleton
    fun provideUsersRepository(usersRemoteDataSource: UsersRemoteDataSource): UsersRepository {
        return RemoteUsersRepository(usersRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideIgnoreHandle(): IgnoreHandle = IgnoreHandle.Base()

    @Provides
    @Singleton
    fun provideAuth(): Auth = Auth.Base()

    companion object {
        private val Context.datastore by preferencesDataStore("settings")
    }
}