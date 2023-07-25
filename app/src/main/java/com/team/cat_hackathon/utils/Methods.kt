package com.team.cat_hackathon.utils

import android.os.Build
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.data.models.TeamWithUsers
import com.team.cat_hackathon.data.models.TeamWithUsersResponse

fun getDeviceName(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    return if (model.startsWith(manufacturer)) {
        model.capitalize()
    } else {
        "$manufacturer ${model.capitalize()}"
    }
}

fun mapFullTeamToTeam(fullTeam: TeamWithUsers): Team{
    return Team(
        fullTeam.id,
        fullTeam.name,
        fullTeam.description,
        fullTeam.created_at,
        fullTeam.updated_at
    )
}