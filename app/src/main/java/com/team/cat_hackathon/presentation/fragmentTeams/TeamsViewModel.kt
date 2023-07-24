package com.team.cat_hackathon.presentation.fragmentTeams

import androidx.lifecycle.ViewModel
import com.team.cat_hackathon.data.models.AllDataResponse
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.data.repositories.TeamsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(val repository: TeamsRepository) : ViewModel(){

    private suspend fun getTeamById(teamId:Int) = withContext(Dispatchers.IO){
        repository.getTeamById(teamId)
    }

    suspend fun updateCachedUser(user: User) {
    repository.updateCacheUser(user)
    }

    private suspend fun getCachedUser() = withContext(Dispatchers.IO) {
        repository.getCachedUser()
    }

    suspend fun getCurrentUserTeam(): Team? {
      val teamId = getCachedUser().id
      val response = getTeamById(teamId)
      return getTeamFromResponse(response)
    }

    private fun getTeamFromResponse(response: Response<AllDataResponse>): Team? {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return result.teams[0]
            }
        }
        return null
    }

    fun getFakeUsers(number: Int): ArrayList<User>? = repository.getFakeUsers(number)


}