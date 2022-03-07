package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.SavedElection
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val elecId: Int, private val division: Division, private val dataSource: ElectionDao) : ViewModel() {

    //Add live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private val _savedCount: LiveData<Int>
        get() = dataSource.isSaved(elecId)
    val savedElection: LiveData<Boolean> = Transformations.map(_savedCount){
        it?.let{
            it > 0
        }
    }

    //Add var and methods to support loading URLs
    val locationUrl = Transformations.map(_voterInfo){
        it?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl
    }

    val ballotUrl = Transformations.map(_voterInfo){
        it?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl
    }


    init {
        getVoterInfo()
    }

    //Add var and methods to populate voter info
    private fun getVoterInfo(){
        viewModelScope.launch {
            val address = if (division.state.isNullOrBlank()) division.country else "${division.country},${division.state}"
            _voterInfo.value = CivicsApi.retrofitService.getVoterInfo(elecId,address)
        }
    }



    //lAdd var and methods to save and remove elections to local database
    fun saveElection(){
        viewModelScope.launch{
            Log.i("VoterInfoViewModel","saveElection() called")
            dataSource.saveElection(SavedElection(elecId))
        }
    }

    fun removeElection(){
        viewModelScope.launch {
            Log.i("VoterInfoViewModel","removeElection() called")
            dataSource.deleteSavedElectionById(elecId)
        }
    }

    //cont'd -- Populate initial state of save button to reflect proper action based on election saved status


    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}