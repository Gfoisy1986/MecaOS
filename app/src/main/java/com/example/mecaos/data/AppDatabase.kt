package com.example.mecaos.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mecaos.data.dao.ClientDao
import com.example.mecaos.data.dao.EmployeDao
import com.example.mecaos.data.dao.FlotteDao
import com.example.mecaos.data.dao.InventaireDao
import com.example.mecaos.data.dao.JobDao
import com.example.mecaos.data.dao.PunchDao
import com.example.mecaos.data.dao.WorkOrderDao
import com.example.mecaos.data.entity.Client
import com.example.mecaos.data.entity.Employe
import com.example.mecaos.data.entity.Flotte
import com.example.mecaos.data.entity.Inventaire
import com.example.mecaos.data.entity.Job
import com.example.mecaos.data.entity.Punch
import com.example.mecaos.data.entity.WorkOrder

@Database(
    entities = [Inventaire::class, Employe::class, Client::class, Flotte::class, WorkOrder::class, Job::class, Punch::class],
    version = 11,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun inventaireDao(): InventaireDao
    abstract fun employeDao(): EmployeDao
    abstract fun clientDao(): ClientDao
    abstract fun flotteDao(): FlotteDao
    abstract fun workOrderDao(): WorkOrderDao
    abstract fun jobDao(): JobDao
    abstract fun punchDao(): PunchDao

    companion object {
        const val DATABASE_NAME = "myDatabase.sqlite3"
    }
}
