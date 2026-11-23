package com.edric.week9_audio_db.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edric.week9_audio_db.ui.theme.MusicBg
import com.edric.week9_audio_db.ui.theme.MusicTextMuted
import com.edric.week9_audio_db.ui.theme.MusicTextPrimary
import com.edric.week9_audio_db.ui.widgets.AlbumCard
import com.edric.week9_audio_db.ui.widgets.ArtistBannerCard
import com.edric.week9_audio_db.ui.widgets.ErrorBox
import com.edric.week9_audio_db.ui.widgets.ExpandableTextSection
import com.edric.week9_audio_db.ui.widgets.LoadingBox
import com.edric.week9_audio_db.ui.widgets.MusicHeader
import com.edric.week9_audio_db.ui.widgets.SearchBar
import com.edric.week9_audio_db.ui.widgets.SectionHeader
import com.edric.week9_audio_db.viewmodel.ArtistViewModel

@Composable
fun ArtistScreen(
    onAlbumClick: (String) -> Unit,
    viewModel: ArtistViewModel = viewModel(factory = ArtistViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val discography = uiState.discography

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MusicBg,
        contentColor = MusicTextPrimary,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                MusicHeader()
                SearchBar(
                    query = uiState.query,
                    onQueryChange = viewModel::onQueryChange,
                    onSearch = {
                        focusManager.clearFocus()
                        viewModel.onSearch()
                    },
                )
            }

            when {
                uiState.isLoading -> LoadingBox(
                    message = "Memuat data artis...",
                    modifier = Modifier.weight(1f),
                )

                uiState.error != null -> ErrorBox(
                    message = uiState.error.orEmpty(),
                    onRetry = viewModel::retry,
                    modifier = Modifier.weight(1f),
                )

                discography != null -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 2.dp,
                        bottom = 24.dp + navBarPadding,
                    ),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item(key = "banner", span = { GridItemSpan(maxLineSpan) }) {
                        ArtistBannerCard(artist = discography.artist)
                    }

                    item(key = "bio", span = { GridItemSpan(maxLineSpan) }) {
                        ExpandableTextSection(
                            title = "Tentang Artis",
                            text = discography.artist.biography,
                            collapsedMaxLines = 4,
                            emptyFallback = "Biografi tidak tersedia.",
                        )
                    }

                    item(key = "section", span = { GridItemSpan(maxLineSpan) }) {
                        Column {
                            Spacer(Modifier.height(6.dp))
                            SectionHeader(
                                title = "Diskografi Album",
                                badge = "${discography.albums.size} Album",
                                subtitle = "Koleksi rilisan resmi",
                            )
                        }
                    }

                    if (discography.albums.isEmpty()) {
                        item(key = "empty_albums", span = { GridItemSpan(maxLineSpan) }) {
                            Text(
                                text = "Belum ada album untuk artis ini.",
                                color = MusicTextMuted,
                                fontSize = 13.sp,
                            )
                        }
                    } else {
                        itemsIndexed(
                            items = discography.albums,
                            key = { index, album -> "${album.id}_$index" },
                        ) { _, album ->
                            AlbumCard(
                                album = album,
                                onClick = { onAlbumClick(album.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}
