package com.team.cat_hackathon.data.repositories

import android.content.Context
import com.androiddevs.mvvmnewsapp.data.api.RetrofitInstance
import com.team.cat_hackathon.data.models.Model
import com.team.cat_hackathon.data.models.MyResponse
import com.team.cat_hackathon.data.source.MyDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class BaseRepositoryImpl (val dao : MyDao , val context : Context) {

//    TODO: remember this when you add data store.(remove if not needed)
//    @Inject
//    lateinit var dataStore :

    //you can just  call these methods from the viewModel

    // TODO: example of how to get data from retrofit
    suspend fun getDataFromApi(countryCode: String, pageNumber: Int = 1): Response<MyResponse> {
        return RetrofitInstance.api.getHeadLineNews(
            countryCode = countryCode,
            pageNumber = pageNumber
        )
    }

    //todo : example of how to get data from room
    suspend fun getAllFromRoom() : List<Model> = withContext(Dispatchers.IO){
        dao.getAllModels()
    }

}