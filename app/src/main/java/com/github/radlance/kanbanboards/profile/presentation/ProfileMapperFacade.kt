package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.kanbanboards.profile.domain.ProfileProvider
import javax.inject.Inject

interface ProfileMapperFacade {

    fun mapLoadProfileResult(loadProfileResult: LoadProfileResult): ProfileUiState

    fun mapProfileProvider(profileProvider: ProfileProvider): ProfileProviderUi

    class Base @Inject constructor(
        private val loadProfileMapper: LoadProfileResult.Mapper<ProfileUiState>,
        private val profileProviderMapper: ProfileProvider.Mapper<ProfileProviderUi>
    ) : ProfileMapperFacade {

        override fun mapLoadProfileResult(loadProfileResult: LoadProfileResult): ProfileUiState {
            return loadProfileResult.map(loadProfileMapper)
        }

        override fun mapProfileProvider(profileProvider: ProfileProvider): ProfileProviderUi {
            return profileProvider.map(profileProviderMapper)
        }
    }
}