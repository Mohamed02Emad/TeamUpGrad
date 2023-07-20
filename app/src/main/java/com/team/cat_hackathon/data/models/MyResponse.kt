package com.team.cat_hackathon.data.models

import androidx.annotation.Keep

@Keep
data class MyResponse(
    val articles: MutableList<Model>,
    val status: String,
    val totalResults: Int
)
