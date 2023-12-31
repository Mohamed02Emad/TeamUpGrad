package com.team.cat_hackathon.data.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Keep
data class Team(
    @PrimaryKey val  id: Int,
    var name: String = "",
    var description: String ="",
    var Num_of_Members: Int = 0,
    var created_at: String? ="",
    var updated_at: String? ="",
    var members: List<User> = emptyList()
    ):Serializable
