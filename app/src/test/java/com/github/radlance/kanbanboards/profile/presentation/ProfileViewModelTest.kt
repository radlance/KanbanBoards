package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.kanbanboards.common.BaseTest
import com.github.radlance.kanbanboards.common.data.UserProfileEntity
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboards.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProfileViewModelTest : BaseTest() {

    private lateinit var profileRepository: TestProfileRepository
    private lateinit var profileViewModelWrapper: TestProfileViewModelWrapper
    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setup() {
        profileRepository = TestProfileRepository()
        profileViewModelWrapper = TestProfileViewModelWrapper()

        viewModel = ProfileViewModel(
            profileRepository = profileRepository,
            profileMapper = LoadProfileResultMapper(),
            profileViewModelWrapper = profileViewModelWrapper,
            runAsync = TestRunAsync()
        )
    }

    @Test
    fun test_initial_state() {
        assertEquals(1, profileViewModelWrapper.profileUiStateCalledCount)
        assertEquals(2, profileViewModelWrapper.saveProfileUiStateCalledList.size)
        assertEquals(
            ProfileUiState.Loading,
            profileViewModelWrapper.saveProfileUiStateCalledList[0]
        )
        assertEquals(
            ProfileUiState.Base(name = "test name", email = "test@gmail.com"),
            profileViewModelWrapper.saveProfileUiStateCalledList[1]
        )
    }

    @Test
    fun test_sign_out() {
        viewModel.signOut()
        assertEquals(1, profileRepository.signOutCalledCount)
    }

    private class TestProfileRepository : ProfileRepository {
        var profileCalledCount = 0
        var signOutCalledCount = 0

        private var userProfileEntity: UserProfileEntity = UserProfileEntity(
            email = "test@gmail.com",
            name = "test name"
        )

        override fun profile(): LoadProfileResult {
            profileCalledCount++
            val userProfileEntity = userProfileEntity
            return LoadProfileResult.Base(userProfileEntity.name!!, userProfileEntity.email)
        }

        override suspend fun signOut() {
            signOutCalledCount++
        }
    }

    private class TestProfileViewModelWrapper : ProfileViewModelWrapper {

        private val profileUiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Initial)
        var profileUiStateCalledCount = 0
        val saveProfileUiStateCalledList = mutableListOf<ProfileUiState>()

        override fun profileUiState(): StateFlow<ProfileUiState> {
            profileUiStateCalledCount++
            return profileUiState
        }

        override fun saveProfileUiState(profileUiState: ProfileUiState) {
            this.profileUiState.value = profileUiState
            saveProfileUiStateCalledList.add(profileUiState)
        }
    }
}