package com.example.mecaos.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkOrderDao {
    @Query("SELECT * FROM work_orders")
    fun getAll(): Flow<List<WorkOrder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workOrder: WorkOrder)

    @Update
    suspend fun update(workOrder: WorkOrder)

    @Delete
    suspend fun delete(workOrder: WorkOrder)
}
