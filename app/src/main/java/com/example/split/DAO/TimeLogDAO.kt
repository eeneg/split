package com.example.split.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface TimeLogDAO {
    @Query("select * from racelog order by time DESC")
    fun getTimeLog(): Flow<List<TimeLog>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(bib: String, time: String)

    @Query("update racelog set time = :time where id = :id")
    fun updateTimeLog(time: String, id: Int)

    @Query("delete from racelog where id = :id LIMIT 1")
    fun deleteTimeLog(id: Int)

    @Query("delete from racelog")
    suspend fun deleteAll()

}