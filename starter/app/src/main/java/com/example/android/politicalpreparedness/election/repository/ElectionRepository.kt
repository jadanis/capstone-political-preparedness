package com.example.android.politicalpreparedness.election.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.SavedElection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class ElectionRepository(private val database: ElectionDatabase) {

    val elections: LiveData<List<Election>> = database.electionDao.getUpcomingElections()
    val savedElections: LiveData<List<Election>> = database.electionDao.getSavedElections()

    suspend fun refreshElections(){
        withContext(Dispatchers.IO){
            try{
                val electionResponse = CivicsApi.retrofitService.getElections()
                database.electionDao.insertElections(*electionResponse.elections.toTypedArray())
            } catch(e: Exception){
                Log.e("ElectionRepository","Error: ${e.message}")
            }
        }
    }

}