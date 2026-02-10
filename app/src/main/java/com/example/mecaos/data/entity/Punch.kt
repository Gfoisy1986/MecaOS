package com.example.mecaos.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "punches",
    foreignKeys = [
        ForeignKey(
            entity = Job::class,
            parentColumns = ["jobId"],
            childColumns = ["jobId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Employe::class,
            parentColumns = ["id"],
            childColumns = ["employeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Punch(
    @PrimaryKey(autoGenerate = true)
    val punchId: Int = 0,
    val jobId: Int,
    val employeId: Int,
    val timestamp: Long,
    val punchType: String, // "IN" or "OUT"
    val report: String? = null
)
