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
import com.example.evently.features.announcement.presentation.AnnouncementViewModel
import com.example.evently.features.announcement.presentation.screens.AnnouncementDetailScreen
import com.example.evently.features.announcement.presentation.screens.AnnouncementFeedScreen
import com.example.evently.features.announcement.presentation.screens.AnnouncementFormScreen

// Definisi Route Navigasi Resmi Proyek
sealed class Screen(val route: String) {
    object AnnouncementFeed : Screen("announcement_feed")
    object AnnouncementDetail : Screen("announcement_detail")
    object AnnouncementForm : Screen("announcement_form")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            // Inisialisasi ViewModel (Bertahan dari Screen Rotation)
            val announcementViewModel: AnnouncementViewModel = viewModel()

            NavHost(
                navController = navController,
                startDestination = Screen.AnnouncementFeed.route
            ) {
                // Modul 4: Fitur Pengumuman (Jefry Hindrayanto)
                announcementNavGraph(navController, announcementViewModel)

                // Tempat menggabungkan modul anggota lain nanti:
                // catalogNavGraph(navController, catalogViewModel)      // Fitur 1: Yehezkiel
                // ticketNavGraph(navController, ticketViewModel)        // Fitur 2: Noel
                // certificateNavGraph(navController, certificateViewModel) // Fitur 3: Steven
                // scheduleNavGraph(navController, scheduleViewModel)    // Fitur 5: Sayyid
            }
        }
    }
}

// Extension Function untuk Navigasi Modular Fitur Pengumuman
fun NavGraphBuilder.announcementNavGraph(
    navController: NavHostController,
    viewModel: AnnouncementViewModel
) {
    // 1. Halaman Feed Pengumuman
    composable(Screen.AnnouncementFeed.route) {
        val announcements by viewModel.announcements.collectAsState()

        AnnouncementFeedScreen(
            announcements = announcements,
            onAnnouncementClick = { item ->
                viewModel.selectAnnouncement(item)
                navController.navigate(Screen.AnnouncementDetail.route)
            },
            onFabClick = {
                viewModel.selectAnnouncement(null) // Reset ke Mode Buat Baru
                navController.navigate(Screen.AnnouncementForm.route)
            }
        )
    }

    // 2. Halaman Detail Pengumuman
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
            // Fallback jika tidak ada data yang dipilih
            LaunchedEffect(Unit) {
                navController.popBackStack()
            }
        }
    }

    // 3. Halaman Form Pengumuman (Tambah / Edit)
    composable(Screen.AnnouncementForm.route) {
        val selectedAnnouncement by viewModel.selectedAnnouncement.collectAsState()
        val events by viewModel.events.collectAsState()

        AnnouncementFormScreen(
            initialAnnouncement = selectedAnnouncement,
            eventOptions = events, // List EventDto dinamis dari Supabase
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