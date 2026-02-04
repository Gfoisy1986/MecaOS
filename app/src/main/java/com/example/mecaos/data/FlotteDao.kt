package com.example.mecaos.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlotteDao {
    @Query("SELECT * FROM flottes")
    fun getAll(): Flow<List<Flotte>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flotte: Flotte)

    @Update
    suspend fun update(flotte: Flotte)

    @Delete
    suspend fun delete(flotte: Flotte)
}
