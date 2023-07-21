package com.team.cat_hackathon.data.models

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
@Keep
data class User(
    var bio: String?,
    var created_at: String?,
    var email: String,
    var email_verified_at: String?,
    var facebookUrl: String?,
    var githubUrl: String?,
   @PrimaryKey var id: Int,
    var imageUrl: String?,
    var isInTeam: Int,
    var isLeader: Int,
    var linkedinUrl: String?,
    var name: String,
    var track: String?,
    var updated_at: String?
)