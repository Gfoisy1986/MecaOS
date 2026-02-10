package com.example.mecaos.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employe")
data class Employe(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nom: String,
    val email: String,
    val cellulaire: String,
    val addresse: String,
    val dateNaissance: String,
    val liscenseMecanicien: String,
    val numeropep: String,
    val numerosaaq: String,
    val refpermisconduire: String,
    val idpermisconduire: String
)
