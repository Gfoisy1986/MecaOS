package com.example.mecaos.ui.inventaire

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.example.mecaos.data.Inventaire

@Composable
fun InventaireScreen(viewModel: InventaireViewModel, modifier: Modifier = Modifier) {
    val inventaire by viewModel.inventaire.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<Inventaire?>(null) }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            if (inventaire.isNotEmpty()) {
                FloatingActionButton(onClick = {
                    selectedItem = null
                    showDialog = true
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Créer un item")
                }
            }
        }
    ) { paddingValues ->
        if (inventaire.isEmpty()) {
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
                LazyColumn(
                    modifier = Modifier.width(1000.dp),
                    contentPadding = PaddingValues(bottom = 80.dp, end = 16.dp) // Added padding for FAB
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Nom", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Quantité", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Prix", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Box(modifier = Modifier.weight(0.5f))
                        }
                    }
                    itemsIndexed(inventaire) { index, item ->
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
                            Text(text = item.nom, modifier = Modifier.weight(1f))
                            Text(text = item.quantite.toString(), modifier = Modifier.weight(1f))
                            Text(text = "$${item.prix}", modifier = Modifier.weight(1f))
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
        InventaireUpsertDialog(
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

@Composable
fun InventaireUpsertDialog(
    item: Inventaire?,
    onDismiss: () -> Unit,
    onSave: (Inventaire) -> Unit
) {
    var nom by remember { mutableStateOf(item?.nom ?: "") }
    var quantite by remember { mutableStateOf(item?.quantite?.toString() ?: "") }
    var prix by remember { mutableStateOf(item?.prix?.toString() ?: "") }

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
                    value = nom,
                    onValueChange = { nom = it },
                    label = { Text("Nom") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = quantite,
                    onValueChange = { quantite = it },
                    label = { Text("Quantité") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = prix,
                    onValueChange = { prix = it },
                    label = { Text("Prix") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedItem = Inventaire(
                        id = item?.id ?: 0,
                        nom = nom,
                        quantite = quantite.toIntOrNull() ?: 0,
                        prix = prix.toDoubleOrNull() ?: 0.0
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
