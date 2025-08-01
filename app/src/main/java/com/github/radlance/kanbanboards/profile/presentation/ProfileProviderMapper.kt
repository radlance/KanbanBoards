package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.api.service.ProfileProvider
import javax.inject.Inject

class ProfileProviderMapper @Inject constructor() : ProfileProvider.Mapper<ProfileProviderUi> {

    override fun mapEmail(): ProfileProviderUi = ProfileProviderUi.Email

    override fun mapGoogle(): ProfileProviderUi = ProfileProviderUi.Google
}