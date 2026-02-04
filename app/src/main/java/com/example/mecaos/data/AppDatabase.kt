package com.example.mecaos.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Inventaire::class, Employe::class, Client::class, Flotte::class],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun inventaireDao(): InventaireDao
    abstract fun employeDao(): EmployeDao
    abstract fun clientDao(): ClientDao
    abstract fun flotteDao(): FlotteDao

    companion object {
        const val DATABASE_NAME = "myDatabase.sqlite3"
    }
}
