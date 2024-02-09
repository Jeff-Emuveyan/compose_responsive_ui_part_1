package com.example.compose_responsive_ui_part_1.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.compose_responsive_ui_part_1.R

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {

    object Movies: Screen("movies", R.string.movies, Icons.Filled.DateRange)
    object Bookmarks: Screen("bookmarks", R.string.bookmarks, Icons.Filled.Add)
    object Settings: Screen("settings", R.string.settings, Icons.Filled.Settings)
}