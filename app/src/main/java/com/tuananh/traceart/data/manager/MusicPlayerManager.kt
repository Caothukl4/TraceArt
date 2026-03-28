package com.tuananh.traceart.data.manager

import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicPlayerManager @Inject constructor(
    private val player: ExoPlayer,
    private val cacheDataSourceFactory: DataSource.Factory

) {
    //    private val player = ExoPlayer.Builder(context).build()
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration

    private val _playIndex = MutableStateFlow(0)
    val playIndex: StateFlow<Int> = _playIndex

    // Flag để phân biệt chế độ SingleUrl
    private var isSingleUrlMode = false
    private var lastSnapshot: PlayerSnapshot? = null

    init {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (!isSingleUrlMode) {
                    _isPlaying.value = isPlaying
                }
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (!isSingleUrlMode) {
                    _duration.value = player.duration.coerceAtLeast(0L)
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                if (!isSingleUrlMode) {
                    _playIndex.value = player.currentMediaItemIndex
                    _duration.value = player.duration.coerceAtLeast(0L)
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                if (!isSingleUrlMode) {
                    val index = player.currentMediaItemIndex
                    player.prepare()
                    Log.e("MusicPlayerManager", "Bài thứ ${index + 1} bị lỗi: ${error.message}")
                }
            }
        })
    }

    @OptIn(UnstableApi::class)
    private fun buildMediaSource(mediaItem: MediaItem): MediaSource {
        return ProgressiveMediaSource.Factory(cacheDataSourceFactory)
            .createMediaSource(mediaItem)
    }

    @OptIn(UnstableApi::class)
    fun updatePlaylistKeepCurrent(newMediaItems: List<MediaItem>) {
        val currentMediaId = player.currentMediaItem?.mediaId
        val currentPosition = player.currentPosition

        val existingUris = (0 until player.mediaItemCount).map {
            player.getMediaItemAt(it).localConfiguration?.uri?.toString()
        }
        val newUris = newMediaItems.map { it.localConfiguration?.uri?.toString() }

        if (existingUris != newUris) {
//            player.clearMediaItems()
            player.setMediaSources(newMediaItems.map { buildMediaSource(it) })

            // Tìm vị trí bài hiện tại (nếu còn trong danh sách mới)
            val newIndex = newMediaItems.indexOfFirst {
                it.mediaId == currentMediaId
            }

            if (newIndex != -1) {
                player.seekTo(newIndex, currentPosition)
            } else {
                player.seekTo(0, 0) // hoặc bắt đầu từ đầu nếu bài cũ bị xoá
            }
            player.pause()
            player.prepare()
        }

    }

    fun playPause() {
        if (isPlaying.value) player.pause() else {
            if (player.playbackState == Player.STATE_IDLE && player.mediaItemCount > 0) {
                player.prepare()
            }
            if (player.playbackState == Player.STATE_ENDED &&
                player.currentMediaItemIndex == player.mediaItemCount - 1
            ) {
                player.seekTo(0)
            }
            player.play()
        }
    }

    fun pause() = player.pause()

    fun skipNext() = player.seekToNextMediaItem()
    fun skipPrevious() {
        val currentPosition = player.currentPosition
        val threshold = 3000L // 3 giây

        if (currentPosition > threshold) {
            // Quay về đầu bài hiện tại
            player.seekTo(0)
        } else {
            // Quay về bài trước
            player.seekToPreviousMediaItem()
        }
    }

    fun forward10s() = player.seekTo(player.currentPosition + 10_000)
    fun backward10s() = player.seekTo((player.currentPosition - 10_000).coerceAtLeast(0))
    fun getPlayer(): ExoPlayer = player
    fun seekTo(position: Long) = player.seekTo(position)
    fun setMediaIndex(index: Int) {
        player.seekTo(index, 0)
        _playIndex.value = index
    }

    fun stop() {
        player.stop()
        _isPlaying.value = false
        if (isSingleUrlMode) {
            isSingleUrlMode = false
            singleUrlListener?.let { player.removeListener(it) }
            restoreSnapshot()
        }
    }

    fun clearMediaItems() {
        player.clearMediaItems()
    }

    private var singleUrlListener: Player.Listener? = null


    @OptIn(UnstableApi::class)
    fun playSingleUrl(url: String, onComplete: (() -> Unit)? = {}, onReady: (() -> Unit)? = {}) {
        if (!isSingleUrlMode) {
            lastSnapshot = PlayerSnapshot(
                mediaItems = (0 until player.mediaItemCount).map { player.getMediaItemAt(it) },
                currentIndex = player.currentMediaItemIndex,
                currentPosition = player.currentPosition,
                wasPlaying = player.isPlaying
            )
        }
        isSingleUrlMode = true
        player.stop()
        singleUrlListener?.let { player.removeListener(it) }

        singleUrlListener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    restoreSnapshot()
                    onComplete?.invoke()
                    player.removeListener(this)
                    singleUrlListener = null
                    isSingleUrlMode = false
                } else if (state == Player.STATE_READY) {
                    onReady?.invoke()
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                restoreSnapshot()
                onComplete?.invoke()
                player.removeListener(this)
                singleUrlListener = null
                isSingleUrlMode = false
            }


        }
        player.addListener(singleUrlListener!!)
//        player.clearMediaItems()
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaSource(buildMediaSource(mediaItem))
        player.prepare()
        player.play()
    }

    private fun restoreSnapshot() {
        lastSnapshot?.let { snapshot ->
            isSingleUrlMode = false

            player.setMediaItems(
                snapshot.mediaItems,
                snapshot.currentIndex,
                snapshot.currentPosition
            )
            player.prepare()

            if (snapshot.wasPlaying) {
                player.play()
            } else {
                player.pause()
            }

            lastSnapshot = null
        }
    }
}

private data class PlayerSnapshot(
    val mediaItems: List<MediaItem>,
    val currentIndex: Int,
    val currentPosition: Long,
    val wasPlaying: Boolean
)