package com.example.mecaos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nomentreprise: String,
    val nomproprietaire: String,
    val nomresponsable: String,
    val addresse: String,
    val telephone: String,
    val email: String,
    val fax: String,
    val tauxhx: Double
)
