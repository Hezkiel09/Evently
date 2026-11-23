package com.example.evently

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.evently.core.auth.AuthRepository
import com.example.evently.core.ui.components.EventlyNavItem
import com.example.evently.features.announcement.presentation.AnnouncementViewModel
import com.example.evently.features.announcement.presentation.screens.AnnouncementDetailScreen
import com.example.evently.features.announcement.presentation.screens.AnnouncementFeedScreen
import com.example.evently.features.announcement.presentation.screens.AnnouncementFormScreen
import com.example.evently.features.auth.presentation.AuthViewModel
import com.example.evently.features.auth.presentation.ForgotPasswordScreen
import com.example.evently.features.auth.presentation.LoginScreen
import com.example.evently.features.auth.presentation.RegisterScreen
import com.example.evently.features.auth.presentation.RoleSelectionScreen
import com.example.evently.features.crudevent.presentation.AddEditEventScreen
import com.example.evently.features.crudevent.presentation.AddEditViewModel
import com.example.evently.features.home.presentation.HomeScreen
import com.example.evently.features.home.presentation.HomeViewModel
import com.example.evently.features.ticket.presentation.TicketFeatureRoute

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object RoleSelection : Screen("role_selection")
    object ForgotPassword : Screen("forgot_password")
    object Home : Screen("home")
    object AddEditEvent : Screen("add_edit_event")
    object Ticket : Screen("ticket")
    object AnnouncementFeed : Screen("announcement_feed")
    object AnnouncementDetail : Screen("announcement_detail")
    object AnnouncementForm : Screen("announcement_form")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            val authViewModel: AuthViewModel = viewModel()
            val announcementViewModel: AnnouncementViewModel = viewModel()
            val homeViewModel: HomeViewModel = viewModel()
            val addEditViewModel: AddEditViewModel = viewModel()

            // Debug installs should open Login so Register/Login can be tested repeatedly.
            // Release installs restore Home only when a Supabase session already exists.
            val isDebugBuild = applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE != 0
            val startDest = if (isDebugBuild) {
                Screen.Login.route
            } else if (AuthRepository().isUserLoggedIn()) {
                Screen.Home.route
            } else {
                Screen.Login.route
            }

            NavHost(
                navController = navController,
                startDestination = startDest
            ) {
                // Auth Nav Graph
                authNavGraph(navController, authViewModel, homeViewModel)

                // Modul 1 & CRUD Event (Yehezkiel)
                catalogNavGraph(navController, homeViewModel, addEditViewModel, authViewModel)

                // Modul 2: Ticket (Noel)
                composable(Screen.Ticket.route) {
                    TicketFeatureRoute()
                }

                // Modul 4: Announcement (Jefry)
                announcementNavGraph(navController, announcementViewModel)
            }
        }
    }
}

