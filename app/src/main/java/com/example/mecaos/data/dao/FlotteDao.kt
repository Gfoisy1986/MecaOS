package com.example.mecaos.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mecaos.data.entity.Flotte
import kotlinx.coroutines.flow.Flow

@Dao
interface FlotteDao {
    @Query("SELECT * FROM flottes")
    fun getAll(): Flow<List<Flotte>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flotte: Flotte): Long

    @Update
    suspend fun update(flotte: Flotte)

    @Delete
    suspend fun delete(flotte: Flotte)
}
