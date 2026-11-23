package com.edric.week9_audio_db.data.repository

import com.edric.week9_audio_db.data.dto.AlbumDto
import com.edric.week9_audio_db.data.dto.ArtistDto
import com.edric.week9_audio_db.data.dto.TrackDto
import com.edric.week9_audio_db.model.Album
import com.edric.week9_audio_db.model.AlbumDetail
import com.edric.week9_audio_db.model.Artist
import com.edric.week9_audio_db.model.Track
import java.util.Locale

private fun String?.orNull(): String? = this?.takeIf { it.isNotBlank() }

private fun String?.toFormattedDuration(): String {
    val millis = this?.toLongOrNull() ?: return "--:--"
    val totalSeconds = millis / 1000
    if (totalSeconds <= 0) return "--:--"
    return String.format(Locale.US, "%02d:%02d", totalSeconds / 60, totalSeconds % 60)
}

fun ArtistDto.toDomain() = Artist(
    id = id,
    name = name,
    genre = genre.orEmpty().trim(),
    country = country.orEmpty().trim(),
    biography = biography.orEmpty().trim(),
    thumbUrl = thumb.orNull(),
    bannerUrl = fanart.orNull() ?: banner.orNull() ?: thumb.orNull(),
)

fun AlbumDto.toDomain() = Album(
    id = id,
    title = title,
    artist = artist.orEmpty().trim(),
    year = year.orEmpty().trim(),
    genre = genre.orEmpty().trim(),
    coverUrl = cover.orNull(),
)

fun TrackDto.toDomain(fallbackGenre: String) = Track(
    id = id,
    number = number?.toIntOrNull() ?: 0,
    title = title,
    duration = duration.toFormattedDuration(),
    genre = genre.orNull() ?: fallbackGenre,
)

fun AlbumDto.toDetail(tracks: List<TrackDto>): AlbumDetail {
    val albumGenre = genre.orEmpty().trim()
    return AlbumDetail(
        id = id,
        title = title,
        artist = artist.orEmpty().trim(),
        year = year.orEmpty().trim(),
        genre = albumGenre,
        coverUrl = cover.orNull(),
        description = description.orEmpty().trim(),
        tracks = tracks
            .map { it.toDomain(albumGenre) }
            .sortedBy { it.number },
    )
}
