package com.team.cat_hackathon.data.models

import androidx.annotation.Keep

@Keep
data class TeamsResponse(
    val articles: MutableList<Team>,
    val status: String,
    val totalResults: Int
)
