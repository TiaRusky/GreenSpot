package com.example.greenspot.presentation.spotter.addReport

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


@Composable
fun AddReport(
    navController: NavHostController,
    onSignOut: () -> Unit
){

    val context = LocalContext.current
    val file = context.createImageFile()                //The file that will contain the image
    val uri = FileProvider.getUriForFile(               //The URI of the image
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
        }

    //Here is defined the activity that requests the permissions to use the camera and location
    val permissionsLauncher  = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions  ->
        val cameraPermissionGranted = permissions[android.Manifest.permission.CAMERA] ?: false
        val locationPermissionGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false

        if (cameraPermissionGranted && locationPermissionGranted) {
            Log.i("Permission","Granted")
            cameraLauncher.launch(uri)              //Start the camera if the user gave the permissions
        } else {
            Log.i("Permission","Denied")
            Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()     //Show a message when no permission
        }
    }


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
                value = "Capture Image",
                onButtonClicked = {
                    //Checks if there is the oermission to open the camera and access the location
                    val cameraPermissionCheckResult =
                        ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)

                    val locationPermissionCheckResult =
                        ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)

                    if (cameraPermissionCheckResult == PackageManager.PERMISSION_GRANTED
                        && locationPermissionCheckResult == PackageManager.PERMISSION_GRANTED ) {
                        cameraLauncher.launch(uri)
                    } else {        //If no permissions , then ask for them
                        permissionsLauncher.launch(
                            arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        )
                    }
                },
            )
            //Show the captured image
            if (capturedImageUri.path?.isNotEmpty() == true) {
                Image(
                    modifier = Modifier
                        .padding(16.dp, 8.dp),
                    painter = rememberAsyncImagePainter(capturedImageUri),
                    contentDescription = null
                )
            }
        }
    }
}

/**********UTILITIES*********/

//The function used to create the tmp file that hold the image captured via camera
fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,          /* prefix */
        ".jpg",           /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}

