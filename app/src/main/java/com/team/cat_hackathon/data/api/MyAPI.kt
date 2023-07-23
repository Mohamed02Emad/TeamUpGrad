

import com.team.cat_hackathon.data.api.GET_ALL_DATA_ENDPOINT
import com.team.cat_hackathon.data.api.LOGIN_ENDPOINT
import com.team.cat_hackathon.data.api.REGISTER_ENDPOINT
import com.team.cat_hackathon.data.models.AuthResponse
import com.team.cat_hackathon.data.models.AllDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
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
    ): Response<AuthResponse>
    @POST(REGISTER_ENDPOINT)
    suspend fun registerUser(
        @Query("name")
        name: String? = null,
        @Query("email")
        email: String? = null,
        @Query("password")
        password: String? = null
    ): Response<AuthResponse>


    //todo : not working
    @GET(GET_ALL_DATA_ENDPOINT)
    suspend fun getAllData(@Header("Authorization") Authorization : String) : Response<AllDataResponse>


}