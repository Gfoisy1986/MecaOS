package com.example.mecaos.ui.client

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

@Composable
fun ClientScreen(viewModel: ClientViewModel, modifier: Modifier = Modifier) {
    val clients by viewModel.clients.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<Client?>(null) }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            if (clients.isNotEmpty()) {
                FloatingActionButton(onClick = {
                    selectedItem = null
                    showDialog = true
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Créer un item")
                }
            }
        }
    ) { paddingValues ->
        if (clients.isEmpty()) {
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
                            Text(text = "Noms entreprise", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Noms du proprietaire", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Noms du responsable", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Entreprise addresse complete", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Entreprise telephone", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Entreprise email", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Fax number", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Taux horraire", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Box(modifier = Modifier.weight(0.5f))
                        }
                    }
                    itemsIndexed(clients) { index, item ->
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
                            Text(text = item.nomentreprise, modifier = Modifier.weight(1f))
                            Text(text = item.nomproprietaire, modifier = Modifier.weight(1f))
                            Text(text = item.nomresponsable, modifier = Modifier.weight(1f))
                            Text(text = item.addresse, modifier = Modifier.weight(1f))
                            Text(text = item.telephone, modifier = Modifier.weight(1f))
                            Text(text = item.email, modifier = Modifier.weight(1f))
                            Text(text = item.fax, modifier = Modifier.weight(1f))
                            Text(text = item.tauxhx.toString(), modifier = Modifier.weight(1f))
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
        ClientUpsertDialog(
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
fun ClientUpsertDialog(
    item: Client?,
    onDismiss: () -> Unit,
    onSave: (Client) -> Unit
) {
    var nomentreprise by remember { mutableStateOf(item?.nomentreprise ?: "") }
    var nomproprietaire by remember { mutableStateOf(item?.nomproprietaire ?: "") }
    var nomresponsable by remember { mutableStateOf(item?.nomresponsable ?: "") }
    var addresse by remember { mutableStateOf(item?.addresse ?: "") }
    var telephone by remember { mutableStateOf(item?.telephone ?: "") }
    var email by remember { mutableStateOf(item?.email ?: "") }
    var fax by remember { mutableStateOf(item?.fax ?: "") }
    var tauxhx by remember { mutableStateOf(item?.tauxhx?.toString() ?: "") }

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
                    value = nomentreprise,
                    onValueChange = { nomentreprise = it },
                    label = { Text("Noms entreprise") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nomproprietaire,
                    onValueChange = { nomproprietaire = it },
                    label = { Text("Noms du proprietaire") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nomresponsable,
                    onValueChange = { nomresponsable = it },
                    label = { Text("Noms du responsable") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = addresse,
                    onValueChange = { addresse = it },
                    label = { Text("Entreprise addresse complete") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = telephone,
                    onValueChange = { telephone = it },
                    label = { Text("Entreprise telephone") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Entreprise email") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = fax,
                    onValueChange = { fax = it },
                    label = { Text("Fax number") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = tauxhx,
                    onValueChange = { tauxhx = it },
                    label = { Text("Taux horraire") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedItem = Client(
                        id = item?.id ?: 0,
                        nomentreprise = nomentreprise,
                        nomproprietaire = nomproprietaire,
                        nomresponsable = nomresponsable,
                        addresse = addresse,
                        telephone = telephone,
                        email = email,
                        fax = fax,
                        tauxhx = tauxhx.toDoubleOrNull() ?: 0.0
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
