package com.example.mecaos.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "jobs",
    foreignKeys = [
        ForeignKey(entity = WorkOrder::class,
            parentColumns = ["id"],
            childColumns = ["workOrderId"],
            onDelete = ForeignKey.CASCADE)
    ])
data class Job(
    @PrimaryKey(autoGenerate = true)
    val jobId: Int = 0,
    val workOrderId: Int,
    val name: String,
    val description: String,
    val status: String = "at work"
)
