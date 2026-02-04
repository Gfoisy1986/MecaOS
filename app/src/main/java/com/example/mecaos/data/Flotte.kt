package com.example.mecaos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flottes")
data class Flotte(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val serie: String,
    val unit: String,
    val noment: String, // This will be linked to a client
    val annee: Int,
    val fabricant: String,
    val model: String,
    val km: Int,
    val hrs: Int,
    val ecm: String,
    val plate: String,
    val nextmaintkm: Int,
    val nextmainthrs: Int,
    val lastmaintkm: Int,
    val lastmainthrs: Int,
    val fingdate: String,
    val fingkm: Int,
    val finghrs: Int
)
