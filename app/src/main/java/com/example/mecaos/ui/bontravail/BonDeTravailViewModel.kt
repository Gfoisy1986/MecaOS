package com.example.mecaos.ui.bontravail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecaos.data.AppDatabase
import com.example.mecaos.data.WorkOrder
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BonDeTravailViewModel(private val db: AppDatabase) : ViewModel() {
    val workOrders: StateFlow<List<WorkOrder>> = db.workOrderDao().getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun insert(workOrder: WorkOrder) {
        viewModelScope.launch {
            db.workOrderDao().insert(workOrder)
        }
    }

    fun update(workOrder: WorkOrder) {
        viewModelScope.launch {
            db.workOrderDao().update(workOrder)
        }
    }

    fun delete(workOrder: WorkOrder) {
        viewModelScope.launch {
            db.workOrderDao().delete(workOrder)
        }
    }
}
