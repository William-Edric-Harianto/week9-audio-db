package com.edric.week9_audio_db.data.remote

import com.edric.week9_audio_db.data.dto.AlbumResponseDto
import com.edric.week9_audio_db.data.dto.ArtistResponseDto
import com.edric.week9_audio_db.data.dto.TrackResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AudioDbApiService {
    @GET("search.php")
    suspend fun searchArtist(@Query("s") artistName: String): ArtistResponseDto

    @GET("searchalbum.php")
    suspend fun searchAlbums(@Query("s") artistName: String): AlbumResponseDto

    @GET("album.php")
    suspend fun getAlbumsByArtist(@Query("i") artistId: String): AlbumResponseDto

    @GET("album.php")
    suspend fun getAlbumDetail(@Query("m") albumId: String): AlbumResponseDto

    @GET("track.php")
    suspend fun getAlbumTracks(@Query("m") albumId: String): TrackResponseDto
}
