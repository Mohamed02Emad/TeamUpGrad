package com.team.cat_hackathon.utils

import android.os.Build

 fun getDeviceName(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    return if (model.startsWith(manufacturer)) {
        model.capitalize()
    } else {
        "$manufacturer ${model.capitalize()}"
    }
}