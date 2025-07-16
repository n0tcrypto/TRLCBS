package com.android.translateappcouchbase.data.model

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class WordEntity(
    @PrimaryKey val word: String,
    val definition: String,
    val note: String? = null
): Parcelable
