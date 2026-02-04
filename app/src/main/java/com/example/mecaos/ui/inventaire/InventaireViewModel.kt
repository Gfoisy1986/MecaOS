package com.example.mecaos.ui.inventaire

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecaos.data.Inventaire
import com.example.mecaos.data.InventaireDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventaireViewModel(private val inventaireDao: InventaireDao) : ViewModel() {

    val inventaire: StateFlow<List<Inventaire>> = inventaireDao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insert(inventaire: Inventaire) {
        viewModelScope.launch {
            inventaireDao.insert(inventaire)
        }
    }

    fun update(inventaire: Inventaire) {
        viewModelScope.launch {
            inventaireDao.update(inventaire)
        }
    }

    fun delete(inventaire: Inventaire) {
        viewModelScope.launch {
            inventaireDao.delete(inventaire)
        }
    }
}
