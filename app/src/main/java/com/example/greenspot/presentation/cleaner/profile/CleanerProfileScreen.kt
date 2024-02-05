package com.example.greenspot.presentation.cleaner.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import com.example.greenspot.navgraph.LoggedCleanerScreens
import com.example.greenspot.presentation.cleaner.sign.CleanerData
import com.example.greenspot.presentation.cleaner.sign.CleanerProfileScreenViewModel

import com.example.greenspot.presentation.common.GreenspotBottomBar
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleanerProfileScreen(
    navController: NavHostController,
    onSignOut: () -> Unit,
    cleanerProfileViewModel: CleanerProfileScreenViewModel = viewModel()
) {
    //val cleanerProfileUiState by cleanerProfileViewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            GreenspotBottomBar(
                navController = navController,
                selectedScreen = LoggedCleanerScreens.MyHome.title,
                onSignOut = onSignOut
            )
        },

        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            item {
                SpotterProfileInfos(
                    CleanerData()
                )
            }
        }
    }
}

@Composable
fun SpotterProfileInfos(
    cleanerData: CleanerData
) {
    val companyName = cleanerData.username
    val profilePictureUrl = cleanerData.profilePictureUrl
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(12.dp)
    ) {
        if (profilePictureUrl != null) {
            AsyncImage(
                model = profilePictureUrl,
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

        if (companyName != null) {
            Text(
                text = companyName,
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
fun SpotterProfileData(){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp, bottom = 100.dp),
        //modifier = Modifier.fillMaxHeight(),
    ){
        item {
            Text(text = "AAAAAAAAAAA")
        }
    }
}


@Preview
@Composable
fun SpotterProfileScreenPreview() {
    CleanerProfileScreen(
        navController = rememberNavController(),
        onSignOut = {},

    )
}


@Preview
@Composable
fun SpotterProfileInfosPreview(){
    SpotterProfileInfos(
        cleanerData = CleanerData(
            userId = "1",
            username = "TestUsername",
            email = "TestEmail",
            profilePictureUrl = null
        )
    )
}
