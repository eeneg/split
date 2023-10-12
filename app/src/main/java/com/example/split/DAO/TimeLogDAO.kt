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

    @Query("update racelog set time = :time, bib = :bib where id = :id")
    fun updateTimeLog(time: String, bib: String, id: Int)
//
    @Delete
    fun deleteTimeLog(log: TimeLog)

    @Query("delete from racelog")
    fun deleteAll()

}