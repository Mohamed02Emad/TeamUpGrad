package com.team.cat_hackathon.data.models

import androidx.annotation.Keep

@Keep
data class TeamResponse(
    var code: Int,
    var team: Team?
)