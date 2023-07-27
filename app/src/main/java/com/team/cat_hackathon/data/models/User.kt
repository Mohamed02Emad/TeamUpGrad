package com.team.cat_hackathon.data.models

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
@Keep
data class User(
    @PrimaryKey var id: Int,
    var name: String = " ",
    var email: String =" ",
    var email_verified_at: String?= null,
    var track: String? = null,
    var bio: String? = null,
    var imageUrl: String? = null,
    var githubUrl: String? = null,
    var facebookUrl: String? = null,
    var linkedinUrl: String? = null,
    var isLeader: Int = 0,
    var team_id: Int? = null,
    var created_at: String? = null,
    var updated_at: String? = null
): Serializable