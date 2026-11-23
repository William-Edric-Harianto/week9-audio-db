package com.edric.week9_audio_db.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edric.week9_audio_db.model.Album
import com.edric.week9_audio_db.model.Artist
import com.edric.week9_audio_db.model.Track
import com.edric.week9_audio_db.ui.theme.MusicBg
import com.edric.week9_audio_db.ui.theme.MusicDivider
import com.edric.week9_audio_db.ui.theme.MusicGreen
import com.edric.week9_audio_db.ui.theme.MusicSurface
import com.edric.week9_audio_db.ui.theme.MusicTextBody
import com.edric.week9_audio_db.ui.theme.MusicTextMuted
import com.edric.week9_audio_db.ui.theme.MusicTextPrimary
import java.util.Locale

@Composable
fun ArtistBannerCard(
    artist: Artist,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(20.dp)),
    ) {
        RemoteImage(
            url = artist.bannerUrl,
            contentDescription = artist.name,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.30f to Color.Transparent,
                        1f to MusicBg,
                    ),
                ),
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RemoteImage(
                url = artist.thumbUrl,
                contentDescription = artist.name,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White.copy(alpha = 0.25f), CircleShape),
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = artist.name,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MusicTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (artist.genre.isNotEmpty()) {
                        Text(
                            text = artist.genre,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MusicGreen,
                        )
                    }
                    if (artist.genre.isNotEmpty() && artist.country.isNotEmpty()) {
                        Text(
                            text = "  •  ",
                            fontSize = 11.sp,
                            color = MusicTextMuted,
                        )
                    }
                    if (artist.country.isNotEmpty()) {
                        Text(
                            text = artist.country,
                            fontSize = 11.sp,
                            color = MusicTextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandableTextSection(
    title: String,
    text: String,
    collapsedMaxLines: Int,
    emptyFallback: String,
    modifier: Modifier = Modifier,
) {
    val contentText = text.ifBlank { emptyFallback }
    var expanded by rememberSaveable(contentText) { mutableStateOf(false) }
    var hasOverflow by rememberSaveable(contentText) { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MusicTextPrimary,
        )
        Text(
            text = contentText,
            fontSize = 13.sp,
            lineHeight = 20.sp,
            color = MusicTextBody,
            maxLines = if (expanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { layoutResult ->
                if (!expanded) {
                    hasOverflow = layoutResult.hasVisualOverflow
                }
            },
        )
        if (hasOverflow) {
            Text(
                text = if (expanded) "Tutup ringkasan" else "Baca selengkapnya",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MusicGreen,
                modifier = Modifier.clickable { expanded = !expanded },
            )
        }
    }
}

@Composable
fun AlbumCard(
    album: Album,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        RemoteImage(
            url = album.coverUrl,
            contentDescription = album.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp)),
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = album.title,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = MusicTextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = album.year.ifEmpty { "-" },
                fontSize = 11.sp,
                color = MusicGreen,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = album.genre,
                fontSize = 11.sp,
                color = MusicTextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Composable
fun InfoPill(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MusicSurface)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            color = MusicTextBody,
        )
    }
}

@Composable
fun TrackItem(
    track: Track,
    modifier: Modifier = Modifier,
) {
    val trackNumberLabel = if (track.number > 0) {
        String.format(Locale.US, "%02d", track.number)
    } else {
        "--"
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = trackNumberLabel,
                color = MusicGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.width(34.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MusicTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (track.genre.isNotEmpty()) {
                    Text(
                        text = track.genre,
                        fontSize = 11.sp,
                        color = MusicTextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = null,
                tint = MusicTextMuted,
                modifier = Modifier.size(13.dp),
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = track.duration,
                fontSize = 12.sp,
                color = MusicTextMuted,
            )
        }
        HorizontalDivider(color = MusicDivider)
    }
}
