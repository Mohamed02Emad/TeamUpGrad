package com.team.cat_hackathon.data.models

import androidx.annotation.Keep

@Keep

data class AuthResponse(
    var access_token: String,
    var code: Int = -1,
    var user: User
)