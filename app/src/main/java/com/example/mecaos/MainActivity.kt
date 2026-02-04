package com.example.mecaos

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mecaos.ui.ViewModelFactory
import com.example.mecaos.ui.client.ClientScreen
import com.example.mecaos.ui.client.ClientViewModel
import com.example.mecaos.ui.employe.EmployeScreen
import com.example.mecaos.ui.employe.EmployeViewModel
import com.example.mecaos.ui.flotte.FlotteScreen
import com.example.mecaos.ui.flotte.FlotteViewModel
import com.example.mecaos.ui.inventaire.InventaireScreen
import com.example.mecaos.ui.inventaire.InventaireViewModel
import com.example.mecaos.ui.theme.MecaOSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        enableEdgeToEdge()
        setContent {
            MecaOSTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var menuExpanded by remember { mutableStateOf(false) }
    val menuItems = listOf(
        "Acceuil",
        "Inventaire",
        "Employées",
        "Clients",
        "Flottes",
        "Bons Travaux",
        "Travaux",
        "Calendrier",
        "Facturation",
        "Comptabilité",
        "Support Technique"
    )
    var currentScreen by remember { mutableStateOf("Acceuil") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("MecaOS") },
                navigationIcon = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.menu_icon),
                                contentDescription = "Menu"
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            menuItems.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        currentScreen = item
                                        menuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 8.dp
            ) {
                Text(
                    text = "© 2025 Guillaume Foisy",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        },
        containerColor = Color(0xFFADD8E6) // Light Blue
    ) { innerPadding ->
        val application = if (!LocalInspectionMode.current) {
            (androidx.compose.ui.platform.LocalContext.current.applicationContext as MecaOSApplication)
        } else {
            null
        }

        when (currentScreen) {
            "Acceuil" -> AcceuilScreen(modifier = Modifier.padding(innerPadding))
            "Inventaire" -> {
                if (application != null) {
                    val inventaireViewModel: InventaireViewModel = viewModel(factory = ViewModelFactory(application.database))
                    InventaireScreen(viewModel = inventaireViewModel, modifier = Modifier.padding(innerPadding))
                } else {
                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Inventaire screen is not available in preview.")
                    }
                }
            }
            "Employées" -> {
                if (application != null) {
                    val employeViewModel: EmployeViewModel = viewModel(factory = ViewModelFactory(application.database))
                    EmployeScreen(viewModel = employeViewModel, modifier = Modifier.padding(innerPadding))
                } else {
                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Employées screen is not available in preview.")
                    }
                }
            }
            "Clients" -> {
                if (application != null) {
                    val clientViewModel: ClientViewModel = viewModel(factory = ViewModelFactory(application.database))
                    ClientScreen(viewModel = clientViewModel, modifier = Modifier.padding(innerPadding))
                } else {
                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Clients screen is not available in preview.")
                    }
                }
            }
            "Flottes" -> {
                if (application != null) {
                    val flotteViewModel: FlotteViewModel = viewModel(factory = ViewModelFactory(application.database))
                    FlotteScreen(viewModel = flotteViewModel, modifier = Modifier.padding(innerPadding))
                } else {
                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Flottes screen is not available in preview.")
                    }
                }
            }
            else -> BlankPage(
                title = currentScreen,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun AcceuilScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bienvenue!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Nouvelles", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "// TODO: La liste des nouvelles sera diffusée ici depuis un backend Linode.",
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun BlankPage(title: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = title
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MecaOSTheme {
        MainScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun AcceuilScreenPreview() {
    MecaOSTheme {
        AcceuilScreen()
    }
}
