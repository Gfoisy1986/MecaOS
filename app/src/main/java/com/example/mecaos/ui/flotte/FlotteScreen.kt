package com.example.mecaos.ui.flotte

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
import com.example.mecaos.data.Client
import com.example.mecaos.data.Flotte

@Composable
fun FlotteScreen(viewModel: FlotteViewModel, modifier: Modifier = Modifier) {
    val flottes by viewModel.flottes.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<Flotte?>(null) }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            if (flottes.isNotEmpty()) {
                FloatingActionButton(onClick = {
                    selectedItem = null
                    showDialog = true
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Créer un item")
                }
            }
        }
    ) { paddingValues ->
        if (flottes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = {
                    selectedItem = null
                    showDialog = true
                }) {
                    Text("Créer un item")
                }
            }
        } else {
            Box(modifier = Modifier.padding(paddingValues).horizontalScroll(rememberScrollState())) {
                LazyColumn(modifier = Modifier.width(2400.dp)) {
                    item {
                        Row(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Numero serie VIN", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Numero unité", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Noms entreprise", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Année du vehicule", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Le fabricant du vehicule", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Model du vehicule", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Kilometrage", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Les heures vehicule", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Password ecm", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Numero liscence plate", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Prochaine manitenance km", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Prochaine manitenance hrs", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Derniere maintenance km", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Derniere maintenance hrs", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Date fin garantie", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Fin garantie km", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Fin garantie hrs", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Box(modifier = Modifier.weight(0.5f))
                        }
                    }
                    itemsIndexed(flottes) { index, item ->
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
                            Text(text = item.serie, modifier = Modifier.weight(1f))
                            Text(text = item.unit, modifier = Modifier.weight(1f))
                            Text(text = item.noment, modifier = Modifier.weight(1f))
                            Text(text = item.annee.toString(), modifier = Modifier.weight(1f))
                            Text(text = item.fabricant, modifier = Modifier.weight(1f))
                            Text(text = item.model, modifier = Modifier.weight(1f))
                            Text(text = item.km.toString(), modifier = Modifier.weight(1f))
                            Text(text = item.hrs.toString(), modifier = Modifier.weight(1f))
                            Text(text = item.ecm, modifier = Modifier.weight(1f))
                            Text(text = item.plate, modifier = Modifier.weight(1f))
                            Text(text = item.nextmaintkm.toString(), modifier = Modifier.weight(1f))
                            Text(text = item.nextmainthrs.toString(), modifier = Modifier.weight(1f))
                            Text(text = item.lastmaintkm.toString(), modifier = Modifier.weight(1f))
                            Text(text = item.lastmainthrs.toString(), modifier = Modifier.weight(1f))
                            Text(text = item.fingdate, modifier = Modifier.weight(1f))
                            Text(text = item.fingkm.toString(), modifier = Modifier.weight(1f))
                            Text(text = item.finghrs.toString(), modifier = Modifier.weight(1f))
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
    }

    if (showDialog) {
        val clients by viewModel.clients.collectAsState()
        FlotteUpsertDialog(
            item = selectedItem,
            clients = clients,
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
fun FlotteUpsertDialog(
    item: Flotte?,
    clients: List<Client>,
    onDismiss: () -> Unit,
    onSave: (Flotte) -> Unit
) {
    var serie by remember { mutableStateOf(item?.serie ?: "") }
    var unit by remember { mutableStateOf(item?.unit ?: "") }
    var noment by remember { mutableStateOf(item?.noment ?: "") }
    var annee by remember { mutableStateOf(item?.annee?.toString() ?: "") }
    var fabricant by remember { mutableStateOf(item?.fabricant ?: "") }
    var model by remember { mutableStateOf(item?.model ?: "") }
    var km by remember { mutableStateOf(item?.km?.toString() ?: "") }
    var hrs by remember { mutableStateOf(item?.hrs?.toString() ?: "") }
    var ecm by remember { mutableStateOf(item?.ecm ?: "") }
    var plate by remember { mutableStateOf(item?.plate ?: "") }
    var nextmaintkm by remember { mutableStateOf(item?.nextmaintkm?.toString() ?: "") }
    var nextmainthrs by remember { mutableStateOf(item?.nextmainthrs?.toString() ?: "") }
    var lastmaintkm by remember { mutableStateOf(item?.lastmaintkm?.toString() ?: "") }
    var lastmainthrs by remember { mutableStateOf(item?.lastmainthrs?.toString() ?: "") }
    var fingdate by remember { mutableStateOf(item?.fingdate ?: "") }
    var fingkm by remember { mutableStateOf(item?.fingkm?.toString() ?: "") }
    var finghrs by remember { mutableStateOf(item?.finghrs?.toString() ?: "") }

    var clientMenuExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (item == null) "Créer un item" else "Modifier un item") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = serie,
                    onValueChange = { serie = it },
                    label = { Text("Numero serie VIN") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Numero unité") },
                    modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = noment,
                        onValueChange = { noment = it },
                        label = { Text("Noms entreprise") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = "Select Client",
                                modifier = Modifier.clickable { clientMenuExpanded = true })
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = clientMenuExpanded,
                        onDismissRequest = { clientMenuExpanded = false }
                    ) {
                        clients.forEach {
                            DropdownMenuItem(text = { Text(it.nomentreprise) }, onClick = {
                                noment = it.nomentreprise
                                clientMenuExpanded = false
                            })
                        }
                    }
                }
                OutlinedTextField(
                    value = annee,
                    onValueChange = { annee = it },
                    label = { Text("Année du vehicule") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = fabricant,
                    onValueChange = { fabricant = it },
                    label = { Text("Le fabricant du vehicule") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("Model du vehicule") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = km,
                    onValueChange = { km = it },
                    label = { Text("Kilometrage") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = hrs,
                    onValueChange = { hrs = it },
                    label = { Text("Les heures vehicule") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = ecm,
                    onValueChange = { ecm = it },
                    label = { Text("Password ecm") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = plate,
                    onValueChange = { plate = it },
                    label = { Text("Numero liscence plate") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nextmaintkm,
                    onValueChange = { nextmaintkm = it },
                    label = { Text("Prochaine manitenance km") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nextmainthrs,
                    onValueChange = { nextmainthrs = it },
                    label = { Text("Prochaine manitenance hrs") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = lastmaintkm,
                    onValueChange = { lastmaintkm = it },
                    label = { Text("Derniere maintenance km") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = lastmainthrs,
                    onValueChange = { lastmainthrs = it },
                    label = { Text("Derniere maintenance hrs") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = fingdate,
                    onValueChange = { fingdate = it },
                    label = { Text("Date fin garantie") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = fingkm,
                    onValueChange = { fingkm = it },
                    label = { Text("Fin garantie km") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = finghrs,
                    onValueChange = { finghrs = it },
                    label = { Text("Fin garantie hrs") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedItem = Flotte(
                        id = item?.id ?: 0,
                        serie = serie,
                        unit = unit,
                        noment = noment,
                        annee = annee.toIntOrNull() ?: 0,
                        fabricant = fabricant,
                        model = model,
                        km = km.toIntOrNull() ?: 0,
                        hrs = hrs.toIntOrNull() ?: 0,
                        ecm = ecm,
                        plate = plate,
                        nextmaintkm = nextmaintkm.toIntOrNull() ?: 0,
                        nextmainthrs = nextmainthrs.toIntOrNull() ?: 0,
                        lastmaintkm = lastmaintkm.toIntOrNull() ?: 0,
                        lastmainthrs = lastmainthrs.toIntOrNull() ?: 0,
                        fingdate = fingdate,
                        fingkm = fingkm.toIntOrNull() ?: 0,
                        finghrs = finghrs.toIntOrNull() ?: 0
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
