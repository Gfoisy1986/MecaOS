package com.example.mecaos.ui.bon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecaos.data.AppDatabase
import com.example.mecaos.data.Flotte
import com.example.mecaos.data.WorkOrder
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BonsDeTravauxViewModel(private val db: AppDatabase) : ViewModel() {

    val workOrders: StateFlow<List<WorkOrder>> = db.workOrderDao().getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val flottes: StateFlow<List<Flotte>> = db.flotteDao().getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
