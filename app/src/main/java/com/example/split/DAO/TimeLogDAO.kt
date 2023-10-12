package com.example.split.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeLogDAO {
    @Query("select * from racelog order by time DESC")
    fun getTimeLog(): Flow<List<TimeLog>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(log: TimeLog)

//    @Query("update racelog set time = :time where id = :id")
//    fun updateTimeLog(log: TimeLog)
//
    @Delete
    fun deleteTimeLog(log: TimeLog)

    @Query("delete from racelog")
    fun deleteAll()

}