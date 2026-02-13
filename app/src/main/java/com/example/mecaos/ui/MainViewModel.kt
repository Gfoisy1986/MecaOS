package com.example.mecaos.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var currentScreen by mutableStateOf("Acceuil")
        private set

    fun onScreenChanged(screen: String) {
        currentScreen = screen
    }

    var showGitHubDialog by mutableStateOf(false)

    fun onGitHubDialogVisibilityChanged(show: Boolean) {
        showGitHubDialog = show
    }
}
