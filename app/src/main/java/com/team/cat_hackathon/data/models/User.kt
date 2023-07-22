package com.team.cat_hackathon.data.models

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
@Keep
data class User(
    @PrimaryKey var id: Int,
    var name: String,
    var email: String,
    var email_verified_at: String?,
    var track: String?,
    var bio: String?,
    var imageUrl: String?,
    var githubUrl: String?,
    var facebookUrl: String?,
    var linkedinUrl: String?,
    var isLeader: Int,
    var isInTeam: Int,
    var created_at: String?,
    var updated_at: String?
)