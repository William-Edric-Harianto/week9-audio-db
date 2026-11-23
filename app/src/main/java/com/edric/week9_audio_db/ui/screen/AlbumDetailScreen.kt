package com.edric.week9_audio_db.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edric.week9_audio_db.ui.theme.MusicBg
import com.edric.week9_audio_db.ui.theme.MusicGreen
import com.edric.week9_audio_db.ui.theme.MusicTextMuted
import com.edric.week9_audio_db.ui.theme.MusicTextPrimary
import com.edric.week9_audio_db.ui.widgets.ErrorBox
import com.edric.week9_audio_db.ui.widgets.ExpandableTextSection
import com.edric.week9_audio_db.ui.widgets.InfoPill
import com.edric.week9_audio_db.ui.widgets.LoadingBox
import com.edric.week9_audio_db.ui.widgets.RemoteImage
import com.edric.week9_audio_db.ui.widgets.SectionHeader
import com.edric.week9_audio_db.ui.widgets.TrackItem
import com.edric.week9_audio_db.viewmodel.AlbumDetailUiState
import com.edric.week9_audio_db.viewmodel.AlbumDetailViewModel

@Composable
fun AlbumDetailScreen(
    onBackClick: () -> Unit,
    viewModel: AlbumDetailViewModel = viewModel(factory = AlbumDetailViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MusicTextPrimary,
                    )
                }
                Text(
                    text = "Detail Album",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MusicTextPrimary,
                )
            }

            when (val state = uiState) {
                AlbumDetailUiState.Loading -> LoadingBox(
                    message = "Memuat detail album & track list...",
                    modifier = Modifier.weight(1f),
                )

                is AlbumDetailUiState.Error -> ErrorBox(
                    message = state.message,
                    onRetry = viewModel::load,
                    modifier = Modifier.weight(1f),
                )

                is AlbumDetailUiState.Success -> {
                    val album = state.album
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 24.dp + navBarPadding,
                        ),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        item(key = "album_header") {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                RemoteImage(
                                    url = album.coverUrl,
                                    contentDescription = album.title,
                                    modifier = Modifier
                                        .size(240.dp)
                                        .aspectRatio(1f)
                                        .shadow(
                                            elevation = 24.dp,
                                            shape = RoundedCornerShape(20.dp),
                                            ambientColor = MusicGreen,
                                            spotColor = MusicGreen,
                                        )
                                        .clip(RoundedCornerShape(20.dp)),
                                )
                                Spacer(Modifier.height(18.dp))
                                Text(
                                    text = album.title,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MusicTextPrimary,
                                    textAlign = TextAlign.Center,
                                )
                                if (album.artist.isNotEmpty()) {
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = album.artist,
                                        color = MusicGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                    )
                                }
                                Spacer(Modifier.height(10.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    InfoPill("Tahun ${album.year.ifEmpty { "-" }}")
                                    InfoPill(album.genre.ifEmpty { "-" })
                                }
                            }
                        }

                        item(key = "album_description") {
                            ExpandableTextSection(
                                title = "Deskripsi Album",
                                text = album.description,
                                collapsedMaxLines = 3,
                                emptyFallback = "Deskripsi tidak tersedia.",
                            )
                        }

                        item(key = "track_section") {
                            SectionHeader(
                                title = "Daftar Lagu",
                                badge = "${album.tracks.size} Lagu",
                                icon = Icons.Filled.MusicNote,
                                modifier = Modifier.padding(top = 4.dp),
                            )
                        }

                        if (album.tracks.isEmpty()) {
                            item(key = "empty_tracks") {
                                Text(
                                    text = "Belum ada daftar lagu untuk album ini.",
                                    color = MusicTextMuted,
                                    fontSize = 13.sp,
                                )
                            }
                        } else {
                            itemsIndexed(
                                items = album.tracks,
                                key = { index, track -> "${track.id}_$index" },
                            ) { _, track ->
                                TrackItem(track = track)
                            }
                        }
                    }
                }
            }
        }
    }
}
