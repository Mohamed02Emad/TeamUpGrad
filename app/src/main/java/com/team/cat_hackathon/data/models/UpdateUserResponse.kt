package com.team.cat_hackathon.data.models

import com.google.errorprone.annotations.Keep

@Keep
data class UpdateUserResponse(
    val coede: Int?,
    val message: String?,
    val user: User?
)
