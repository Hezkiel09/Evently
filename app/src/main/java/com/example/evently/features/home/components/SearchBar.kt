package com.example.evently.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        OutlinedTextField(
            value = value,

            onValueChange = onValueChange,

            modifier = Modifier.weight(1f),

            singleLine = true,

            placeholder = {
                Text(
                    text = "Cari event favorit",
                    fontSize = 13.sp
                )
            },

            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Cari"
                )
            },

            shape = RoundedCornerShape(14.dp)
        )

        IconButton(
            onClick = {}
        ) {

            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                shadowElevation = 1.dp
            ) {

                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Filter",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}