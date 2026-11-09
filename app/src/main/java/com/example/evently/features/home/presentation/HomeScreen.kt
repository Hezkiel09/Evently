package com.example.evently.features.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.evently.features.home.components.CategoryChip
import com.example.evently.features.home.components.LocationHeader
import com.example.evently.features.home.components.SearchBar
import com.example.evently.features.home.components.UpComingEventSection
import com.example.evently.navigation.BottomNavBar

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onAddEventClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavBar()
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            LocationHeader()

            Spacer(modifier = Modifier.height(20.dp))

            SearchBar(
                value = uiState.searchQuery,
                onValueChange = {
                    viewModel.updateSearchQuery(it)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CategoryChip(
                categories = uiState.categories,
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = {
                    viewModel.selectCategory(it)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            UpComingEventSection(
                events = viewModel.getFilteredEvents(),
                onAddEventClick = onAddEventClick
            )
        }
    }
}