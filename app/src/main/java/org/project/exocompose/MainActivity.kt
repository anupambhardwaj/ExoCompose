package org.project.exocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.media3.common.MediaItem
import org.project.exocompose.ui.theme.ExoComposeTheme
import org.project.library.exocompose.Media
import org.project.library.exocompose.rememberManagedExoPlayer
import org.project.library.exocompose.rememberMediaState

class MainActivity : ComponentActivity() {

    val urls = listOf(
        "https://storage.googleapis.com/downloads.webmproject.org/av1/exoplayer/bbb-av1-480p.mp4",
        "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3",
        "https://storage.googleapis.com/exoplayer-test-media-1/mkv/android-screens-lavf-56.36.100-aac-avc-main-1280x720.mkv",
        "https://storage.googleapis.com/exoplayer-test-media-1/mp4/frame-counter-one-hour.mp4",
        "https://html5demos.com/assets/dizzy.mp4",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExoComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    var url by rememberSaveable { mutableStateOf(urls[0]) }

                    val player by rememberManagedExoPlayer()
                    var setPlayer by rememberSaveable { mutableStateOf(true) }

                    val mediaItem = remember(url) { MediaItem.Builder().setMediaId(url).setUri(url).build() }
                    DisposableEffect(mediaItem, player) {
                        player?.run {
                            setMediaItem(mediaItem)
                            prepare()
                        }
                        onDispose {}
                    }

                    val mediaState = rememberMediaState(player = player.takeIf { setPlayer })

                    Media(
                        state = mediaState,
                        modifier = Modifier
                            .aspectRatio(16f / 9f)
                            .background(Color.Black),
                    ) {
                        SimpleController(
                            mediaState = mediaState,
                        )
                    }
                }
            }
        }
    }
}
