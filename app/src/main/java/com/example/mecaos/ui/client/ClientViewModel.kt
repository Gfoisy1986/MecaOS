package com.example.mecaos.ui.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecaos.data.Client
import com.example.mecaos.data.ClientDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ClientViewModel(private val clientDao: ClientDao) : ViewModel() {

    val clients: StateFlow<List<Client>> = clientDao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insert(client: Client) {
        viewModelScope.launch {
            clientDao.insert(client)
        }
    }

    fun update(client: Client) {
        viewModelScope.launch {
            clientDao.update(client)
        }
    }

    fun delete(client: Client) {
        viewModelScope.launch {
            clientDao.delete(client)
        }
    }
}
