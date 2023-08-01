package com.team.cat_hackathon.data.models

import androidx.annotation.Keep

@Keep
data class AllDataResponse(
    var users: List<User>,
    var teams: List<Team>,
    var count: Map<String , Int>
    )
