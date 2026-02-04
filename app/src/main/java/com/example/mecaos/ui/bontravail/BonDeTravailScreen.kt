package com.example.mecaos.ui.bontravail

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.example.mecaos.data.WorkOrder

@Composable
fun BonDeTravailScreen(viewModel: BonDeTravailViewModel, modifier: Modifier = Modifier) {
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
            LazyColumn(modifier = Modifier.width(1000.dp)) {
                item {
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "ID", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
                        Text(text = "Nom Entreprise", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text(text = "Unit", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
                        Text(text = "Serie", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text(text = "Status", modifier = Modifier.weight(0.7f), fontWeight = FontWeight.Bold)
                        Text(text = "Nom Personnalisé", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
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
                        Text(text = item.id.toString(), modifier = Modifier.weight(0.5f))
                        Text(text = item.nomEntreprise, modifier = Modifier.weight(1f))
                        Text(text = item.unit, modifier = Modifier.weight(0.5f))
                        Text(text = item.serie, modifier = Modifier.weight(1f))
                        Text(text = item.status, modifier = Modifier.weight(0.7f))
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
        BonDeTravailUpsertDialog(
            item = selectedItem,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BonDeTravailUpsertDialog(
    item: WorkOrder?,
    onDismiss: () -> Unit,
    onSave: (WorkOrder) -> Unit
) {
    var nomEntreprise by remember { mutableStateOf(item?.nomEntreprise ?: "") }
    var unit by remember { mutableStateOf(item?.unit ?: "") }
    var serie by remember { mutableStateOf(item?.serie ?: "") }
    var status by remember { mutableStateOf(item?.status ?: "Ouvert") }
    var customName by remember { mutableStateOf(item?.customName ?: "") }
    val statusOptions = listOf("Ouvert", "Terminé", "Fermé")
    var expanded by remember { mutableStateOf(false) }


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
                OutlinedTextField(
                    value = nomEntreprise,
                    onValueChange = { nomEntreprise = it },
                    label = { Text("Nom Entreprise") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Unit") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = serie,
                    onValueChange = { serie = it },
                    label = { Text("Serie") },
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = status,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        statusOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    status = option
                                    expanded = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = customName,
                    onValueChange = { customName = it },
                    label = { Text("Nom Personnalisé") },
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
