package com.edric.week9_audio_db.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.edric.week9_audio_db.ui.theme.MusicBg
import com.edric.week9_audio_db.ui.theme.MusicBorder
import com.edric.week9_audio_db.ui.theme.MusicGreen
import com.edric.week9_audio_db.ui.theme.MusicGreenBadgeBg
import com.edric.week9_audio_db.ui.theme.MusicRed
import com.edric.week9_audio_db.ui.theme.MusicSurface
import com.edric.week9_audio_db.ui.theme.MusicTextMuted
import com.edric.week9_audio_db.ui.theme.MusicTextPrimary
import com.edric.week9_audio_db.ui.theme.MusicTextSubtle

@Composable
fun RemoteImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    if (url.isNullOrBlank()) {
        Box(
            modifier = modifier.background(MusicSurface),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.MusicNote,
                contentDescription = contentDescription,
                tint = MusicTextSubtle,
                modifier = Modifier.size(28.dp),
            )
        }
    } else {
        AsyncImage(
            model = url,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier.background(MusicSurface),
        )
    }
}

@Composable
fun MusicHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MusicGreen),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.GraphicEq,
                contentDescription = null,
                tint = MusicBg,
                modifier = Modifier.size(22.dp),
            )
        }
        Spacer(Modifier.width(10.dp))
        Text(
            text = "Music Artist",
            color = MusicTextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = TextStyle(color = MusicTextPrimary, fontSize = 15.sp),
        placeholder = {
            Text(text = "Cari artis...", color = MusicTextSubtle, fontSize = 15.sp)
        },
        leadingIcon = {
            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = MusicGreen,
                )
            }
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear",
                        tint = MusicTextMuted,
                    )
                }
            }
        },
        shape = RoundedCornerShape(28.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MusicTextPrimary,
            unfocusedTextColor = MusicTextPrimary,
            focusedContainerColor = MusicSurface,
            unfocusedContainerColor = MusicSurface,
            focusedBorderColor = MusicGreen,
            unfocusedBorderColor = MusicBorder,
            cursorColor = MusicGreen,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
    )
}

@Composable
fun CountBadge(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MusicGreenBadgeBg)
            .padding(horizontal = 10.dp, vertical = 5.dp),
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MusicGreen,
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    badge: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: ImageVector? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MusicGreen,
                    modifier = Modifier.size(20.dp),
                )
            }
            Column {
                Text(
                    text = title,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = MusicTextPrimary,
                )
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = MusicTextMuted,
                    )
                }
            }
        }
        CountBadge(badge)
    }
}

@Composable
fun LoadingBox(
    message: String = "",
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(color = MusicGreen)
        if (message.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = message,
                fontSize = 13.sp,
                color = MusicTextMuted,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun ErrorBox(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            tint = MusicRed,
            modifier = Modifier.size(54.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Terjadi Kendala",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MusicTextPrimary,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = message,
            fontSize = 13.sp,
            color = MusicTextMuted,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MusicGreen,
                contentColor = MusicBg,
            ),
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "Coba Lagi",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
