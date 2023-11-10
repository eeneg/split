package com.example.split.DAO.Participant

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ParticipantDao {
    @Query("select * from participant order by name")
    fun getParticipant(): Flow<List<Participant>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(participant: Participant)

    @Query("delete from participant where userId = :id")
    fun deleteAll(id: String)

}