package com.konkuk.medicarecall.ui.common.extension

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.konkuk.medicarecall.ui.navigation.Route

// https://youtu.be/h61Wqy3qcKg?si=OqctoATR5MGbypOW
@Composable
inline fun <reified T : ViewModel, reified R : Route> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
): T {
    destination.route ?: return hiltViewModel<T>()

    val entry = try {
        navController.getBackStackEntry<R>()
    } catch (e: IllegalArgumentException) {
        Log.e("NavBackStackEntryExt", "No back stack entry found for route: ${R::class}", e)
        null
    }

    return if (entry != null) {
        val rememberedEntry = remember(this) { entry }
        hiltViewModel(rememberedEntry)
    } else {
        hiltViewModel()
    }
}
