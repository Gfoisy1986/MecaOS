package com.example.mecaos.ui.travaux

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mecaos.data.entity.Employe
import com.example.mecaos.data.entity.Punch
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TravauxScreen(viewModel: TravauxViewModel, modifier: Modifier = Modifier) {
    val workOrdersWithJobs by viewModel.activeWorkOrders.collectAsState()
    val employes by viewModel.employes.collectAsState()
    var selectedEmploye by remember { mutableStateOf<Employe?>(null) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(employes) {
        if (employes.isNotEmpty() && selectedEmploye == null) {
            selectedEmploye = employes.first()
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Selected Employee: ")
            Box {
                Button(onClick = { expanded = true }) {
                    Text(text = selectedEmploye?.nom ?: "Select Employee")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    employes.forEach { employe ->
                        DropdownMenuItem(text = { Text(employe.nom) }, onClick = {
                            selectedEmploye = employe
                            expanded = false
                        })
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            workOrdersWithJobs.forEach { workOrderWithJobs ->
                stickyHeader {
                    Text(
                        text = "Work Order #${workOrderWithJobs.workOrder.id} - ${workOrderWithJobs.workOrder.customName}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(vertical = 8.dp)
                    )
                }

                items(workOrderWithJobs.jobs) { jobWithPunches ->
                    var showReportDialog by remember { mutableStateOf(false) }
                    var isExpanded by remember { mutableStateOf(false) }
                    val punches by viewModel.getPunchesForJob(jobWithPunches.job.jobId).collectAsState()

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(text = "Job: ${jobWithPunches.job.name}")
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                PunchButton(
                                    jobId = jobWithPunches.job.jobId,
                                    selectedEmploye = selectedEmploye,
                                    viewModel = viewModel,
                                    onPunchOut = { showReportDialog = true }
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Button(onClick = { isExpanded = !isExpanded }) {
                                    Text(if (isExpanded) "Hide Details" else "Show Details")
                                }
                            }

                            if (isExpanded) {
                                Spacer(modifier = Modifier.height(16.dp))
                                punches.sortedByDescending { it.timestamp }.forEach { punch ->
                                    val employe = employes.find { it.id == punch.employeId }
                                    val formattedTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(punch.timestamp))

                                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                        Text(
                                            text = "${employe?.nom ?: "Unknown"} punched ${punch.punchType} at $formattedTime",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        punch.report?.let { report ->
                                            if (report.isNotBlank()) {
                                                Text(
                                                    text = "  Report: $report",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    modifier = Modifier.padding(start = 8.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (showReportDialog) {
                        ReportDialog(
                            onDismiss = { showReportDialog = false },
                            onConfirm = {
                                viewModel.punchOut(jobWithPunches.job.jobId, selectedEmploye!!.id, it)
                                showReportDialog = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PunchButton(
    jobId: Int,
    selectedEmploye: Employe?,
    viewModel: TravauxViewModel,
    onPunchOut: () -> Unit
) {
    val punchInfo by produceState<Pair<Punch?, Punch?>>(Pair(null, null), selectedEmploye) {
        if (selectedEmploye != null) {
            combine(
                viewModel.getGlobalLastPunchForEmploye(selectedEmploye!!.id),
                viewModel.getLastPunchForJobAndEmploye(jobId, selectedEmploye!!.id)
            ) { globalPunch, jobPunch ->
                Pair(globalPunch, jobPunch)
            }.collect { value = it }
        } else {
            value = Pair(null, null)
        }
    }

    val (globalLastPunch, lastPunchForJob) = punchInfo
    val isPunchedInOnAnotherJob = (globalLastPunch?.punchType == "IN") && (globalLastPunch?.jobId != jobId)

    Row {
        when {
            selectedEmploye == null -> {
                Button(enabled = false, onClick = {}) {
                    Text("Punch In")
                }
            }
            isPunchedInOnAnotherJob -> {
                Text(text = "Punched in on another job")
            }
            lastPunchForJob?.punchType == "IN" -> {
                Button(onClick = onPunchOut) {
                    Text("Punch Out")
                }
            }
            else -> {
                Button(onClick = { viewModel.punchIn(jobId, selectedEmploye!!.id) }) {
                    Text("Punch In")
                }
            }
        }
    }
}

@Composable
fun ReportDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var report by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add Report") },
        text = {
            OutlinedTextField(
                value = report,
                onValueChange = { report = it },
                label = { Text("Report") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(report) }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
