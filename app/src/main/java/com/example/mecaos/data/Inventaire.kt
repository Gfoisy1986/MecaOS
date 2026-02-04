package com.example.mecaos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventaire")
data class Inventaire(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nom: String,
    val quantite: Int,
    @ColumnInfo(defaultValue = "0.0") val prix: Double
)