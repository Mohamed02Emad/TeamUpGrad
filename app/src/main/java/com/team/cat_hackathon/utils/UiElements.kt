package com.team.cat_hackathon.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.team.cat_hackathon.R

fun showSnackbar(message: String, context: Context, view: View) {

    val snackbar = Snackbar.make(
        view,
        message,
        Snackbar.LENGTH_SHORT
    )
    snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.primary_blue))
    snackbar.setTextColor(ContextCompat.getColor(context, R.color.primary_blue))
    snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.white))
    snackbar.show()
}

fun showToast(message: String , context: Context){
    Toast.makeText(context, message , Toast.LENGTH_SHORT).show()
}