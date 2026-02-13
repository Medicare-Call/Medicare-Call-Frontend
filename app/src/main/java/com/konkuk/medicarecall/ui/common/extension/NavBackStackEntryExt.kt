package com.konkuk.medicarecall.ui.common.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.konkuk.medicarecall.ui.navigation.Route
import org.koin.androidx.compose.koinViewModel

// https://youtu.be/h61Wqy3qcKg?si=OqctoATR5MGbypOW
@Composable
inline fun <reified T : ViewModel, reified R : Route> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
): T {
    destination.route ?: return koinViewModel<T>()

    val parentEntry = remember(this) {
        try {
            navController.getBackStackEntry<R>()
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    return if (parentEntry != null) {
        koinViewModel(viewModelStoreOwner = parentEntry)
    } else {
        koinViewModel()
    }
}
