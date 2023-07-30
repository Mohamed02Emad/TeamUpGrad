package com.team.cat_hackathon.data.models

import androidx.annotation.Keep

@Keep
data class AuthResponse(
    var code: Int = -1,
    var message: String? = null,
    var access_token: String,
    var user: User
)