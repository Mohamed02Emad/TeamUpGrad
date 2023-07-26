package com.mo_chatting.chatapp.data.dataStore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.team.cat_hackathon.data.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


private val Context.dataStore by preferencesDataStore("user_data")

class DataStoreImpl(
    appContext: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataStore {

    private val mDataStore by lazy {
        appContext.dataStore
    }

    companion object {
        const val ON_BOARDING = "onBoarding"
        const val IS_LOGGED = "isLogged"

        const val TOKEN = "token"

        const val BIO = "bio"
        const val CREATED_AT = "createdAt"
        const val EMAIL = "email"
        const val EMAIL_VERIFIED_AT = "emailVerifiedAt"
        const val FACEBOOK = "facebook"
        const val GITHUB = "github"
        const val ID = "id"
        const val IMAGE_URL = "imageUrl"
        const val TEAM_ID = "isInTeam"
        const val IS_LEADER = "isLeader"
        const val LINKED_IN_URL = "linkedInUrl"
        const val NAME = "name"
        const val TRACK = "track"
        const val UPDATED_AT = "updatedAt"
    }

    override suspend fun insertUser(user: User) {

        val bio: String = user.bio ?: ""
        val created_at: String = user.created_at ?: ""
        val email: String = user.email
        val email_verified_at: String = user.email_verified_at ?: ""
        val facebookUrl: String = user.facebookUrl ?: ""
        val githubUrl: String = user.githubUrl ?: ""
        val id: Int = user.id
        val imageUrl: String = user.imageUrl ?: ""
        val teamId: Int = user.team_id ?: -1
        val isLeader: Int = user.isLeader
        val linkedinUrl: String = user.linkedinUrl ?: ""
        val name: String = user.name
        val track: String = user.track ?: ""
        val updated_at: String = user.updated_at ?: ""


        mDataStore.edit { settings ->
            settings[stringPreferencesKey(BIO)] = bio
            settings[stringPreferencesKey(CREATED_AT)] = created_at
            settings[stringPreferencesKey(EMAIL)] = email
            settings[stringPreferencesKey(EMAIL_VERIFIED_AT)] = email_verified_at
            settings[stringPreferencesKey(FACEBOOK)] = facebookUrl
            settings[stringPreferencesKey(GITHUB)] = githubUrl
            settings[intPreferencesKey(ID)] = id
            settings[stringPreferencesKey(IMAGE_URL)] = imageUrl
            settings[intPreferencesKey(TEAM_ID)] = teamId
            settings[intPreferencesKey(IS_LEADER)] = isLeader
            settings[stringPreferencesKey(LINKED_IN_URL)] = linkedinUrl
            settings[stringPreferencesKey(NAME)] = name
            settings[stringPreferencesKey(TRACK)] = track
            settings[stringPreferencesKey(UPDATED_AT)] = updated_at
        }
    }

    override suspend fun getUser(): User = withContext(dispatcher) {
        var bio: String = " "
        var created_at: String =  " "
        var email: String = " "
        var email_verified_at: String = " "
        var facebookUrl: String =  " "
        var githubUrl: String = " "
        var id: Int = 0
        var imageUrl: String =" "
        var isInTeam: Int =-1
        var isLeader: Int =-1
        var linkedinUrl: String= " "
        var name: String = " "
        var track: String = " "
        var updated_at: String = " "

        mDataStore.data.map { settings ->
            bio = settings[stringPreferencesKey(BIO)] ?: ""
            created_at = settings[stringPreferencesKey(CREATED_AT)] ?: ""
            email = settings[stringPreferencesKey(EMAIL)] ?: ""
            email_verified_at = settings[stringPreferencesKey(EMAIL_VERIFIED_AT)] ?: ""
            facebookUrl = settings[stringPreferencesKey(FACEBOOK)] ?: ""
            githubUrl = settings[stringPreferencesKey(GITHUB)] ?: ""
            id = settings[intPreferencesKey(ID)] ?: -1
            imageUrl = settings[stringPreferencesKey(IMAGE_URL)] ?: ""
            isInTeam = settings[intPreferencesKey(TEAM_ID)] ?: -1
            isLeader = settings[intPreferencesKey(IS_LEADER)] ?: 0
            linkedinUrl = settings[stringPreferencesKey(LINKED_IN_URL)] ?: ""
            name = settings[stringPreferencesKey(NAME)] ?: ""
            track = settings[stringPreferencesKey(TRACK)] ?: ""
            updated_at = settings[stringPreferencesKey(UPDATED_AT)] ?: ""

        }.first().toString()
        User(
            id , name , email , email_verified_at , track , bio , imageUrl , githubUrl , facebookUrl , linkedinUrl ,
            isLeader , isInTeam , created_at , updated_at
        )
    }

    override suspend fun insertToken(token : String) {
        withContext(dispatcher) {
            mDataStore.edit { settings ->
                settings[stringPreferencesKey(TOKEN)] = token
            }
        }
    }

    override suspend fun getToken(): String  = withContext(dispatcher) {
        mDataStore.data.map { settings ->
            settings[stringPreferencesKey(TOKEN)] ?: ""
        }.first()
    }

    override suspend fun logOut() {
        setIsLoggedIn(false)
        insertUser(User(-1))
    }

    override suspend fun getIsOnBoardingFinished(): Boolean = withContext(dispatcher) {
        mDataStore.data.map { settings ->
            settings[booleanPreferencesKey(ON_BOARDING)] ?: false
        }.first()
    }

    override suspend fun setIsOnBoardingFinished(isOnBoardingFinished: Boolean) {
        withContext(dispatcher) {
            mDataStore.edit { settings ->
                settings[booleanPreferencesKey(ON_BOARDING)] = isOnBoardingFinished
            }
        }
    }

    override suspend fun getIsLoggedIn(): Boolean = withContext(dispatcher) {
        mDataStore.data.map { settings ->
            settings[booleanPreferencesKey(IS_LOGGED)] ?: false
        }.first()
    }

    override suspend fun setIsLoggedIn(isLogged: Boolean) {
        withContext(dispatcher) {
            mDataStore.edit { settings ->
                settings[booleanPreferencesKey(IS_LOGGED)] = isLogged
            }
        }
    }

}