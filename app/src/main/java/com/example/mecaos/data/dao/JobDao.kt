package com.example.mecaos.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mecaos.data.entity.Job
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Insert
    suspend fun insert(job: Job)

    @Update
    suspend fun update(job: Job)

    @Delete
    suspend fun delete(job: Job)

    @Query("SELECT * FROM jobs WHERE workOrderId = :workOrderId")
    fun getJobsForWorkOrder(workOrderId: Int): Flow<List<Job>>

    @Query("SELECT * FROM jobs")
    fun getAllJobs(): Flow<List<Job>>
}
