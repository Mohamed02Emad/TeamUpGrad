package com.team.cat_hackathon.data.models

import androidx.annotation.Keep
import java.lang.reflect.Member

@Keep
data class TeamWithUsers(
    var id: Int,
    var name: String,
    var description: String,
    var created_at: String,
    var updated_at: String,
    var members: List<User>
)