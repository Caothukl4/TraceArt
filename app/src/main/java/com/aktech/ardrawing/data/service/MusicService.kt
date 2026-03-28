package com.aktech.ardrawing.data.service

import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.aktech.ardrawing.data.manager.MusicPlayerManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicService : MediaSessionService() {

    @Inject lateinit var musicPlayerManager: MusicPlayerManager

    private lateinit var mediaSession: MediaSession

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSession.Builder(this, musicPlayerManager.getPlayer()).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession.release()
        super.onDestroy()
    }
}
