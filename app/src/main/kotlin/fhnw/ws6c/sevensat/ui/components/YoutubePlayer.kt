package fhnw.ws6c.sevensat.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YoutubePlayer(key: String) {
  val activityLifecycle = LocalLifecycleOwner.current.lifecycle
  val context = LocalContext.current
  val youtubePlayer = remember(key) {
    YouTubePlayerView(context).apply {
      activityLifecycle.addObserver(this)
      enableAutomaticInitialization = false
      initialize(object : AbstractYouTubePlayerListener() {
        override fun onReady(youTubePlayer: YouTubePlayer) {
          youTubePlayer.cueVideo(key, 0f)
        }
      })
    }
  }
  AndroidView(
    factory = { youtubePlayer },
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight(),
    update = {}
  )
}