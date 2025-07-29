package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.kanbanboards.profile.domain.ProfileProvider
import javax.inject.Inject

class ProfileProviderMapper @Inject constructor(): ProfileProvider.Mapper<ProfileProviderUi> {

    override fun mapEmail(): ProfileProviderUi = ProfileProviderUi.Email

    override fun mapGoogle(): ProfileProviderUi = ProfileProviderUi.Google
}