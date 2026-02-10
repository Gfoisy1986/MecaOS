package com.example.mecaos.ui.employe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecaos.data.dao.EmployeDao
import com.example.mecaos.data.entity.Employe
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EmployeViewModel(private val employeDao: EmployeDao) : ViewModel() {

    val employes: StateFlow<List<Employe>> = employeDao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insert(employe: Employe) {
        viewModelScope.launch {
            employeDao.insert(employe)
        }
    }

    fun update(employe: Employe) {
        viewModelScope.launch {
            employeDao.update(employe)
        }
    }

    fun delete(employe: Employe) {
        viewModelScope.launch {
            employeDao.delete(employe)
        }
    }
}
