package com.team.cat_hackathon.data.models

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

//todo : rename
@Keep
@Entity
data class Model(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)
