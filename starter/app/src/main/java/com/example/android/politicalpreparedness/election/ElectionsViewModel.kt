package com.example.android.politicalpreparedness.election

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.repository.ElectionRepository
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch


//Construct ViewModel and provide election datasource
class ElectionsViewModel(context: Context): ViewModel() {

    private val database = ElectionDatabase.getInstance(context)
    private val repository = ElectionRepository(database)

    //Create live data val for upcoming elections
    //private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>> = repository.elections
        //get() = _upcomingElections

    //Create live data val for saved elections
    //private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>> = repository.savedElections
        //get() = _savedElections

    private val _navigateToElection = MutableLiveData<Election>()
    val navigateToElection: LiveData<Election>
        get() = _navigateToElection

    init {
        viewModelScope.launch {
            repository.refreshElections()
        }
    }


//    // Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
//    private fun getUpcomingElections(){
//        viewModelScope.launch {
//            try {
//                var elecResponse = CivicsApi.retrofitService.getElections()
//                Log.i("ElectionsViewModel","${elecResponse.elections.size} were returned")
//                _upcomingElections.value = elecResponse.elections
//            } catch (e: Exception){
//                //_upcomingElections.value = "Failure: ${e.message}"
//                Log.e("ElectionsViewModel","Failed network call: ${e.message}")
//            }
//        }
//    }
//
//    private fun getSavedElections(){
//        viewModelScope.launch {
//            Log.i("ElectionsViewModel","getSavedElections() called")
//            try {
//                val elecs = database.electionDao.getAllElections().value
//                Log.i("ElectionsViewModel","Elections from Database: ${elecs?.size}")
//                _savedElections.value = elecs
//            } catch(e: Exception){
//                Log.e("ElectionsViewModel","Failed database call: ${e.message}")
//            }
//        }
//    }

    //Create functions to navigate to saved or upcoming election voter info
    fun onElectionClick(election: Election){
        _navigateToElection.value = election
    }

    fun onElectionNavigated(){
        _navigateToElection.value = null
    }

}