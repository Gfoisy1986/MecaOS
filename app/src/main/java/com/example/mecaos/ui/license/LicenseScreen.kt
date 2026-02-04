package com.example.mecaos.ui.license

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LicenseScreen(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val repoUrl = "https://github.com/Gfoisy1986/MecaOS"
    val licenseBrowserUrl = "https://github.com/Gfoisy1986/MecaOS?tab=GPL-3.0-1-ov-file"
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "MecaOS is open-source software licensed under the GNU General Public License v3.0.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { showDialog = true }) {
            Text("View License on GitHub")
        }
    }

    if (showDialog) {
        val packageManager = context.packageManager
        val isGithubInstalled = try {
            packageManager.getPackageInfo("com.github.android", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Open with...") },
            text = {
                if (isGithubInstalled) {
                    Text("How would you like to open the link?")
                } else {
                    Text("The GitHub app is not installed. Would you like to install it or open the link in your browser?")
                }
            },
            confirmButton = {
                if (isGithubInstalled) {
                    Button(onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repoUrl)).apply {
                            setPackage("com.github.android")
                        }
                        context.startActivity(intent)
                        showDialog = false
                    }) {
                        Text("Open in GitHub App")
                    }
                } else {
                    Button(onClick = {
                        val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.github.android"))
                        context.startActivity(playStoreIntent)
                        showDialog = false
                    }) {
                        Text("Install from Play Store")
                    }
                }
            },
            dismissButton = {
                Button(onClick = {
                    uriHandler.openUri(licenseBrowserUrl)
                    showDialog = false
                }) {
                    Text("Open in Browser")
                }
            }
        )
    }
}
