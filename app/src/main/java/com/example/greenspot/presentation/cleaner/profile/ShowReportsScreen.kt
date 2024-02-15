package com.example.greenspot.presentation.cleaner.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.greenspot.R
import com.example.greenspot.navgraph.LoggedCleanerScreens

import com.example.greenspot.presentation.common.GreenspotBottomBar
import com.example.greenspot.presentation.spotter.reports.ListScreen
import com.example.greenspot.presentation.spotter.reports.createDummyAddress
import kotlinx.coroutines.launch

@Composable
fun ShowReportScreen(
    navController: NavHostController,
    onSignOut: () -> Unit,
    cleanerViewModel: CleanerDataViewModel = viewModel(),
) {
    val context = LocalContext.current

    val listItems by cleanerViewModel.listItems.collectAsState()        //The reports loaded

    val coroutineScope = rememberCoroutineScope()

    //Toast message for when the report is cleaned
    val onValidated: () -> Unit = {
        coroutineScope.launch {
            Toast.makeText(
                context,
                "Report validated correctly",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    //The function needed to resolve a report
    val resolveReport: (String) -> Unit = { reportId ->
        cleanerViewModel.resolveReport(reportId, onValidated)
    }


    //The function to execute when no reports are found
    val onNoReports: () -> Unit = {
        coroutineScope.launch {
            Toast.makeText(
                context,
                "No Reports for this city",
                Toast.LENGTH_LONG
            ).show()
        }
    }

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
            SearchBar(onSearch = { city ->
                cleanerViewModel.searchReports(city, onNoReports)
            })

            ListScreen(
                modifier = Modifier.padding(innerPadding),
                listItems = listItems,
                {},
                isSpotter = false,
                resolveReport = { reportId ->
                    resolveReport(reportId)
                }
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

        TextField(
            value = searchText,
            onValueChange = { newValue ->
                searchText = newValue
            },
            placeholder = {
                Text(text = "Search here...")
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.tertiary),
            modifier = Modifier
                .height(52.dp)
                .weight(1f)
                .padding(end = 8.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
        )


        SearchButtonComponent(
            value = "Search",
            onButtonClicked = {
                onSearch(searchText)
            },
            painterResource = painterResource(id = R.drawable.ic_search)
        )
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