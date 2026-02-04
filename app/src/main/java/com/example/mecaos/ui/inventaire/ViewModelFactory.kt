package com.example.mecaos.ui.inventaire

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mecaos.data.InventaireDao

class ViewModelFactory(private val inventaireDao: InventaireDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventaireViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventaireViewModel(inventaireDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}