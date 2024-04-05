package org.project.exocompose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.project.library.exocompose.ControllerState
import org.project.library.exocompose.MediaState
import org.project.library.exocompose.TimeBar
import org.project.library.exocompose.rememberControllerState


@Composable
fun SimpleController(
    mediaState: MediaState,
    modifier: Modifier = Modifier,
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp

    Crossfade(targetState = mediaState.isControllerShowing, modifier, label = "Player control animation") { isShowing ->
        if (isShowing) {

            var scrubbing by remember { mutableStateOf(false) }
            val controllerState = rememberControllerState(mediaState)
            val hideWhenTimeout = !mediaState.shouldShowControllerIndefinitely && !scrubbing
            var hideEffectReset by remember { mutableStateOf(0) }
            LaunchedEffect(hideWhenTimeout, hideEffectReset) {
                if (hideWhenTimeout) {
                    // hide after 3s
                    delay(3000)
                    mediaState.isControllerShowing = false
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x98000000))
            , contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(horizontal = (screenWidth/4).dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(
                            if (controllerState.showPause) R.drawable.baseline_pause_24
                            else R.drawable.baseline_play_arrow_24
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(52.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) {
                                hideEffectReset++
                                controllerState.playOrPause()
                            },
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }


                LaunchedEffect(Unit) {
                    while (true) {
                        delay(200)
                        controllerState.triggerPositionUpdate()
                    }
                }
                TimeBar(
                    controllerState.durationMs,
                    controllerState.positionMs,
                    controllerState.bufferedPositionMs,
                    modifier = Modifier
                        .systemGestureExclusion()
                        .fillMaxWidth()
                        .height(28.dp)
                        .padding(end = 28.dp)
                        .align(Alignment.BottomCenter),
                    contentPadding = PaddingValues(12.dp),
                    scrubberCenterAsAnchor = true,
                    onScrubStart = { scrubbing = true },
                    onScrubStop = { positionMs ->
                        scrubbing = false
                        controllerState.seekTo(positionMs)
                    }
                )
            }
        }
    }
}
