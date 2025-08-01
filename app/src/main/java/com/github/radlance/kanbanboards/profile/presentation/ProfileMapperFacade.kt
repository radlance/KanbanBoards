package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.api.service.ProfileProvider
import com.github.radlance.common.domain.UnitResult
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import com.github.radlance.login.presentation.signin.CredentialResult
import javax.inject.Inject

interface ProfileMapperFacade {

    fun mapLoadProfileResult(loadProfileResult: LoadProfileResult): ProfileUiState

    fun mapProfileProvider(profileProvider: ProfileProvider): ProfileProviderUi

    fun mapProfileCredentialResult(credentialResult: CredentialResult): ProfileCredentialUiState

    fun mapDeleteProfileResult(deleteProfileResult: UnitResult): DeleteProfileUiState

    class Base @Inject constructor(
        private val loadProfileMapper: LoadProfileResult.Mapper<ProfileUiState>,
        private val profileProviderMapper: ProfileProvider.Mapper<ProfileProviderUi>,
        private val profileCredentialMapper: CredentialResult.Mapper<ProfileCredentialUiState>,
        private val deleteProfileMapper: UnitResult.Mapper<DeleteProfileUiState>
    ) : ProfileMapperFacade {

        override fun mapLoadProfileResult(loadProfileResult: LoadProfileResult): ProfileUiState {
            return loadProfileResult.map(loadProfileMapper)
        }

        override fun mapProfileProvider(profileProvider: ProfileProvider): ProfileProviderUi {
            return profileProvider.map(profileProviderMapper)
        }

        override fun mapProfileCredentialResult(credentialResult: CredentialResult): ProfileCredentialUiState {
            return credentialResult.map(profileCredentialMapper)
        }

        override fun mapDeleteProfileResult(deleteProfileResult: UnitResult): DeleteProfileUiState {
            return deleteProfileResult.map(deleteProfileMapper)
        }
    }
}