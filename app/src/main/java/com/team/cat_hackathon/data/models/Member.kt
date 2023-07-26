package com.team.cat_hackathon.data.models

import com.google.errorprone.annotations.Keep

@Keep
data class Member(
    var user_id: Int,
    var team_id: Int,
    var join: Int,
    var created_at: String,
    var updated_at: String
)