package com.edric.week9_audio_db

import com.edric.week9_audio_db.data.dto.AlbumDto
import com.edric.week9_audio_db.data.dto.AlbumResponseDto
import com.edric.week9_audio_db.data.dto.ArtistDto
import com.edric.week9_audio_db.data.dto.ArtistResponseDto
import com.edric.week9_audio_db.data.dto.TrackDto
import com.edric.week9_audio_db.data.dto.TrackResponseDto
import com.edric.week9_audio_db.data.remote.AudioDbApiService
import com.edric.week9_audio_db.data.repository.MusicRepository
import com.edric.week9_audio_db.data.repository.toDetail
import com.edric.week9_audio_db.data.repository.toDomain
import com.edric.week9_audio_db.util.toUserMessage
import java.io.IOException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

private class FakeAudioDbApiService : AudioDbApiService {
    var artistCalls = 0
    var albumSearchCalls = 0
    var albumByArtistCalls = 0
    var albumDetailCalls = 0
    var trackCalls = 0

    var artistResponse = ArtistResponseDto()
    var searchAlbumsResponse = AlbumResponseDto()
    var albumsByArtistResponse = AlbumResponseDto()
    var albumDetailResponse = AlbumResponseDto()
    var tracksResponse = TrackResponseDto()

    override suspend fun searchArtist(artistName: String): ArtistResponseDto {
        artistCalls++
        return artistResponse
    }

    override suspend fun searchAlbums(artistName: String): AlbumResponseDto {
        albumSearchCalls++
        return searchAlbumsResponse
    }

    override suspend fun getAlbumsByArtist(artistId: String): AlbumResponseDto {
        albumByArtistCalls++
        return albumsByArtistResponse
    }

    override suspend fun getAlbumDetail(albumId: String): AlbumResponseDto {
        albumDetailCalls++
        return albumDetailResponse
    }

    override suspend fun getAlbumTracks(albumId: String): TrackResponseDto {
        trackCalls++
        return tracksResponse
    }
}

class MusicRepositoryAndMapperTest {

    @Test
    fun artistDtoToDomain_usesBannerFallbackAndTrimsFields() {
        val dto = ArtistDto(
            id = "111239",
            name = "Coldplay",
            genre = " Alternative Rock ",
            country = " London, England ",
            biography = " British rock band ",
            thumb = "https://example.com/thumb.jpg",
            banner = "",
            fanart = null,
        )

        val artist = dto.toDomain()

        assertEquals("Alternative Rock", artist.genre)
        assertEquals("London, England", artist.country)
        assertEquals("British rock band", artist.biography)
        assertEquals("https://example.com/thumb.jpg", artist.thumbUrl)
        assertEquals("https://example.com/thumb.jpg", artist.bannerUrl)
    }

    @Test
    fun albumDtoToDetail_formatsDurationAndSortsTracks() {
        val albumDto = AlbumDto(
            id = "2110796",
            title = "A Rush of Blood to the Head",
            artist = "Coldplay",
            year = "2002",
            genre = "Pop-Rock",
            cover = "",
            description = " Second studio album ",
        )
        val tracks = listOf(
            TrackDto(id = "2", number = "2", title = "In My Place", duration = "228000", genre = ""),
            TrackDto(id = "1", number = "1", title = "Politik", duration = "318000", genre = "Alt Rock"),
            TrackDto(id = "3", number = "3", title = "Unknown", duration = null, genre = null),
        )

        val detail = albumDto.toDetail(tracks)

        assertNull(detail.coverUrl)
        assertEquals("Second studio album", detail.description)
        assertEquals(listOf(1, 2, 3), detail.tracks.map { it.number })
        assertEquals("05:18", detail.tracks[0].duration)
        assertEquals("Alt Rock", detail.tracks[0].genre)
        assertEquals("03:48", detail.tracks[1].duration)
        assertEquals("Pop-Rock", detail.tracks[1].genre)
        assertEquals("--:--", detail.tracks[2].duration)
    }

    @Test
    fun getDiscography_fallsBackToArtistIdAndCachesResult() = runBlocking {
        val fakeApi = FakeAudioDbApiService().apply {
            artistResponse = ArtistResponseDto(
                artists = listOf(
                    ArtistDto(id = "1", name = "Coldplay", genre = "Rock"),
                ),
            )
            searchAlbumsResponse = AlbumResponseDto(album = emptyList())
            albumsByArtistResponse = AlbumResponseDto(
                album = listOf(AlbumDto(id = "10", title = "Parachutes", year = "2000")),
            )
        }
        val repo = MusicRepository(fakeApi)

        val first = repo.getDiscography(" Coldplay ")
        val second = repo.getDiscography("coldplay")

        assertTrue(first.isSuccess)
        assertTrue(second.isSuccess)
        assertEquals(1, fakeApi.artistCalls)
        assertEquals(1, fakeApi.albumSearchCalls)
        assertEquals(1, fakeApi.albumByArtistCalls)
        assertEquals("Parachutes", first.getOrThrow().albums.single().title)
    }

    @Test
    fun getDiscography_returnsFailureWhenArtistMissing() = runBlocking {
        val fakeApi = FakeAudioDbApiService()
        val repo = MusicRepository(fakeApi)

        val result = repo.getDiscography("UnknownArtist")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NoSuchElementException)
    }

    @Test
    fun getAlbumDetail_fetchesAndCachesDetail() = runBlocking {
        val fakeApi = FakeAudioDbApiService().apply {
            albumDetailResponse = AlbumResponseDto(
                album = listOf(AlbumDto(id = "99", title = "X&Y", year = "2005")),
            )
            tracksResponse = TrackResponseDto(
                track = listOf(TrackDto(id = "t1", number = "1", title = "Square One", duration = "287000")),
            )
        }
        val repo = MusicRepository(fakeApi)

        val first = repo.getAlbumDetail("99")
        val second = repo.getAlbumDetail("99")

        assertTrue(first.isSuccess)
        assertTrue(second.isSuccess)
        assertEquals(1, fakeApi.albumDetailCalls)
        assertEquals(1, fakeApi.trackCalls)
        assertEquals("04:47", first.getOrThrow().tracks.single().duration)
    }

    @Test
    fun toUserMessage_preservesIoExceptionMessage() {
        val error = IOException("Unable to resolve host \"www.theaudiodb.com\"")
        assertEquals("Unable to resolve host \"www.theaudiodb.com\"", error.toUserMessage())
    }
}
