package com.example.mecaos.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "work_orders")
data class WorkOrder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nomEntreprise: String,
    val unit: String,
    val serie: String,
    val status: String = "active",
    val customName: String
)
