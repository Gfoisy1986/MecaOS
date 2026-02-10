package com.example.mecaos.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mecaos.data.entity.WorkOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkOrderDao {
    @Insert
    suspend fun insert(workOrder: WorkOrder)

    @Update
    suspend fun update(workOrder: WorkOrder)

    @Delete
    suspend fun delete(workOrder: WorkOrder)

    @Query("SELECT * FROM work_orders ORDER BY id DESC")
    fun getAll(): Flow<List<WorkOrder>>

    @Query("SELECT * FROM work_orders WHERE status = 'active' ORDER BY id DESC")
    fun getActiveWorkOrders(): Flow<List<WorkOrder>>
}
