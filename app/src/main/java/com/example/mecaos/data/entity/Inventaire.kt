package com.example.mecaos.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventaire")
data class Inventaire(
    @PrimaryKey(autoGenerate = true)
    val inventaireId: Int = 0,
    val name: String,
    val quantity: Int
)
