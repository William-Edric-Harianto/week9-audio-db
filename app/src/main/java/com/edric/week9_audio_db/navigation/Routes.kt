package com.edric.week9_audio_db.navigation

import kotlinx.serialization.Serializable

@Serializable
data object ArtistRoute

@Serializable
data class AlbumDetailRoute(val albumId: String)
