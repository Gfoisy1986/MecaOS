package com.example.mecaos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mecaos.data.entity.Punch
import kotlinx.coroutines.flow.Flow

@Dao
interface PunchDao {
    @Insert
    suspend fun insert(punch: Punch)

    @Query("SELECT * FROM punches WHERE jobId = :jobId ORDER BY timestamp DESC")
    fun getPunchesForJob(jobId: Int): Flow<List<Punch>>

    @Query("SELECT * FROM punches WHERE jobId = :jobId AND employeId = :employeId ORDER BY timestamp DESC LIMIT 1")
    fun getLastPunchForJobAndEmploye(jobId: Int, employeId: Int): Flow<Punch?>

    @Query("SELECT * FROM punches WHERE employeId = :employeId ORDER BY timestamp DESC LIMIT 1")
    fun getGlobalLastPunchForEmploye(employeId: Int): Flow<Punch?>
}
