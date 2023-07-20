package com.team.cat_hackathon.presentation.fragmentHome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.data.api.RequestState
import com.team.cat_hackathon.data.models.MyResponse
import com.team.cat_hackathon.data.repositories.BaseRepositoryImpl
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class HomeViewModel @Inject constructor(val repository: BaseRepositoryImpl) : ViewModel() {

    private val _models: MutableLiveData<RequestState<MyResponse>> = MutableLiveData()
    val models: LiveData<RequestState<MyResponse>> = _models
    var modelResponse: MyResponse? = null


    // this viewModelScope.launch means we are running this logic asynchronous
    /* you can see a green arrow at line 31 on the left this means this method is suspended which means
    we will wait untill it's finished before continuing
    */
    fun getBreakingNews(countryCode: String) = viewModelScope.launch {

        //we should observe on this models list if loading we show progress bar otherwise we show data or error
        // we are now sending the request then we are in loading state untill we get the response
        _models.postValue(RequestState.Loading())
        // here we send the request from the repository
        val response = repository.getDataFromApi(countryCode, 0)
        // now after we got the response we need to handle it to see if error occurred
        _models.postValue(handleDataFromMyResponse(response))
    }


    /*this method takes the response from the api and returns it's state
    if successful it adds the data to the old data and returns it as Success Request state
    if an error is encountered it returns the message of that error as Error Request state
     */
    private fun handleDataFromMyResponse(response: Response<MyResponse>): RequestState<MyResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                // pageNumber++   uncomment if you want pagination
                if (modelResponse == null) {
                    // this means this is the first call and we donot have data yet
                    modelResponse = result
                } else {
                    // adding new request data to currend data (if using pagination)
                    val oldData = modelResponse?.articles
                    val newData = result.articles
                    oldData?.addAll(newData)

                }
                return RequestState.Sucess(modelResponse ?: result)
            }
        }
        return RequestState.Error(response.message())
    }

    // you can access database methods from the repository too after adding it's methods to the repository

    // so the repository is the only place where we can get our data
}