package com.example.greenspot.presentation.spotter


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.greenspot.navgraph.LoggedSpotterScreens
import com.example.greenspot.presentation.common.GreenspotBottomBar
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun SpotterReports(
    navController: NavHostController,
    onSignOut: () -> Unit,
    spotterViewModel: ReportsViewModel = viewModel()
) {
    val listItems by spotterViewModel.listItems.collectAsState()
    val lastVisibleItem by spotterViewModel.lastVisibleItem.collectAsState()
    val isLoading by spotterViewModel.isLoading.collectAsState()

    val listState = rememberLazyListState()     //It is used to check if the user scrolled to the end of the list

    // The function to call when the user reach the end of the list (so new data need to be loaded)
    val loadMoreItems: () -> Unit = {
        if (!isLoading && listState.isScrolledToTheEnd()) {
            spotterViewModel.loadNextPage()
        }
    }


    Scaffold(
        bottomBar = {
            GreenspotBottomBar(
                navController = navController,
                selectedScreen = LoggedSpotterScreens.MyReports.title,
                onSignOut = onSignOut,
                isSpotter = true
            )
        },

        modifier = Modifier
            .fillMaxSize(),
    ) { innerPadding ->

        ListScreen(
            modifier = Modifier.padding(innerPadding),
            listItems = listItems,
            onItemClick = { clickedItem ->
                // Aggiungi qui la logica per gestire il clic sull'elemento della lista
            },
            onLoadMoreItems = loadMoreItems
        )

    }
}


@Composable
fun ListItem(
    listItemData: ListItemData,
    onItemClick: (ListItemData) -> Unit
) {
    var showImage by remember { mutableStateOf(false) }     //It tells if the item has been clicked or not


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /*onItemClick(listItemData)*/ showImage = !showImage }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = if (listItemData.validated) Icons.Default.CheckCircle else Icons.Default.Circle,
            contentDescription = null,
            tint = if (listItemData.validated) MaterialTheme.colorScheme.primary else Color.Gray,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp)) // Spazio tra l'icona e il testo


        Text(
            text = listItemData.date,
            modifier = Modifier.weight(1f)
        )

        /*
        Text(
            text = listItemData.location,
            modifier = Modifier.padding(end = 16.dp)
        )
        */

        if (showImage) {
            val painter = rememberAsyncImagePainter(
                model = listItemData.imageUrl
            )
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun ListScreen(
    modifier: Modifier,
    listItems: List<ListItemData>,
    onItemClick: (ListItemData) -> Unit,
    onLoadMoreItems: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(items = listItems) { listItem ->
            ListItem(
                listItemData = listItem,
                onItemClick = onItemClick
            )
            Divider() // Aggiungi una linea divisoria tra gli elementi della lista

            if (listItem == listItems.lastOrNull()) {
                onLoadMoreItems()
            }
        }
    }
}

@Preview
@Composable
fun SpotterReportsPreview() {
    SpotterReports(
        navController = rememberNavController(),
        onSignOut = {}
    )
}


/*--------UTILITIES----------*/

//This function is used to verify if the user reached the end of the list
fun LazyListState.isScrolledToTheEnd(): Boolean {
    val totalItems = layoutInfo.totalItemsCount
    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
    return totalItems > 0 && lastVisibleItem >= totalItems - 1
}