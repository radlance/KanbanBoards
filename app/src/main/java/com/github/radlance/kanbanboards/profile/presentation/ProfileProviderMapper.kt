package com.github.radlance.kanbanboards.profile.presentation

import javax.inject.Inject

class ProfileProviderMapper @Inject constructor() :
    com.github.radlance.api.service.ProfileProvider.Mapper<ProfileProviderUi> {

    override fun mapEmail(): ProfileProviderUi = ProfileProviderUi.Email

    override fun mapGoogle(): ProfileProviderUi = ProfileProviderUi.Google
}