

import com.team.cat_hackathon.data.api.ACCEPT_MEMBER
import com.team.cat_hackathon.data.api.CREATE_TEAM
import com.team.cat_hackathon.data.api.DELETE_TEAM
import com.team.cat_hackathon.data.api.GET_ALL_DATA_ENDPOINT
import com.team.cat_hackathon.data.api.GET_JOIN_TEAM_REQUESTS
import com.team.cat_hackathon.data.api.GET_TEAM_BY_ID
import com.team.cat_hackathon.data.api.GET_TEAM_DETAILS
import com.team.cat_hackathon.data.api.JOIN_TEAM
import com.team.cat_hackathon.data.api.LOGIN_ENDPOINT
import com.team.cat_hackathon.data.api.LOG_OUT_ENDPOINT
import com.team.cat_hackathon.data.api.REGISTER_ENDPOINT
import com.team.cat_hackathon.data.api.REJECT_MEMBER
import com.team.cat_hackathon.data.models.AllDataResponse
import com.team.cat_hackathon.data.models.AuthResponse
import com.team.cat_hackathon.data.models.MessageResponse
import com.team.cat_hackathon.data.models.JoinRequestsResponse
import com.team.cat_hackathon.data.models.TeamResponse
import com.team.cat_hackathon.data.models.TeamWithUsers
import com.team.cat_hackathon.data.models.TeamWithUsersResponse
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

    @POST(CREATE_TEAM)
    suspend fun createTeam(
        @Header("Authorization")
        token: String,
        @Query("name")
        name: String,
        @Query("description")
        teamId: String
    ): Response<MessageResponse>

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

}