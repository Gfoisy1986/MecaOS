package com.example.mecaos.ui.pep

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mecaos.MecaOSApplication
import com.example.mecaos.ui.ViewModelFactory

@Composable
fun PepScreen(modifier: Modifier = Modifier) {
    val application = LocalContext.current.applicationContext as MecaOSApplication
    val viewModel: PepViewModel = viewModel(factory = ViewModelFactory(application.container.database))
    val pdfBitmap by viewModel.pdfBitmap.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        pdfBitmap?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = "PEP Form")
        }
    }
}
