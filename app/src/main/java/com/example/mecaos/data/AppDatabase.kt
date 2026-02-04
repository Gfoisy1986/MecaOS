package com.example.mecaos.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Inventaire::class, Employe::class, Client::class, Flotte::class, WorkOrder::class],
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun inventaireDao(): InventaireDao
    abstract fun employeDao(): EmployeDao
    abstract fun clientDao(): ClientDao
    abstract fun flotteDao(): FlotteDao
    abstract fun workOrderDao(): WorkOrderDao

    companion object {
        const val DATABASE_NAME = "myDatabase.sqlite3"
    }
}
