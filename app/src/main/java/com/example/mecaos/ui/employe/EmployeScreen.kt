package com.example.mecaos.ui.employe

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.mecaos.data.Employe

@Composable
fun EmployeScreen(viewModel: EmployeViewModel, modifier: Modifier = Modifier) {
    val employes by viewModel.employes.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<Employe?>(null) }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            if (employes.isNotEmpty()) {
                FloatingActionButton(onClick = {
                    selectedItem = null
                    showDialog = true
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Créer un item")
                }
            }
        }
    ) { paddingValues ->
        if (employes.isEmpty()) {
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
                            Text(text = "ID", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
                            Text(text = "Nom", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Email", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Cellulaire", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Addresse", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Date naissance", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Liscense mécanicien", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Numero PEP", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Numero SAAQ", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "Ref Permis Conduire", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(text = "ID Permis Conduire", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Box(modifier = Modifier.weight(0.5f))
                        }
                    }
                    itemsIndexed(employes) { index, item ->
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
                            Text(text = item.nom, modifier = Modifier.weight(1f))
                            Text(text = item.email, modifier = Modifier.weight(1f))
                            Text(text = item.cellulaire, modifier = Modifier.weight(1f))
                            Text(text = item.addresse, modifier = Modifier.weight(1f))
                            Text(text = item.dateNaissance, modifier = Modifier.weight(1f))
                            Text(text = item.liscenseMecanicien, modifier = Modifier.weight(1f))
                            Text(text = item.numeropep, modifier = Modifier.weight(1f))
                            Text(text = item.numerosaaq, modifier = Modifier.weight(1f))
                            Text(text = item.refpermisconduire, modifier = Modifier.weight(1f))
                            Text(text = item.idpermisconduire, modifier = Modifier.weight(1f))
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
        EmployeUpsertDialog(
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
fun EmployeUpsertDialog(
    item: Employe?,
    onDismiss: () -> Unit,
    onSave: (Employe) -> Unit
) {
    var nom by remember { mutableStateOf(item?.nom ?: "") }
    var email by remember { mutableStateOf(item?.email ?: "") }
    var cellulaire by remember { mutableStateOf(item?.cellulaire ?: "") }
    var addresse by remember { mutableStateOf(item?.addresse ?: "") }
    var dateNaissance by remember { mutableStateOf(item?.dateNaissance ?: "") }
    var liscenseMecanicien by remember { mutableStateOf(item?.liscenseMecanicien ?: "") }
    var numeropep by remember { mutableStateOf(item?.numeropep ?: "") }
    var numerosaaq by remember { mutableStateOf(item?.numerosaaq ?: "") }
    var refpermisconduire by remember { mutableStateOf(item?.refpermisconduire ?: "") }
    var idpermisconduire by remember { mutableStateOf(item?.idpermisconduire ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (item == null) "Créer un item" else "Modifier un item") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = nom,
                    onValueChange = { nom = it },
                    label = { Text("Nom") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cellulaire,
                    onValueChange = { cellulaire = it },
                    label = { Text("Cellulaire") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = addresse,
                    onValueChange = { addresse = it },
                    label = { Text("Addresse") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dateNaissance,
                    onValueChange = { dateNaissance = it },
                    label = { Text("Date naissance") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = liscenseMecanicien,
                    onValueChange = { liscenseMecanicien = it },
                    label = { Text("Liscense mécanicien") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = numeropep,
                    onValueChange = { numeropep = it },
                    label = { Text("Numero PEP") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = numerosaaq,
                    onValueChange = { numerosaaq = it },
                    label = { Text("Numero SAAQ") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = refpermisconduire,
                    onValueChange = { refpermisconduire = it },
                    label = { Text("Ref Permis Conduire") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = idpermisconduire,
                    onValueChange = { idpermisconduire = it },
                    label = { Text("ID Permis Conduire") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedItem = Employe(
                        id = item?.id ?: 0,
                        nom = nom,
                        email = email,
                        cellulaire = cellulaire,
                        addresse = addresse,
                        dateNaissance = dateNaissance,
                        liscenseMecanicien = liscenseMecanicien,
                        numeropep = numeropep,
                        numerosaaq = numerosaaq,
                        refpermisconduire = refpermisconduire,
                        idpermisconduire = idpermisconduire
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
