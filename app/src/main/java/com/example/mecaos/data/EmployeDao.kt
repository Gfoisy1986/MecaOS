package com.example.mecaos.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeDao {
    @Query("SELECT * FROM employes")
    fun getAll(): Flow<List<Employe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employe: Employe)

    @Update
    suspend fun update(employe: Employe)

    @Delete
    suspend fun delete(employe: Employe)
}
