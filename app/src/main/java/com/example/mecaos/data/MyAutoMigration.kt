package com.example.mecaos.data

import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.RenameTable
import androidx.room.migration.AutoMigrationSpec

@RenameTable(fromTableName = "Inventaire", toTableName = "inventaire")
@DeleteColumn.Entries(
    DeleteColumn(tableName = "Inventaire", columnName = "description")
)
@DeleteTable.Entries(
    DeleteTable(tableName = "Employe")
)
class MyAutoMigration : AutoMigrationSpec
