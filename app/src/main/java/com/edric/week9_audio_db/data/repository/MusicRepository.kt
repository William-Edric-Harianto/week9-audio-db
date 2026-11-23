package com.edric.week9_audio_db.data.repository

import com.edric.week9_audio_db.data.remote.AudioDbApiService
import com.edric.week9_audio_db.model.AlbumDetail
import com.edric.week9_audio_db.model.ArtistDiscography
import com.edric.week9_audio_db.util.safeCall
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MusicRepository(private val api: AudioDbApiService) {

    private val discographyCache = mutableMapOf<String, ArtistDiscography>()
    private val detailCache = mutableMapOf<String, AlbumDetail>()

    suspend fun getDiscography(artistName: String): Result<ArtistDiscography> {
        val trimmed = artistName.trim()
        val key = trimmed.lowercase()
        discographyCache[key]?.let { return Result.success(it) }

        return safeCall {
            val artistDto = api.searchArtist(trimmed)
                .artists
                ?.firstOrNull()
                ?: throw NoSuchElementException("Artis \"$trimmed\" tidak ditemukan.")

            val artist = artistDto.toDomain()
            val albumsByName = api.searchAlbums(artist.name).album.orEmpty()
            val albums = albumsByName.ifEmpty {
                api.getAlbumsByArtist(artist.id).album.orEmpty()
            }

            ArtistDiscography(
                artist = artist,
                albums = albums.map { it.toDomain() },
            ).also { discographyCache[key] = it }
        }
    }

    suspend fun getAlbumDetail(albumId: String): Result<AlbumDetail> {
        detailCache[albumId]?.let { return Result.success(it) }

        return safeCall {
            coroutineScope {
                val albumRequest = async { api.getAlbumDetail(albumId) }
                val tracksRequest = async { api.getAlbumTracks(albumId) }

                val album = albumRequest.await()
                    .album
                    ?.firstOrNull()
                    ?: throw NoSuchElementException("Album tidak ditemukan.")

                album.toDetail(tracksRequest.await().track.orEmpty())
                    .also { detailCache[albumId] = it }
            }
        }
    }
}
