package com.team.cat_hackathon.data.models

import androidx.annotation.Keep

@Keep
data class JoinRequestsResponse(
    var code: Int = -1,
    var members: List<User>
)