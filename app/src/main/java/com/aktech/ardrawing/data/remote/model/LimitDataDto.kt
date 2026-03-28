package com.aktech.ardrawing.data.remote.model

import com.aktech.ardrawing.domain.model.LimitCount
import com.aktech.ardrawing.domain.model.LimitData
import com.google.firebase.Timestamp

data class LimitDataDto(val limitOrc: LimitCountDto? = null, var limitTts: LimitCountDto? = null) {
    fun toLimitData(): LimitData{
        return LimitData(limitOrc=limitOrc?.toLimitCount(), limitTts=limitTts?.toLimitCount())
    }
}
data class LimitCountDto(val count: Int? = null, val updateAt: Timestamp? = null){
    fun toLimitCount(): LimitCount{
        return LimitCount(count=count, updateAt=updateAt?.toDate()?.time)
    }
}

