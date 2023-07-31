


import com.team.cat_hackathon.data.api.ApiVars.ACCEPT_MEMBER
import com.team.cat_hackathon.data.api.ApiVars.CREATE_TEAM
import com.team.cat_hackathon.data.api.ApiVars.DELETE_MEMBER
import com.team.cat_hackathon.data.api.ApiVars.DELETE_TEAM
import com.team.cat_hackathon.data.api.ApiVars.GET_ALL_DATA_ENDPOINT
import com.team.cat_hackathon.data.api.ApiVars.GET_JOIN_TEAM_REQUESTS
import com.team.cat_hackathon.data.api.ApiVars.GET_TEAM_DETAILS
import com.team.cat_hackathon.data.api.ApiVars.JOIN_TEAM
import com.team.cat_hackathon.data.api.ApiVars.LEAVE_TEAM
import com.team.cat_hackathon.data.api.ApiVars.LOGIN_ENDPOINT
import com.team.cat_hackathon.data.api.ApiVars.LOG_OUT_ENDPOINT
import com.team.cat_hackathon.data.api.ApiVars.REGISTER_ENDPOINT
import com.team.cat_hackathon.data.api.ApiVars.REJECT_MEMBER
import com.team.cat_hackathon.data.api.ApiVars.UPDATE_PROFILE
import com.team.cat_hackathon.data.api.ApiVars.UPDATE_TEAM
import com.team.cat_hackathon.data.models.AllDataResponse
import com.team.cat_hackathon.data.models.AuthResponse
import com.team.cat_hackathon.data.models.JoinRequestsResponse
import com.team.cat_hackathon.data.models.MessageResponse
import com.team.cat_hackathon.data.models.TeamWithUsersResponse
import com.team.cat_hackathon.data.models.UpdateUserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @POST(LOG_OUT_ENDPOINT)
    suspend fun logOut(
        @Header("Authorization")
        token: String
    ): Response<AuthResponse>

    @POST(JOIN_TEAM)
    suspend fun requestToJoinTeam(
        @Header("Authorization")
        token: String,
        @Query("team_id")
        teamId: Int
    ): Response<MessageResponse>

    @POST(DELETE_TEAM)
    suspend fun deleteTeam(
        @Header("Authorization")
        token: String,
        @Query("team_id")
        teamId: Int
    ): Response<MessageResponse>

    @POST(UPDATE_TEAM)
    suspend fun updateTeam(
        @Header("Authorization")
        token: String,
        @Query("team_id")
        teamId: Int,
        @Query("name")
        name: String,
        @Query("description")
        description: String,
    ): Response<MessageResponse>

    @POST(CREATE_TEAM)
    suspend fun createTeam(
        @Header("Authorization")
        token: String,
        @Query("name")
        name: String,
        @Query("description")
        teamId: String
    ): Response<MessageResponse>

    @POST(LEAVE_TEAM)
    suspend fun leaveTeam(
        @Header("Authorization")
        token: String,
        @Query("team_id")
        teamId: Int
    ): Response<MessageResponse>

    @Multipart
    @POST(UPDATE_PROFILE)
    suspend fun updateUser(
        @Header("Authorization")
        token: String,
        @Part
        imageUrl: MultipartBody.Part,
        @Part("name")
        name: RequestBody?,
        @Part("track")
        track: RequestBody?,
        @Part("bio")
        bio: RequestBody?,
        @Part("githubUrl")
        githubUrl: RequestBody?,
        @Part("facebookUrl")
        facebookUrl: RequestBody?,
        @Part("linkedinUrl")
        linkedinUrl: RequestBody?
    ): Response<UpdateUserResponse>
    @POST(UPDATE_PROFILE)
    suspend fun updateUser(
        @Header("Authorization")
        token: String,
        @Query("name")
        name: String? = null,
        @Query("track")
        track: String? = null,
        @Query("bio")
        bio: String? = null,
        @Query("githubUrl")
        githubUrl: String? = null,
        @Query("facebookUrl")
        facebookUrl: String? = null,
        @Query("linkedinUrl")
        linkedinUrl: String? = null
    ): Response<UpdateUserResponse>

    @GET(GET_ALL_DATA_ENDPOINT)
    suspend fun getAllData(
        @Header("Authorization")
        token: String
    ): Response<AllDataResponse>

    @GET(GET_TEAM_DETAILS)
    suspend fun getTeamDetails(
        @Header("Authorization")
        token: String,
        @Query("team_id")
        teamId: Int
    ): Response<TeamWithUsersResponse>

    @GET(GET_JOIN_TEAM_REQUESTS)
    suspend fun getJoinTeamRequests(
        @Header("Authorization")
        token: String,
        @Query("team_id")
        teamId: Int
    ): Response<JoinRequestsResponse>

    @GET(ACCEPT_MEMBER)
    suspend fun acceptUser(
        @Header("Authorization")
        token: String,
        @Query("team_id")
        teamId: Int,
        @Query("user_id")
        user_id: Int
    ): Response <MessageResponse>

    @GET(REJECT_MEMBER)
    suspend fun rejectUser(
        @Header("Authorization")
        token: String,
        @Query("team_id")
        teamId: Int,
        @Query("user_id")
        user_id: Int
    ):  Response <MessageResponse>

    @GET(DELETE_MEMBER)
    suspend fun deleteMember(
        @Header("Authorization")
        token: String,
        @Query("team_id")
        teamId: Int,
        @Query("user_id")
        user_id: Int
    ): Response<MessageResponse>
    
}