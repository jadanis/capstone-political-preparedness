package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel: ViewModel() {

    //TODO: Create live data val for upcoming elections
    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    private fun getUpcomingElections(){
        viewModelScope.launch {
            try {
                var listResult = CivicsApi.retrofitService.getElections()
                _upcomingElections.value = listResult
            } catch (e: Exception){
                //_upcomingElections.value = "Failure: ${e.message}"
            }
        }
    }

    //TODO: Create functions to navigate to saved or upcoming election voter info

}