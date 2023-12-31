package com.team.cat_hackathon.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.data.models.TeamWithUsers
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream


fun getDeviceName(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    return if (model.startsWith(manufacturer)) {
        model.capitalize()
    } else {
        "$manufacturer ${model.capitalize()}"
    }
}

fun mapFullTeamToTeam(fullTeam: TeamWithUsers): Team {
    return Team(
        fullTeam.id,
        fullTeam.name,
        fullTeam.description,
        created_at = fullTeam.created_at,
        updated_at = fullTeam.updated_at
    )
}

fun openFacebookIntent(fullUrl: String, context: Context) {
    val username = extractUsernameFromFaceBookUrl(fullUrl)
    val intent = try {
        context.getPackageManager().getPackageInfoCompat(context.packageName, 0)
        val encodedUsername = Uri.encode(username)
        val fbAppUri = "fb://facewebmodal/f?href=https://www.facebook.com/$encodedUsername"
        Intent(Intent.ACTION_VIEW, Uri.parse(fbAppUri))
    } catch (e: Exception) {
        Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl))
    }
    context.startActivity(intent)
}

fun openGithubIntent(fullUrl: String, context: Context) {
    val username = extractUsernameFromGitHubUrl(fullUrl)
    val intent = try {
        context.getPackageManager().getPackageInfoCompat(context.packageName, 0)
        val githubAppUri = "https://github.com/$username"
        Intent(Intent.ACTION_VIEW, Uri.parse(githubAppUri))
    } catch (e: Exception) {
        Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl))
    }
    context.startActivity(intent)
}

fun openLinkedInIntent(fullUrl: String, context: Context) {
    val intent = try {
        context.getPackageManager().getPackageInfoCompat(context.packageName, 0)
        Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl))
    } catch (e: Exception) {
        Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl))
    }
    context.startActivity(intent)
}

private fun extractUsernameFromFaceBookUrl(fullUrl: String): String {
    val pattern = "https://www.facebook.com/(.*)".toRegex()
    val matchResult = pattern.find(fullUrl)
    return matchResult?.groupValues?.getOrNull(1) ?: ""
}

private fun extractUsernameFromGitHubUrl(githubUrl: String): String {
    val pattern = "https://github.com/(.*)".toRegex()
    val matchResult = pattern.find(githubUrl)
    return matchResult?.groupValues?.getOrNull(1) ?: ""
}

private fun PackageManager.getPackageInfoCompat(
    packageName: String,
    flags: Int = 0,
): PackageInfo =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
    } else {
        @Suppress("DEPRECATION") getPackageInfo(packageName, flags)
    }

fun parseErrorMessage(responseBody: String?): String {
    return try {
        val json = JSONObject(responseBody)
        val message = json.getString("message")
        message
    } catch (e: Exception) {
        "General Error" // Return a default message if parsing fails
    }
}