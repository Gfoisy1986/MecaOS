package com.example.mecaos.ui.bon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mecaos.data.Flotte
import com.example.mecaos.data.WorkOrder

@Composable
fun BonsDeTravauxScreen(viewModel: BonsDeTravauxViewModel, modifier: Modifier = Modifier) {
    val workOrders by viewModel.workOrders.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<WorkOrder?>(null) }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedItem = null
                showDialog = true
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Créer un bon de travail")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).horizontalScroll(rememberScrollState())) {
            LazyColumn(modifier = Modifier.width(1200.dp)) {
                item {
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Nom de l'entreprise", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text(text = "Unité", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text(text = "Série", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text(text = "Statut", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text(text = "Nom personnalisé", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Box(modifier = Modifier.weight(0.5f))
                    }
                }
                itemsIndexed(workOrders) { index, item ->
                    val backgroundColor = if (index % 2 == 0) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                    Row(
                        modifier = Modifier
                            .background(backgroundColor)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = item.nomEntreprise, modifier = Modifier.weight(1f))
                        Text(text = item.unit, modifier = Modifier.weight(1f))
                        Text(text = item.serie, modifier = Modifier.weight(1f))
                        Text(text = item.status, modifier = Modifier.weight(1f))
                        Text(text = item.customName, modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier.weight(0.5f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = {
                                selectedItem = item
                                showDialog = true
                            }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Modifier")
                            }
                            IconButton(onClick = { viewModel.delete(item) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Supprimer")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        val flottes by viewModel.flottes.collectAsState()
        BonsDeTravauxUpsertDialog(
            item = selectedItem,
            flottes = flottes,
            onDismiss = { showDialog = false },
            onSave = { updatedItem ->
                if (selectedItem == null) {
                    viewModel.insert(updatedItem)
                } else {
                    viewModel.update(updatedItem)
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun BonsDeTravauxUpsertDialog(
    item: WorkOrder?,
    flottes: List<Flotte>,
    onDismiss: () -> Unit,
    onSave: (WorkOrder) -> Unit
) {
    var nomEntreprise by remember { mutableStateOf(item?.nomEntreprise ?: "") }
    var unit by remember { mutableStateOf(item?.unit ?: "") }
    var serie by remember { mutableStateOf(item?.serie ?: "") }
    var status by remember { mutableStateOf(item?.status ?: "active") } // Default to "active" for new work orders
    var customName by remember { mutableStateOf(item?.customName ?: "") }

    var flotteMenuExpanded by remember { mutableStateOf(false) }
    var statusMenuExpanded by remember { mutableStateOf(false) }

    val statusOptions = listOf("active", "done", "close")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (item == null) "Créer un bon de travail" else "Modifier un bon de travail") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = if (nomEntreprise.isEmpty() && unit.isEmpty() && serie.isEmpty()) "" else "${nomEntreprise} - ${unit} - ${serie}",
                        onValueChange = { },
                        label = { Text("Flotte") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = "Select Flotte",
                                modifier = Modifier.clickable { flotteMenuExpanded = true })
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = flotteMenuExpanded,
                        onDismissRequest = { flotteMenuExpanded = false }
                    ) {
                        flottes.forEach {
                            DropdownMenuItem(text = { Text("${it.noment} - ${it.unit} - ${it.serie}") }, onClick = {
                                nomEntreprise = it.noment
                                unit = it.unit
                                serie = it.serie
                                flotteMenuExpanded = false
                            })
                        }
                    }
                }

                if (item == null) { // For new work orders, status is read-only "active"
                    OutlinedTextField(
                        value = status,
                        onValueChange = { },
                        label = { Text("Statut") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else { // For existing work orders, show dropdown with options
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = status,
                            onValueChange = { },
                            label = { Text("Statut") },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.ArrowDropDown,
                                    contentDescription = "Select Status",
                                    modifier = Modifier.clickable { statusMenuExpanded = true })
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenu(
                            expanded = statusMenuExpanded,
                            onDismissRequest = { statusMenuExpanded = false }
                        ) {
                            statusOptions.forEach { statusOption ->
                                DropdownMenuItem(text = { Text(statusOption) }, onClick = {
                                    status = statusOption
                                    statusMenuExpanded = false
                                })
                            }
                        }
                    }
                }
                OutlinedTextField(
                    value = customName,
                    onValueChange = { customName = it },
                    label = { Text("Nom personnalisé") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedItem = WorkOrder(
                        id = item?.id ?: 0,
                        nomEntreprise = nomEntreprise,
                        unit = unit,
                        serie = serie,
                        status = status,
                        customName = customName
                    )
                    onSave(updatedItem)
                }
            ) {
                Text("Sauvegarder")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}
