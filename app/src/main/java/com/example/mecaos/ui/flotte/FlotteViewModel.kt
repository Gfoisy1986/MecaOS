package com.example.mecaos.ui.flotte

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecaos.data.ClientDao
import com.example.mecaos.data.Flotte
import com.example.mecaos.data.FlotteDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlotteViewModel(private val flotteDao: FlotteDao, private val clientDao: ClientDao) : ViewModel() {

    val flottes: StateFlow<List<Flotte>> = flotteDao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val clients = clientDao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insert(flotte: Flotte) {
        viewModelScope.launch {
            flotteDao.insert(flotte)
        }
    }

    fun update(flotte: Flotte) {
        viewModelScope.launch {
            flotteDao.update(flotte)
        }
    }

    fun delete(flotte: Flotte) {
        viewModelScope.launch {
            flotteDao.delete(flotte)
        }
    }
}
