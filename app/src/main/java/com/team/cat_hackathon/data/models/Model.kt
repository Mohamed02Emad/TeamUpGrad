package com.team.cat_hackathon.data.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

//todo : rename
@Keep
@Parcelize
@Entity
data class Model(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
): Parcelable
// TODO: this should be the class from Json also should be the class for room entity
