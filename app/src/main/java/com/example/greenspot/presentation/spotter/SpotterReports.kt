package com.example.greenspot.presentation.spotter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.greenspot.presentation.common.GreenspotBottomBar
import com.example.greenspot.presentation.common.SpotterScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotterReports(
    changeScreen : (String) -> Unit
){

    Scaffold(
        bottomBar = {
            GreenspotBottomBar(selectedScreen = SpotterScreens.MyReports.title)
        },

        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ){
            item{
                Text(text = "AAAAA")
            }
        }
    }
}

@Preview
@Composable
fun SpotterReportsPreview(){
    SpotterReports({})
}
