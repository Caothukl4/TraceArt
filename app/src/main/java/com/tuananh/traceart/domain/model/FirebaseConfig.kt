package com.tuananh.traceart.domain.model

import com.tuananh.traceart.di.RemoteKey

// phải khai báo var mới lấy đúng giá trị
data class FirebaseConfig(
    @RemoteKey("emailAdmins")
    val emailAdmins: List<String> = emptyList(),

    val limitOcr: Int = 0,
    val limitTts: Int = 0,
    val rewardAmountOcr: Int = 0,
    val rewardAmountTts: Int = 0,
    val androidVersionCode: Long = 0,
    val minIntervalInterSec: Long = 0,
    val nativeExpirationSec: Long = 0,
)