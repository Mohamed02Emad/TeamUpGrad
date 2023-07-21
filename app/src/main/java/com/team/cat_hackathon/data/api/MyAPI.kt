

import com.team.cat_hackathon.data.models.TeamsResponse
import com.team.cat_hackathon.data.models.UsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MyAPI {

    //Todo: change methods to fit our api later

    @GET(getUser)
    suspend fun getUser(
        @Query("email")
        email: String? = null,
        @Query("password")
        password: String? = null
    ): Response<TeamsResponse>

    @GET(getEverything)
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("pageSize")
        pageSize: Int = 20
    ): Response<TeamsResponse>


    //todo : add this from backend
    @GET("")
    abstract fun getTeamsByQuery(
        @Query("")
        query: String?
    ): Response<TeamsResponse>

    @GET("")
    abstract fun getIndividualsByQuery(
        @Query("")
        query: String?
    ): Response<UsersResponse>
}