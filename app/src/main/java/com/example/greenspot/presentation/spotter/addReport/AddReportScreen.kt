package com.example.greenspot.presentation.spotter.addReport

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.greenspot.presentation.spotter.camera.CameraScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddReport(
    navController: NavHostController,
    onSignOut: () -> Unit
){
    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            Log.i("Permission","Granted")
        } else {
            Log.i("Permission","Denied")
        }
    }

    /*
    In case of multiple permissions:
    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val allPermissionsGranted = permissions.all { it.value }
        if (allPermissionsGranted) {
            // Tutti i permessi sono stati concessi, esegui l'azione desiderata
        } else {
            // Almeno un permesso è stato negato, gestisci di conseguenza
        }
    }

    Button(onClick = { requestPermissionLauncher.launch(
        arrayOf(
            android.Manifest.permission.CAMERA,
             Aggiungi altri permessi qui
        )
    )})
     */

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            AddReportTextComponent("Add a new report")
            CaptionComponent("Add caption...")
            InsertPhotoButton(
                value = "Insert Photo",
                onButtonClicked = {
                    //TODO: funzione che attiva la camera
                    requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                    /*PermissionCamera(
                        hasPermission = cameraPermissionState.status.isGranted,
                        onRequestPermission = cameraPermissionState::launchPermissionRequest
                    )*/
                },
            )
        }
    }
}

@Composable
private fun PermissionCamera(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit
) {

    if (hasPermission) {
        CameraScreen()
    } else {
        NoPermissionScreen(onRequestPermission)
    }
}
