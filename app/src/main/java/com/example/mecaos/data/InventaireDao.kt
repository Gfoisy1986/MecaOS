package com.example.mecaos.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface InventaireDao {
    @Query("SELECT * FROM inventaire")
    fun getAll(): Flow<List<Inventaire>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(inventaire: Inventaire)

    @Update
    suspend fun update(inventaire: Inventaire)

    @Delete
    suspend fun delete(inventaire: Inventaire)
}