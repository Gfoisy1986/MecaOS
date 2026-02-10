package com.example.mecaos.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mecaos.data.entity.Employe
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeDao {
    @Query("SELECT * FROM employe")
    fun getAll(): Flow<List<Employe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employe: Employe)

    @Update
    suspend fun update(employe: Employe)

    @Delete
    suspend fun delete(employe: Employe)
}
