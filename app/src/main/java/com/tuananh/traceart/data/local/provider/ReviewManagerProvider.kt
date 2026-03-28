package com.tuananh.traceart.data.local.provider

import android.app.Activity
import android.content.Context
import com.tuananh.traceart.BuildConfig
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewManagerProvider @Inject constructor(@ApplicationContext context: Context) {
    val reviewManager: ReviewManager = if (BuildConfig.DEBUG) {
        // ⚡ Fake manager cho debug
        FakeReviewManager(context)
    } else {
        ReviewManagerFactory.create(context)
    }

    suspend fun showReview(activity: Activity): Boolean {
        return try {
            val reviewInfo = reviewManager.requestReview()          // suspend, không block UI
            reviewManager.launchReview(activity, reviewInfo)        // suspend
            true // popup có thể được hiển thị
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}