package com.tuananh.traceart.utils

import java.security.MessageDigest
import com.knuddels.jtokkit.Encodings
import com.knuddels.jtokkit.api.EncodingType

fun String.toSha1(): String {
    val digest = MessageDigest.getInstance("SHA-1")
    val hashBytes = digest.digest(this.toByteArray(Charsets.UTF_8))
    return hashBytes.joinToString("") { "%02x".format(it) }
}

fun String.countTokens(): Int{
    try {
        val registry = Encodings.newDefaultEncodingRegistry()
        val encoding = registry.getEncoding(EncodingType.CL100K_BASE) // GPT-4/3.5
        val tokens = encoding.encode(this)
        return tokens.size()
    } catch (_: Exception){

    }
    return  0
}