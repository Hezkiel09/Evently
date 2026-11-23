package com.example.evently.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryChip(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(categories) { category ->

            val selected =
                category == selectedCategory

            FilterChip(
                selected = selected,

                onClick = {
                    onCategorySelected(category)
                },

                label = {
                    Text(
                        text = category,
                        fontSize = 12.sp
                    )
                },

                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF252525),
                    selectedLabelColor = Color.White,
                    containerColor = Color.White
                )
            )
        }
    }
}