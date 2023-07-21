package com.team.cat_hackathon.data.models

data class LoginResponse(
    var access_token: String,
    var code: Int,
    var user: User
)