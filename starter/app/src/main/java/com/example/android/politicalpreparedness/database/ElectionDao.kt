package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.SavedElection

@Dao
interface ElectionDao {

    // Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(election: Election)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElections(vararg elections: Election)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveElection(election: SavedElection)

    // Add select all election query
    @Query("SELECT * FROM election_table")
    fun getAllElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id IN (SELECT * FROM saved_election_table)")
    fun getSavedElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id NOT IN (SELECT * FROM saved_election_table)")
    fun getUpcomingElections(): LiveData<List<Election>>

    // Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :id")
    fun getElectionById(id: Int): LiveData<Election>

    @Query("SELECT COUNT(*) FROM saved_election_table WHERE id = :id")
    fun isSaved(id: Int): LiveData<Int>

    //Add delete query
    @Query("DELETE FROM election_table WHERE id = :id")
    suspend fun deleteElectionById(id: Int)

    //Add clear query
    @Query("DELETE FROM election_table")
    suspend fun deleteAllElections()

    @Query("DELETE FROM saved_election_table WHERE id = :id")
    suspend fun deleteSavedElectionById(id: Int)


}