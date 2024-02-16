package com.example.greenspot.presentation.spotter


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.greenspot.R
import com.example.greenspot.navgraph.LoggedSpotterScreens
import com.example.greenspot.presentation.common.GreenspotBottomBar
import com.example.greenspot.presentation.sign_in.UserData


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
            .background(MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SpotterProfileInfos(
                userData = userData
            )

            SpotterProfileData(
                navController = navController,
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
fun SpotterProfileData(navController: NavHostController,made:Int,resolved:Int){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background),
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
                Shake(navController = navController)
            }
        }
    }

}



@Composable
fun Shake(navController: NavHostController) {

    val context = LocalContext.current //for the current context
    var manager: SensorManager
    var sensor: Sensor
    var detector: ShakeDetector

    val p = remember { Prova(context) } //use as a test to stamp something to see if the shake work

    manager = remember(context) {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
    detector = remember { ShakeDetector() }


    DisposableEffect(key1 = detector) {
        detector.setOnShakeListener { shakesCount ->
            if (shakesCount == 1) {
                navController.navigate(LoggedSpotterScreens.NewReport.route)
            }
        }
        manager.registerListener(detector, sensor, SensorManager.SENSOR_DELAY_UI)

        onDispose {
            manager.unregisterListener(detector)
        }
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
    SpotterProfileData(navController = rememberNavController(),0,0)
}