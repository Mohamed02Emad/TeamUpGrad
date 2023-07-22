package com.team.cat_hackathon.data.models

data class AuthResponse(
    var access_token: String,
    var code: Int,
    var user: User
)