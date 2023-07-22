package com.team.cat_hackathon.data.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Keep
data class Team(
    @PrimaryKey val id : Int,
    var name: String,
    var bio: String?,
    var numOfMembers: Int?,
    var listOfMembers : List<User>
):Serializable
