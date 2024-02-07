package com.example.greenspot.presentation.cleaner.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.greenspot.navgraph.LoggedCleanerScreens

import com.example.greenspot.presentation.common.GreenspotBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowReportScreen(
    navController: NavHostController,
    onSignOut: () -> Unit,
){
    Scaffold(
        bottomBar = {
            GreenspotBottomBar(
                navController = navController,
                selectedScreen = LoggedCleanerScreens.ShowReports.title,
                onSignOut = onSignOut,
                isSpotter = false
            )
        },

        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text("Show Reports Cleaners")
        }
    }
}


@Preview
@Composable
fun ShowReportScreenPreview(){
    CleanerProfileScreen(
        navController = rememberNavController(),
        onSignOut = {},
        )
}