// Extension Function untuk Navigasi Auth Flow
fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel
) {
    composable(Screen.Login.route) {
        LoginScreen(
            viewModel = authViewModel,
            onLoginSuccess = {
                homeViewModel.loadUserRole()
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            },
            onNavigateToRegister = {
                navController.navigate(Screen.Register.route)
            },
            onNavigateToForgot = {
                navController.navigate(Screen.ForgotPassword.route)
            }
        )
    }

    composable(Screen.Register.route) {
        RegisterScreen(
            viewModel = authViewModel,
            onNavigateToRoleSelection = {
                navController.navigate(Screen.RoleSelection.route)
            },
            onNavigateToLogin = {
                navController.popBackStack()
            }
        )
    }

    composable(Screen.RoleSelection.route) {
        RoleSelectionScreen(
            viewModel = authViewModel,
            onRegistrationSuccess = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Login.route) { inclusive = false }
                    launchSingleTop = true
                }
            },
            onBackClick = {
                navController.popBackStack()
            }
        )
    }

    composable(Screen.ForgotPassword.route) {
        ForgotPasswordScreen(
            viewModel = authViewModel,
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}

// Extension Function untuk Navigasi Modular Fitur Event / Catalog
fun NavGraphBuilder.catalogNavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    addEditViewModel: AddEditViewModel,
    authViewModel: AuthViewModel
) {
    composable(Screen.Home.route) {
        val authState by authViewModel.uiState.collectAsState()
        HomeScreen(
            viewModel = homeViewModel,
            isLoggingOut = authState.isLoading,
            authErrorMessage = authState.errorMessage,
            onAddEventClick = {
                addEditViewModel.resetForm()
                navController.navigate(Screen.AddEditEvent.route)
            },
            onEditEventClick = { eventUiModel ->
                val fullEvent = homeViewModel.findEvent(eventUiModel.id)
                if (fullEvent != null) {
                    addEditViewModel.loadEvent(fullEvent)
                } else {
                    addEditViewModel.resetForm()
                }
                navController.navigate(Screen.AddEditEvent.route)
            },
            onLogoutClick = {
                authViewModel.signOut {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            },
            onNavItemClick = { navItem ->
                when (navItem) {
                    EventlyNavItem.HOME, EventlyNavItem.EXPLORE -> {}
                    EventlyNavItem.ANNOUNCEMENT -> {
                        navController.navigate(Screen.AnnouncementFeed.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    EventlyNavItem.TICKET -> {
                        navController.navigate(Screen.Ticket.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    EventlyNavItem.PROFILE -> {
                        navController.navigate(Screen.AnnouncementFeed.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            }
        )
    }

    composable(Screen.AddEditEvent.route) {
        AddEditEventScreen(
            viewModel = addEditViewModel,
            onBackClick = {
                homeViewModel.refreshEvents()
                navController.popBackStack()
            }
        )
    }
}

// Extension Function untuk Navigasi Modular Fitur Pengumuman
fun NavGraphBuilder.announcementNavGraph(
    navController: NavHostController,
    viewModel: AnnouncementViewModel
) {
    composable(Screen.AnnouncementFeed.route) {
        val announcements by viewModel.announcements.collectAsState()

        AnnouncementFeedScreen(
            announcements = announcements,
            onAnnouncementClick = { item ->
                viewModel.selectAnnouncement(item)
                navController.navigate(Screen.AnnouncementDetail.route)
            },
            onFabClick = {
                viewModel.selectAnnouncement(null)
                navController.navigate(Screen.AnnouncementForm.route)
            },
            onNavItemClick = { navItem ->
                when (navItem) {
                    EventlyNavItem.HOME, EventlyNavItem.EXPLORE -> {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    EventlyNavItem.TICKET -> {
                        navController.navigate(Screen.Ticket.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    EventlyNavItem.ANNOUNCEMENT -> {}
                    EventlyNavItem.PROFILE -> {}
                }
            }
        )
    }

    composable(Screen.AnnouncementDetail.route) {
        val selectedAnnouncement by viewModel.selectedAnnouncement.collectAsState()

        selectedAnnouncement?.let { announcement ->
            AnnouncementDetailScreen(
                announcement = announcement,
                onBackClick = { navController.popBackStack() },
                onEditClick = { navController.navigate(Screen.AnnouncementForm.route) },
                onDeleteClick = {
                    navController.popBackStack()
                    viewModel.deleteAnnouncement(announcement.id)
                }
            )
        } ?: run {
            LaunchedEffect(Unit) {
                navController.popBackStack()
            }
        }
    }

    composable(Screen.AnnouncementForm.route) {
        val selectedAnnouncement by viewModel.selectedAnnouncement.collectAsState()
        val events by viewModel.events.collectAsState()

        AnnouncementFormScreen(
            initialAnnouncement = selectedAnnouncement,
            eventOptions = events,
            onBackClick = { navController.popBackStack() },
            onSubmit = { title, eventId, content ->
                viewModel.saveAnnouncement(
                    title = title,
                    eventId = eventId,
                    content = content,
                    editingId = selectedAnnouncement?.id
                )
                navController.popBackStack(Screen.AnnouncementFeed.route, inclusive = false)
            }
        )
    }
}
