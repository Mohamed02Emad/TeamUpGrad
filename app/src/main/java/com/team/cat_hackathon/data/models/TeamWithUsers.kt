package com.team.cat_hackathon.data.models

import java.lang.reflect.Member

data class TeamWithUsers(
    var created_at: String,
    var description: String,
    var id: Int,
    var members: List<User>,
    var name: String,
    var updated_at: String
)