package com.example.greenspot.presentation.spotter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.greenspot.presentation.sign_in.UserData
import coil.compose.AsyncImage
import com.example.greenspot.R
import com.example.greenspot.navgraph.LoggedSpotterScreens
import com.example.greenspot.presentation.common.GreenspotBottomBar


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
                onSignOut = onSignOut
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
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(50.dp),
        horizontalArrangement = Arrangement.spacedBy(60.dp),
        modifier = Modifier.padding(16.dp)

    ){
        item{
            GridItem(text = "Token", number = 0)
        }

        item{
            GridItem(text = "Reports made", number = made)
        }

        item{
            GridItem(text = "Resolved reports", number = resolved)
        }
    }
}
@Composable
fun GridItem(text: String, number: Int) {
    Box(
        modifier = Modifier
            .height(140.dp)
            .width(160.dp)
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(25.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "$text: $number", fontSize = 16.sp )

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
fun GridItemPreview(){
    GridItem("Cisanini",1)
}

@Preview
@Composable
fun SpotterProfileDataPreview(){
    SpotterProfileData(0,0)
}