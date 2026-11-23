package com.edric.week9_audio_db.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.edric.week9_audio_db.ui.screen.AlbumDetailScreen
import com.edric.week9_audio_db.ui.screen.ArtistScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ArtistRoute) {
        composable<ArtistRoute> {
            ArtistScreen(
                onAlbumClick = { albumId -> navController.navigate(AlbumDetailRoute(albumId)) },
            )
        }
        composable<AlbumDetailRoute> {
            AlbumDetailScreen(
                onBackClick = { navController.navigateUp() },
            )
        }
    }
}
