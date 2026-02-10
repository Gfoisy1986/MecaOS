package com.example.mecaos.ui.travaux

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecaos.data.dao.EmployeDao
import com.example.mecaos.data.dao.JobDao
import com.example.mecaos.data.dao.PunchDao
import com.example.mecaos.data.dao.WorkOrderDao
import com.example.mecaos.data.entity.Employe
import com.example.mecaos.data.entity.Job
import com.example.mecaos.data.entity.Punch
import com.example.mecaos.data.entity.WorkOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class WorkOrderWithJobs(val workOrder: WorkOrder, val jobs: List<JobWithPunches>)
data class JobWithPunches(val job: Job, val punches: List<Punch>)

class TravauxViewModel(
    private val workOrderDao: WorkOrderDao,
    private val jobDao: JobDao,
    private val punchDao: PunchDao,
    private val employeDao: EmployeDao
) : ViewModel() {

    val activeWorkOrders: StateFlow<List<WorkOrderWithJobs>> = workOrderDao.getActiveWorkOrders()
        .combine(jobDao.getAllJobs()) { workOrders, jobs ->
            workOrders.map { workOrder ->
                val jobsForWorkOrder = jobs.filter { it.workOrderId == workOrder.id }
                WorkOrderWithJobs(
                    workOrder = workOrder,
                    jobs = jobsForWorkOrder.map { job ->
                        JobWithPunches(job, emptyList()) // Placeholder for punches
                    }
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val employes: StateFlow<List<Employe>> = employeDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun punchIn(jobId: Int, employeId: Int) {
        viewModelScope.launch {
            punchDao.insert(Punch(jobId = jobId, employeId = employeId, timestamp = System.currentTimeMillis(), punchType = "IN"))
        }
    }

    fun punchOut(jobId: Int, employeId: Int, report: String) {
        viewModelScope.launch {
            punchDao.insert(Punch(jobId = jobId, employeId = employeId, timestamp = System.currentTimeMillis(), punchType = "OUT", report = report))
        }
    }

    fun getPunchesForJob(jobId: Int): StateFlow<List<Punch>> {
        return punchDao.getPunchesForJob(jobId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun getLastPunchForJobAndEmploye(jobId: Int, employeId: Int): Flow<Punch?> {
        return punchDao.getLastPunchForJobAndEmploye(jobId, employeId)
    }

    fun getGlobalLastPunchForEmploye(employeId: Int): Flow<Punch?> {
        return punchDao.getGlobalLastPunchForEmploye(employeId)
    }
}
