package com.aktech.ardrawing.domain.model

data class LimitData (val limitOrc: LimitCount?, var limitTts: LimitCount?){
}

data class LimitCount(val count: Int? = null, val updateAt: Long? = null)
