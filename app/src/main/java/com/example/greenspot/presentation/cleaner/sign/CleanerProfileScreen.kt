package com.example.greenspot.presentation.cleaner.sign

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.greenspot.presentation.sign_in.UserData
import coil.compose.AsyncImage
import com.example.greenspot.R
import com.example.greenspot.navgraph.LoggedCleanerScreens
import com.example.greenspot.navgraph.LoggedSpotterScreens
import com.example.greenspot.presentation.common.GreenspotBottomBar
import com.example.greenspot.presentation.sign_in.SignInResult
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleanerProfileScreen(
    navController: NavHostController,
    onSignOut: () -> Unit
) {
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
                SpotterProfileInfos()
            }
        }
    }
}

@Composable
fun SpotterProfileInfos(
    cleanerProfileScreenViewModel: CleanerProfileScreenViewModel = viewModel()
) {
    val userId = FirebaseAuth.getInstance().currentUser!!.uid
    cleanerProfileScreenViewModel.updateUserData(userId)
    val companyName = cleanerProfileScreenViewModel.cleanerData.value.username
    val profilePictureUrl = cleanerProfileScreenViewModel.cleanerData.value.profilePictureUrl
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

@Composable
fun SpotterInfoCard(){

}

@Preview
@Composable
fun SpotterProfileScreenPreview() {
    CleanerProfileScreen(
        navController = rememberNavController(),
        onSignOut = {}
    )
}


@Preview
@Composable
fun SpotterProfileInfosPreview(){
    SpotterProfileInfos()
}

@Preview
@Composable
fun SpotterInfoCardPreview(){
    SpotterInfoCard()
}