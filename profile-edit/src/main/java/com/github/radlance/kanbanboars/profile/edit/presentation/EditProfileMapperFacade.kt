package com.github.radlance.kanbanboars.profile.edit.presentation

import com.github.radlance.kanbanboards.api.service.ProfileProvider
import com.github.radlance.kanbanboards.auth.presentation.signin.CredentialResult
import com.github.radlance.kanbanboards.core.domain.UnitResult
import com.github.radlance.kanbanboards.profile.domain.LoadProfileResult
import javax.inject.Inject

interface EditProfileMapperFacade {

    fun mapLoadProfileResult(loadProfileResult: LoadProfileResult): ProfileEditUiState

    fun mapProfileProvider(profileProvider: ProfileProvider): ProfileProviderUi

    fun mapProfileCredentialResult(credentialResult: CredentialResult): ProfileCredentialUiState

    fun mapDeleteProfileResult(deleteProfileResult: UnitResult): DeleteProfileUiState

    fun mapEditProfileResult(editProfileResult: UnitResult): EditProfileUiState
}

internal class BaseEditProfileMapperFacade @Inject constructor(
    private val profileEditMapper: LoadProfileResult.Mapper<ProfileEditUiState>,
    private val editProfileMapper: UnitResult.Mapper<EditProfileUiState>,
    private val profileProviderMapper: ProfileProvider.Mapper<ProfileProviderUi>,
    private val profileCredentialMapper: CredentialResult.Mapper<ProfileCredentialUiState>,
    private val deleteProfileMapper: UnitResult.Mapper<DeleteProfileUiState>
) : EditProfileMapperFacade {

    override fun mapLoadProfileResult(loadProfileResult: LoadProfileResult): ProfileEditUiState {
        return loadProfileResult.map(profileEditMapper)
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

    override fun mapEditProfileResult(editProfileResult: UnitResult): EditProfileUiState {
        return editProfileResult.map(editProfileMapper)
    }
}