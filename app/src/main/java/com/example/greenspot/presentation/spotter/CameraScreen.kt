package com.example.greenspot.presentation.spotter

import android.widget.LinearLayout
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material3.ExperimentalMaterial3Api
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun CameraScreen(
    cameraViewModel: CameraViewModel = viewModel()
) {
    CameraContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraContent(){
    //implement the controller to control the camera
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember {
        LifecycleCameraController(context)
    }

    //implement camera functionality
    Scaffold (
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = { //button per scattare la foto
            ExtendedFloatingActionButton(
                text = { Text(text = "take photo") },
                icon = { /*TODO*/ },
                onClick = {
                    val mainExecutor = ContextCompat.getMainExecutor(context) //esecutore per lo scatto della foto
                    cameraController.takePicture(mainExecutor, object: ImageCapture.OnImageCapturedCallback(){ //il controller eseguirà lo scatto della foto e acquisiremo la foto con object
                        override fun onCaptureSuccess(image: ImageProxy) {
                            super.onCaptureSuccess(image)
                        }
                    })
                }
            )
        }
    ){ paddingValues: PaddingValues ->
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            factory = { context ->
            PreviewView(context).apply {// in modo che l'anteprima si adatta allo schermo
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                //setBackgroundColor(Color.Black) //colore dell'anteprima quando la fotocamera si sta aprendo
                scaleType = PreviewView.ScaleType.FILL_START //per il ridimensionamento della fotocamera su diversi devices
            }.also { previewView ->  //usato per accedere allo screen della camera
                previewView.controller = cameraController
                cameraController.bindToLifecycle(lifecycleOwner) //il controller si lega al lifecycleOwner, se il lifecycleOwner è distrutto -> anche la connessione al controller del camera è distrutto
            }
        })
    }
}