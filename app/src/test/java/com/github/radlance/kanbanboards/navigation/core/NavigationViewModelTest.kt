//package com.github.radlance.kanbanboards.navigation.core
//
//import com.github.radlance.kanbanboards.common.BaseTest
//import com.github.radlance.kanbanboards.navigation.domain.NavigationRepository
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableStateFlow
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Test
//
//class NavigationViewModelTest : BaseTest() {
//
//    private lateinit var navigationRepository: TestNavigationRepository
//    private lateinit var viewModel: NavigationViewModel
//
//    @Before
//    fun setup() {
//        navigationRepository = TestNavigationRepository()
//
//        viewModel = NavigationViewModel(
//            navigationRepository = navigationRepository,
//            runAsync = TestRunAsync()
//        )
//    }
//
//    @Test
//    fun test_initial_state() {
//        assertEquals(AuthorizedUiState.Unauthorized, viewModel.authorized.value)
//        assertEquals(1, navigationRepository.authorizedStatusCalledCount)
//    }
//
//    @Test
//    fun test_collect_authorized_state() {
//        navigationRepository.saveAuthorizedState(authorized = true)
//        assertEquals(AuthorizedUiState.Authorized, viewModel.authorized.value)
//        assertEquals(1, navigationRepository.authorizedStatusCalledCount)
//        navigationRepository.saveAuthorizedState(authorized = false)
//        assertEquals(AuthorizedUiState.Unauthorized, viewModel.authorized.value)
//        assertEquals(1, navigationRepository.authorizedStatusCalledCount)
//    }
//
//    private class TestNavigationRepository : NavigationRepository {
//
//        private var authorizedStatus = MutableStateFlow(false)
//        var authorizedStatusCalledCount = 0
//
//        fun saveAuthorizedState(authorized: Boolean) {
//            authorizedStatus.value = authorized
//        }
//
//        override fun authorizedStatus(): Flow<Boolean> {
//            authorizedStatusCalledCount++
//            return authorizedStatus
//        }
//    }
//}