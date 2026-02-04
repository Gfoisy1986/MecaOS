package com.example.mecaos

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mecaos.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface AppContainer {
    val database: AppDatabase
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        // Correctly escape the single quote in "d'hiver" by doubling it for SQL.
                        db.execSQL("INSERT INTO inventaire (nom, quantite, prix) VALUES ('Pneu d''hiver', 12, 150.0)")
                        db.execSQL("INSERT INTO inventaire (nom, quantite, prix) VALUES ('Pneu d''été', 8, 120.0)")
                        db.execSQL("INSERT INTO employes (nom, email, cellulaire, addresse, dateNaissance, liscenseMecanicien, numeropep, numerosaaq, refpermisconduire, idpermisconduire) VALUES ('John Doe', 'john.doe@example.com', '555-1234', '123 Main St', '1990-01-01', 'MEC-12345', '', '', '', '')")
                    }
                }
            })
            .build()
    }
}

class MecaOSApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
