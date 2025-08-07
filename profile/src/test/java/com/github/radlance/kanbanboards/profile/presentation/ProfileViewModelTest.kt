package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.kanbanboards.core.BaseTest
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboards.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProfileViewModelTest : BaseTest() {

    private lateinit var repository: TestProfileRepository
    private lateinit var manageResource: TestManageResource

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setup() {
        repository = TestProfileRepository()
        manageResource = TestManageResource()

        viewModel = ProfileViewModel(
            profileRepository = repository,
            facade = BaseProfileMapperFacade(
                loadProfileMapper = LoadProfileResultMapper()
            ),
            runAsync = TestRunAsync()
        )
    }

    @Test
    fun test_initial_state() {
        assertEquals(
            ProfileUiState.Error(message = "initial"),
            viewModel.profileUiState.value
        )
    }

    @Test
    fun test_collect_profile() {
        repository.makeExpectedLoadProfileResult(LoadProfileResult.Error("some error"))
        assertEquals(viewModel.profileUiState.value, ProfileUiState.Error("some error"))
        assertEquals(1, repository.profileCalledCount)
        repository.makeExpectedLoadProfileResult(
            LoadProfileResult.Success(
                name = "name",
                email = "test@gmail.com"
            )
        )
        assertEquals(
            viewModel.profileUiState.value, ProfileUiState.Base(
                name = "name",
                email = "test@gmail.com"
            )
        )
        assertEquals(1, repository.profileCalledCount)
    }

    @Test
    fun test_sign_out() {
        viewModel.signOut()
        assertEquals(1, repository.signOutCalledCount)
    }

    private class TestProfileRepository : ProfileRepository {
        var profileCalledCount = 0
        var signOutCalledCount = 0

        private val loadProfileResult = MutableStateFlow<LoadProfileResult>(
            LoadProfileResult.Error("initial")
        )

        fun makeExpectedLoadProfileResult(loadProfileResult: LoadProfileResult) {
            this.loadProfileResult.value = loadProfileResult
        }

        override fun profile(): Flow<LoadProfileResult> {
            profileCalledCount++
            return loadProfileResult
        }

        override suspend fun signOut() {
            signOutCalledCount++
        }
    }
}