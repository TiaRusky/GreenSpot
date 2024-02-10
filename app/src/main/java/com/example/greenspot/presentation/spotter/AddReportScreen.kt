package com.example.greenspot.presentation.spotter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.greenspot.navgraph.LoggedSpotterScreens
import com.example.greenspot.presentation.cleaner.sign.SignupCleanerUIEvent
import com.example.greenspot.presentation.common.GreenspotBottomBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddReport(
    navController: NavHostController,
    onSignOut: () -> Unit
){
    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    Scaffold(
        bottomBar = {
            GreenspotBottomBar(
                navController = navController,
                selectedScreen = "",
                onSignOut = onSignOut,
                isSpotter = true
            )
        },

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
