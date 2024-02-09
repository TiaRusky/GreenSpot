package com.example.greenspot.presentation.spotter


import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.greenspot.presentation.sign_in.UserData
import coil.compose.AsyncImage
import com.example.greenspot.R
import com.example.greenspot.navgraph.LoggedSpotterScreens
import com.example.greenspot.presentation.common.GreenspotBottomBar
import com.example.greenspot.presentation.spotter.CameraScreen
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import java.io.File
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotterProfileScreen(
    navController: NavHostController,
    userData: UserData?,
    onSignOut: () -> Unit,
    spotterReportsViewModel: SpotterReportsViewModel = viewModel()
) {
    val spotterReportsUiState by spotterReportsViewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            GreenspotBottomBar(
                navController = navController,
                selectedScreen = LoggedSpotterScreens.MyHome.title,
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
            SpotterProfileInfos(
                userData = userData
            )

            SpotterProfileData(
                made = spotterReportsUiState.reportsMade,
                resolved = spotterReportsUiState.resolvedReports
            )

        }
    }
}

@Composable
fun SpotterProfileInfos(
    userData: UserData?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(12.dp)
    ) {
        if (userData?.profilePictureUrl != null) {
            AsyncImage(
                model = userData.profilePictureUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        else{   //Print a standard image if no one is found in the account
            Image(
                painter = painterResource(id = R.drawable.no_image_user),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        if (userData?.username != null) {
            Text(
                text = userData.username,
                textAlign = TextAlign.Center,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth()
            )
        }
    }
}

//Where will be inserted the info about the profile's activities in the app
@Composable
fun SpotterProfileData(made:Int,resolved:Int){

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                //.background()
                .padding(28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                HeadTextComponent(value = "Activity Recap")

                GridItem(text = "Token", number = 0, painterResource = painterResource(id = R.drawable.token))
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                GridItem(text = "Reports made", number = made, painterResource = painterResource(id = R.drawable.ic_reports_made))
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                GridItem(text = "Resolved reports", number = resolved, painterResource = painterResource(id = R.drawable.ic_resolved_report))
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )
                ShakePhoneIconComponent(painterResource = painterResource(id = R.drawable.ic_shake_phone))
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                Shake()
            }
        }
    }

}



@Composable
fun Shake() {

    val context = LocalContext.current //for the current context
    var manager: SensorManager
    var sensor: Sensor
    var detector: ShakeDetector

    val p = remember { Prova(context) } //use as a test to stamp something to see if the shake work

    manager = remember(context) {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!

    detector = ShakeDetector()
    detector.setOnShakeListener { shakesCount ->
        if (shakesCount == 3) {
            //TODO: andare in AddReportScreen
            p.stampa() //test
        }
    }

    manager.registerListener(detector, sensor, SensorManager.SENSOR_DELAY_UI)
}

@Composable
private fun MainContent(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit
) {

    if (hasPermission) {
        CameraScreen()
    } else {
        NoPermissionScreen(onRequestPermission)
    }
}


@Preview
@Composable
fun SpotterProfileScreenPreview() {
    SpotterProfileScreen(
        navController = rememberNavController(),
        onSignOut = {},
        userData = UserData(
            userId = "Preview_UserID",
            username = "Preview_Username",
            profilePictureUrl = null
        ),
    )
}


@Preview
@Composable
fun SpotterProfileInfosPreview(){
    SpotterProfileInfos(
        userData = UserData(
            userId = "Preview_UserID",
            username = "Preview_Username",
            profilePictureUrl = null
        )
    )
}


@Preview
@Composable
fun SpotterProfileDataPreview(){
    SpotterProfileData(0,0)
}