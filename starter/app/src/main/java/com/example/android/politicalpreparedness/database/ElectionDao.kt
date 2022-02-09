package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    // Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(election: Election)

    // Add select all election query
    @Query("SELECT * FROM election_table")
    fun getAllElections(): LiveData<List<Election>>

    // Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :id")
    fun getElectionById(id: Int): LiveData<Election>

    @Query("SELECT COUNT(*) FROM election_table WHERE id = :id")
    fun contains(id: Int): LiveData<Int>

    //Add delete query
    @Query("DELETE FROM election_table WHERE id = :id")
    suspend fun deleteElectionById(id: Int)

    //Add clear query
    @Query("DELETE FROM election_table")
    suspend fun deleteAllElections()


}