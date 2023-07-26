package com.team.cat_hackathon.data.models

import java.lang.reflect.Member

data class TeamWithUsers(
    var id: Int,
    var name: String,
    var description: String,
    var created_at: String,
    var updated_at: String,
    var members: List<User>
)