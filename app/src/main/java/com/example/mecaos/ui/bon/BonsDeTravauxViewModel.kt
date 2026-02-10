package com.example.mecaos.ui.bon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecaos.data.dao.FlotteDao
import com.example.mecaos.data.dao.WorkOrderDao
import com.example.mecaos.data.entity.Flotte
import com.example.mecaos.data.entity.WorkOrder
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BonsDeTravauxViewModel(private val workOrderDao: WorkOrderDao, private val flotteDao: FlotteDao) : ViewModel() {

    val workOrders: StateFlow<List<WorkOrder>> = workOrderDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val flottes: StateFlow<List<Flotte>> = flotteDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insert(workOrder: WorkOrder) {
        viewModelScope.launch {
            workOrderDao.insert(workOrder)
        }
    }

    fun update(workOrder: WorkOrder) {
        viewModelScope.launch {
            workOrderDao.update(workOrder)
        }
    }

    fun delete(workOrder: WorkOrder) {
        viewModelScope.launch {
            workOrderDao.delete(workOrder)
        }
    }
}
