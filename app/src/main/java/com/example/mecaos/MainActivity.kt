package com.example.mecaos

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.mecaos.ui.support.SupportScreen
import com.example.mecaos.ui.theme.MecaOSTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    var bottomBarVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxWidth(),
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
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(80.dp) // Allocate fixed space for the bottom bar
            ) {
                AnimatedVisibility(
                    visible = bottomBarVisible,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        tonalElevation = 8.dp
                    ) {
                        Text(
                            text = "This is free and unencumbered software released into the public domain.\n\nAnyone is free to copy, modify, publish, use, compile, sell, or distribute this software, either in source code form or as a compiled binary, for any purpose, commercial or non-commercial, and by any means.\n\nIn jurisdictions that recognize copyright laws, the author or authors of this software dedicate any and all copyright interest in the software to the public domain. We make this dedication for the benefit of the public at large and to the detriment of our heirs and successors. We intend this dedication to be an overt act of relinquishment in perpetuity of all present and future rights to this software under copyright law.\n\nTHE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\n\nFor more information, please refer to <http://unlicense.org/>",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                if (!bottomBarVisible) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            bottomBarVisible = true
                            coroutineScope.launch {
                                delay(3000L) // Hide after 3 seconds
                                bottomBarVisible = false
                            }
                        }
                    )
                }
            }
        },
        containerColor = Color(0xFFADD8E6) // Light Blue
    ) { innerPadding ->
        val application = LocalContext.current.applicationContext as MecaOSApplication

        when (currentScreen) {
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
            "Support Technique" -> {
                SupportScreen(modifier = Modifier.padding(innerPadding))
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