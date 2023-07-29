package com.team.cat_hackathon.data.models

import androidx.annotation.Keep

@Keep

data class TeamWithUsersResponse(
    var code: Int,
    var team: TeamWithUsers
)