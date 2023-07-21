

import com.team.cat_hackathon.data.api.LOGIN_ENDPOINT
import com.team.cat_hackathon.data.models.LoginResponse
import com.team.cat_hackathon.data.models.MyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
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
    suspend fun getUser(
        @Query("email")
        email: String? = null,
        @Query("password")
        password: String? = null
    ): Response<MyResponse>

    @GET("")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("pageSize")
        pageSize: Int = 20
    ): Response<MyResponse>
}