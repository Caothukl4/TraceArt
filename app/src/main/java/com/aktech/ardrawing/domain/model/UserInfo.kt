package com.aktech.ardrawing.domain.model

data class UserInfo(
    val id: String,
    val name: String? = null,
    val email: String? = null,
    val photo: String? = null,
    val createAt: Long? = null,
    val updateAt: Long? = null,
    val status: Int? = null,
)