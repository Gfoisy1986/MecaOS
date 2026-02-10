package com.example.mecaos.ui.job

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecaos.data.dao.JobDao
import com.example.mecaos.data.entity.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JobsViewModel(private val jobDao: JobDao, private val workOrderId: Int) : ViewModel() {

    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> = _jobs.asStateFlow()

    init {
        viewModelScope.launch {
            jobDao.getJobsForWorkOrder(workOrderId).collect {
                _jobs.value = it
            }
        }
    }

    fun addJob(name: String, description: String) {
        viewModelScope.launch {
            jobDao.insert(Job(workOrderId = workOrderId, name = name, description = description))
        }
    }

    fun updateJob(job: Job) {
        viewModelScope.launch {
            jobDao.update(job)
        }
    }

    fun deleteJob(job: Job) {
        viewModelScope.launch {
            jobDao.delete(job)
        }
    }
}
