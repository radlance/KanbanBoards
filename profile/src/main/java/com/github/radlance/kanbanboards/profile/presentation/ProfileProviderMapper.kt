package com.github.radlance.kanbanboards.profile.presentation

import com.github.radlance.kanbanboards.api.service.ProfileProvider
import javax.inject.Inject

internal class ProfileProviderMapper @Inject constructor() :
    ProfileProvider.Mapper<ProfileProviderUi> {

    override fun mapEmail(): ProfileProviderUi = ProfileProviderUi.Email

    override fun mapGoogle(): ProfileProviderUi = ProfileProviderUi.Google
}