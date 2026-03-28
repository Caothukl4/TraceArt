package com.aktech.ardrawing.domain.params

import com.google.firebase.firestore.FieldValue


data class LimitParams(val limitOrc: LimitCountParams?=null, var limitTts: LimitCountParams?=null) {
}

data class LimitCountParams(val count: Int? = null, val updateAt: FieldValue? = null)
