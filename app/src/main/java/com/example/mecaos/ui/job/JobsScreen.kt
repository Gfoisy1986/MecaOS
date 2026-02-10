package com.example.mecaos.ui.job

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mecaos.MecaOSApplication
import com.example.mecaos.data.entity.Job
import com.example.mecaos.data.entity.WorkOrder
import com.example.mecaos.ui.ViewModelFactory
import com.example.mecaos.ui.bon.BonsDeTravauxViewModel

@Composable
fun JobsScreen(
    modifier: Modifier = Modifier,
) {
    val application = LocalContext.current.applicationContext as MecaOSApplication
    val bonsDeTravauxViewModel: BonsDeTravauxViewModel = viewModel(factory = ViewModelFactory(application.container.database))
    val workOrders by bonsDeTravauxViewModel.workOrders.collectAsState()
    var selectedWorkOrder by remember { mutableStateOf<WorkOrder?>(null) }

    if (selectedWorkOrder != null) {
        JobsListScreen(modifier = modifier, workOrder = selectedWorkOrder!!, onBack = { selectedWorkOrder = null })
    } else {
        LazyColumn(modifier = modifier.padding(16.dp)) {
            items(workOrders) {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { selectedWorkOrder = it }) {
                    Text(text = "Bon de travail #${it.id} - ${it.customName}", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}


@Composable
fun JobsListScreen(
    modifier: Modifier = Modifier,
    workOrder: WorkOrder,
    onBack: () -> Unit
) {
    val application = LocalContext.current.applicationContext as MecaOSApplication
    val viewModel: JobsViewModel = viewModel(factory = ViewModelFactory(application.container.database, workOrder.id))
    val jobs by viewModel.jobs.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedJob by remember { mutableStateOf<Job?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(text = "Jobs for Work Order #${workOrder.id} - ${workOrder.customName}")
            }
            LazyColumn {
                items(jobs) { job ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { selectedJob = job; showDialog = true; }) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = job.name)
                                Text(text = job.description)
                            }
                            IconButton(onClick = { viewModel.deleteJob(job) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Job")
                            }
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { selectedJob = null; showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Job")
        }
    }

    if (showDialog) {
        JobDialog(job = selectedJob,
            onDismiss = { showDialog = false },
            onConfirm = { name, description ->
                if (selectedJob == null) {
                    viewModel.addJob(name, description)
                } else {
                    viewModel.updateJob(selectedJob!!.copy(name = name, description = description))
                }
                showDialog = false
            })
    }
}


@Composable
fun JobDialog(
    job: Job?,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(job?.name ?: "") }
    var description by remember { mutableStateOf(job?.description ?: "") }

    AlertDialog(onDismissRequest = onDismiss,
        title = { Text(text = if (job == null) "Add Job" else "Edit Job") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, description) }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        })
}
