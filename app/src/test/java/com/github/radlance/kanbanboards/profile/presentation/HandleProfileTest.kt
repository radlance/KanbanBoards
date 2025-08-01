//package com.github.radlance.kanbanboards.profile.presentation
//
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Test
//
//class HandleProfileTest {
//
//    private lateinit var handleProfile: HandleProfile
//
//    @Before
//    fun setup() {
//        handleProfile = HandleProfile.Base()
//    }
//
//    @Test
//    fun test_profile_ui_state() {
//        assertEquals(ProfileUiState.Initial, handleProfile.profileUiState.value)
//        handleProfile.saveProfileUiState(ProfileUiState.Loading)
//        assertEquals(ProfileUiState.Loading, handleProfile.profileUiState.value)
//
//        handleProfile.saveProfileUiState(
//            ProfileUiState.Base(
//                name = "name",
//                email = "email@example.com"
//            )
//        )
//
//        assertEquals(
//            ProfileUiState.Base(
//                name = "name",
//                email = "email@example.com"
//            ),
//            handleProfile.profileUiState.value
//        )
//    }
//}