package com.example.mecaos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mecaos.data.AppDatabase
import com.example.mecaos.ui.client.ClientViewModel
import com.example.mecaos.ui.employe.EmployeViewModel
import com.example.mecaos.ui.flotte.FlotteViewModel
import com.example.mecaos.ui.inventaire.InventaireViewModel

class ViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventaireViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventaireViewModel(database.inventaireDao()) as T
        }
        if (modelClass.isAssignableFrom(EmployeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EmployeViewModel(database.employeDao()) as T
        }
        if (modelClass.isAssignableFrom(ClientViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClientViewModel(database.clientDao()) as T
        }
        if (modelClass.isAssignableFrom(FlotteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FlotteViewModel(database.flotteDao(), database.clientDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
