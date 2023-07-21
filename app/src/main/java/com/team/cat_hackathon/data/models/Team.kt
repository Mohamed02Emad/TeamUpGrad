package com.team.cat_hackathon.data.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity
data class Team(
    @PrimaryKey val id : Int,
    var name: String,
    var email: String,
    var track : String?,
    var bio: String?,
    var imageUrl : String?,
    var githubUrl : String?,
    var facebookUrl : String?,
    var linkedInUrl : String?,
    var teamName : String?,
    var isLeader : Boolean
): Parcelable
