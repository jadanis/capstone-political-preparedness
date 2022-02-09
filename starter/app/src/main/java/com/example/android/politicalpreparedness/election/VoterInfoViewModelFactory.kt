package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import java.lang.IllegalArgumentException

//Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(private val elecId: Int,private val division: Division, private val database: ElectionDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(VoterInfoViewModel::class.java)){
            return VoterInfoViewModel(elecId, division, database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}