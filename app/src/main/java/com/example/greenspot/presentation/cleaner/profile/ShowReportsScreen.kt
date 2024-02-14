package com.example.greenspot.presentation.cleaner.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.greenspot.navgraph.LoggedCleanerScreens

import com.example.greenspot.presentation.common.GreenspotBottomBar
import com.example.greenspot.presentation.spotter.reports.ListScreen
import com.example.greenspot.presentation.spotter.reports.createDummyAddress

@Composable
fun ShowReportScreen(
    navController: NavHostController,
    onSignOut: () -> Unit,
    cleanerViewModel : CleanerDataViewModel = viewModel(),
) {

    val listItems by cleanerViewModel.listItems.collectAsState()        //The reports loaded

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
            SearchBar(onSearch = {city->
                cleanerViewModel.searchReports(city)
            })

            ListScreen(
                modifier = Modifier.padding(innerPadding),
                listItems = listItems,
                {}
            )
        }
    }
}


@Composable
fun SearchBar(
    onSearch: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var isSearching by remember { mutableStateOf(false) }

        BasicTextField(
            value = searchText,
            onValueChange = { newValue ->
                searchText = newValue
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
        )

        Button(
            onClick = {
                isSearching = !isSearching
                if (!isSearching) {
                    onSearch(searchText)
                }
            },

        ) {
            Text(text = "Search")
        }
    }

}

@Preview
@Composable
fun ShowReportScreenPreview() {
    CleanerProfileScreen(
        navController = rememberNavController(),
        onSignOut = {},
    )
}