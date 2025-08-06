package com.github.radlance.kanbanboards.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.github.radlance.kanbanboards.api.service.MyUser
import com.github.radlance.kanbanboards.api.service.Service
import com.github.radlance.kanbanboards.core.core.BaseManageResource
import com.github.radlance.kanbanboards.core.core.ManageResource
import com.github.radlance.kanbanboards.core.data.BaseBoardsRemoteDataSource
import com.github.radlance.kanbanboards.core.data.BaseDataStoreManager
import com.github.radlance.kanbanboards.core.data.BaseHandleError
import com.github.radlance.kanbanboards.core.data.BaseHandleUnitResult
import com.github.radlance.kanbanboards.core.data.BaseIgnoreHandle
import com.github.radlance.kanbanboards.core.data.BaseUsersRemoteDataSource
import com.github.radlance.kanbanboards.core.data.BoardsRemoteDataSource
import com.github.radlance.kanbanboards.core.data.DataStoreManager
import com.github.radlance.kanbanboards.core.data.HandleError
import com.github.radlance.kanbanboards.core.data.HandleUnitResult
import com.github.radlance.kanbanboards.core.data.IgnoreHandle
import com.github.radlance.kanbanboards.core.data.RemoteUsersRepository
import com.github.radlance.kanbanboards.core.data.UsersRemoteDataSource
import com.github.radlance.kanbanboards.core.domain.UsersRepository
import com.github.radlance.kanbanboards.core.presentation.BaseDispatchersList
import com.github.radlance.kanbanboards.core.presentation.BaseRunAsync
import com.github.radlance.kanbanboards.core.presentation.DispatchersList
import com.github.radlance.kanbanboards.core.presentation.RunAsync
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class CoreModule {

    @Singleton
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context = context

    @Singleton
    @Provides
    fun provideManageResource(context: Context): ManageResource = BaseManageResource(context)

    @Singleton
    @Provides
    fun provideHandleError(): HandleError = BaseHandleError()

    @Singleton
    @Provides
    fun provideDispatcherList(): DispatchersList = BaseDispatchersList()

    @Singleton
    @Provides
    fun provideDataStore(context: Context): DataStore<Preferences> = context.datastore

    @Singleton
    @Provides
    fun provideDataStoreManager(dataStore: DataStore<Preferences>): DataStoreManager {
        return BaseDataStoreManager(dataStore)
    }

    @Singleton
    @Provides
    fun provideRunAsync(dispatchersList: DispatchersList): RunAsync {
        return BaseRunAsync(dispatchersList)
    }

    @Provides
    @Singleton
    fun provideUsersRepository(usersRemoteDataSource: UsersRemoteDataSource): UsersRepository {
        return RemoteUsersRepository(usersRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideIgnoreHandle(): IgnoreHandle = BaseIgnoreHandle()

    @Provides
    @Singleton
    fun provideUsersRemoteDataSource(
        service: Service,
        myUser: MyUser
    ): UsersRemoteDataSource = BaseUsersRemoteDataSource(service, myUser)

    @Provides
    @Singleton
    fun provideBoardsRemoteDataSource(
        service: Service,
        myUser: MyUser
    ): BoardsRemoteDataSource = BaseBoardsRemoteDataSource(service, myUser)

    @Provides
    @Singleton
    fun provideHandleUnitResult(manageResource: ManageResource): HandleUnitResult {
        return BaseHandleUnitResult(manageResource)
    }


    companion object {
        private val Context.datastore by preferencesDataStore("settings")
    }
}