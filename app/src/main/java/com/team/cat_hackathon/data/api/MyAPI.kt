

import com.team.cat_hackathon.data.api.LOGIN_ENDPOINT
import com.team.cat_hackathon.data.models.LoginResponse
import com.team.cat_hackathon.data.models.TeamsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MyAPI {

    @POST(LOGIN_ENDPOINT)
    suspend fun loginUser(
        @Query("email")
        email: String? = null,
        @Query("password")
        password: String? = null,
        @Query("device_name")
        deviceName: String? = null
    ): Response<LoginResponse>

    @GET("")
    abstract fun getTeams(
        @Query("")
        query: String?
    ): Response<TeamsResponse>

}