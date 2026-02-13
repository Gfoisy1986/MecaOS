package com.example.mecaos

import android.content.Intent
import android.content.pm.PackageManager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mecaos.ui.MainViewModel
import com.example.mecaos.ui.ViewModelFactory
import com.example.mecaos.ui.bon.BonsDeTravauxScreen
import com.example.mecaos.ui.bon.BonsDeTravauxViewModel
import com.example.mecaos.ui.client.ClientScreen
import com.example.mecaos.ui.client.ClientViewModel
import com.example.mecaos.ui.employe.EmployeScreen
import com.example.mecaos.ui.employe.EmployeViewModel
import com.example.mecaos.ui.flotte.FlotteScreen
import com.example.mecaos.ui.flotte.FlotteViewModel
import com.example.mecaos.ui.inventaire.InventaireScreen
import com.example.mecaos.ui.inventaire.InventaireViewModel
import com.example.mecaos.ui.job.JobsScreen
import com.example.mecaos.ui.license.LicenseScreen
import com.example.mecaos.ui.support.SupportScreen
import com.example.mecaos.ui.travaux.TravauxScreen
import com.example.mecaos.ui.travaux.TravauxViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier, mainViewModel: MainViewModel = viewModel()) {
    val menuItems = listOf(
        "Acceuil",
        "Inventaire",
        "Employées",
        "Clients",
        "Flottes",
        "Bons Travaux",
        "Jobs",
        "Travaux",
        "Calendrier",
        "Facturation",
        "Comptabilité",
        "Support Technique",
        "GitHub",
        "License"
    )

    if (mainViewModel.showGitHubDialog) {
        GitHubDialog(onDismiss = { mainViewModel.onGitHubDialogVisibilityChanged(false) })
    }

    Scaffold(
        modifier = modifier.fillMaxWidth(),
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    menuItems.forEach { item ->
                        TextButton(
                            onClick = {
                                if (item == "GitHub") {
                                    mainViewModel.onGitHubDialogVisibilityChanged(true)
                                } else {
                                    mainViewModel.onScreenChanged(item)
                                }
                            }
                        ) {
                            Text(item)
                        }
                    }
                }
            }
        },
        containerColor = Color(0xFFADD8E6) // Light Blue
    ) { innerPadding ->
        val application = LocalContext.current.applicationContext as MecaOSApplication

        when (mainViewModel.currentScreen) {
            "Acceuil" -> AcceuilScreen(modifier = Modifier.padding(innerPadding))
            "Inventaire" -> {
                val inventaireViewModel: InventaireViewModel = viewModel(factory = ViewModelFactory(application.container.database))
                InventaireScreen(viewModel = inventaireViewModel, modifier = Modifier.padding(innerPadding))
            }
            "Employées" -> {
                val employeViewModel: EmployeViewModel = viewModel(factory = ViewModelFactory(application.container.database))
                EmployeScreen(viewModel = employeViewModel, modifier = Modifier.padding(innerPadding))
            }
            "Clients" -> {
                val clientViewModel: ClientViewModel = viewModel(factory = ViewModelFactory(application.container.database))
                ClientScreen(viewModel = clientViewModel, modifier = Modifier.padding(innerPadding))
            }
            "Flottes" -> {
                val flotteViewModel: FlotteViewModel = viewModel(factory = ViewModelFactory(application.container.database))
                FlotteScreen(viewModel = flotteViewModel, modifier = Modifier.padding(innerPadding))
            }
            "Bons Travaux" -> {
                val bonsDeTravauxViewModel: BonsDeTravauxViewModel = viewModel(factory = ViewModelFactory(application.container.database))
                BonsDeTravauxScreen(viewModel = bonsDeTravauxViewModel, modifier = Modifier.padding(innerPadding))
            }
            "Jobs" -> {
                JobsScreen(modifier = Modifier.padding(innerPadding))
            }
            "Travaux" -> {
                val travauxViewModel: TravauxViewModel = viewModel(factory = ViewModelFactory(application.container.database))
                TravauxScreen(viewModel = travauxViewModel, modifier = Modifier.padding(innerPadding))
            }
            "Support Technique" -> {
                SupportScreen(modifier = Modifier.padding(innerPadding))
            }
            "License" -> {
                LicenseScreen(modifier = Modifier.padding(innerPadding))
            }
            else -> BlankPage(
                title = mainViewModel.currentScreen,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun GitHubDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val repoUrl = "https://github.com/Gfoisy1986/MecaOS"

    val packageManager = context.packageManager
    val isGithubInstalled = try {
        packageManager.getPackageInfo("com.github.android", PackageManager.GET_ACTIVITIES)
        true
    } catch (_: PackageManager.NameNotFoundException) {
        false
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Open GitHub Repository") },
        text = { Text("How would you like to open the repository?") },
        confirmButton = {
            if (isGithubInstalled) {
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, repoUrl.toUri()).apply {
                        setPackage("com.github.android")
                    }
                    context.startActivity(intent)
                    onDismiss()
                }) {
                    Text("Open in GitHub App")
                }
            } else {
                Button(onClick = {
                    val playStoreIntent = Intent(Intent.ACTION_VIEW, "market://details?id=com.github.android".toUri())
                    context.startActivity(playStoreIntent)
                    onDismiss()
                }) {
                    Text("Install from Play Store")
                }
            }
        },
        dismissButton = {
            Button(onClick = {
                uriHandler.openUri(repoUrl)
                onDismiss()
            }) {
                Text("Open in Browser")
            }
        }
    )
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
    MaterialTheme {
        MainScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun AcceuilScreenPreview() {
    MaterialTheme {
        AcceuilScreen()
    }
}
