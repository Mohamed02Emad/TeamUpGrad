package com.team.cat_hackathon.data.models

import androidx.annotation.Keep

@Keep

data class MessageResponse(
    var code: Int = -1,
    var message: String
)