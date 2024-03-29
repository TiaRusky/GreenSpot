package com.example.greenspot.presentation.spotter.reports


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.greenspot.navgraph.LoggedSpotterScreens
import com.example.greenspot.presentation.common.GreenspotBottomBar
import com.google.firebase.firestore.GeoPoint
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Calendar
import java.util.Locale


@Composable
fun SpotterReports(
    navController: NavHostController,
    onSignOut: () -> Unit,
    spotterViewModel: ReportsViewModel = viewModel()
) {
    val listItems by spotterViewModel.listItems.collectAsState()
    val isLoading by spotterViewModel.isLoading.collectAsState()

    val listState =
        rememberLazyListState()     //It is used to check if the user scrolled to the end of the list

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
            onLoadMoreItems = loadMoreItems,
            isSpotter = true,
            resolveReport = {}      //Only needed by the cleaner
        )

        Spacer(modifier = Modifier.height(8.dp))

    }
}

@Composable
fun ListItem(
    listItemData: ListItemData,
    isSpotter: Boolean,
    resolveReport : (String)->Unit,
) {
    val context = LocalContext.current
    val packageManager = LocalContext.current.packageManager            //Needed to verify if google maps is installed
    val coroutineScope = rememberCoroutineScope()


    val isChecked = listItemData.validated
    val borderColor = if (isChecked) MaterialTheme.colorScheme.primary else Color.Gray

    //Manage when the item is clicked to show more informations
    val expanded = remember { mutableStateOf(false) }

    //Collects infos about the date
    val date = listItemData.date.toDate()
    val calendar = Calendar.getInstance()
    calendar.time = date

    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH) + 1    //Months start from 0
    val year = calendar.get(Calendar.YEAR)

    val imageLoaded = rememberSaveable { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded.value = !expanded.value }
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, borderColor)
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Icona checkbox
            Icon(
                imageVector = if (listItemData.validated) Icons.Default.CheckCircle else Icons.Default.Circle,
                contentDescription = null,
                tint = if (listItemData.validated) MaterialTheme.colorScheme.primary else Color.Gray,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Campo data
            Text(
                text = "Date: $dayOfMonth/$month/$year",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            // Voti associati
            Text(
                text = "Votes: ${listItemData.votes}",
                fontSize = 16.sp
            )
        }

        // Divider
        Divider()



        // Contenuto espandibile
        if (expanded.value) {
            //Loads the image
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    loadAndCacheImage(listItemData.imageUrl, imageLoaded)
                }
            }

            if(imageLoaded.value){
                Image(
                    painter = rememberAsyncImagePainter(listItemData.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
            else{   //The image is still loading
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            // GeoPoint
            Row {
                Text(
                    text = "Region: ${listItemData.region}",
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "City: ${listItemData.city}",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Row {
                Text(
                    text = "Description: ${listItemData.description}"
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            if(listItemData.validated && listItemData.resolvedByName != null){
                Row {
                    Text(
                        text = "Resolved By: ${listItemData.resolvedByName}"
                    )
                }
            }

            //Show the resolve reports button for the cleaner
            if(!isSpotter && !listItemData.validated){

                Spacer(modifier = Modifier.width(6.dp))

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Button(
                        onClick = { resolveReport(listItemData.id) },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text(text = "Resolve")
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    //The button open maps to allow the cleaner to reach a report
                    Button(onClick = {
                        val geoPoint = listItemData.location
                        val latitude = geoPoint.latitude
                        val longitude = geoPoint.longitude

                        val uri = "geo:$latitude,$longitude?q=$latitude,$longitude(Report Location)"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        intent.setPackage("com.google.android.apps.maps")

                        //Checks if the Google Maps is installed
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(context, intent, null)
                        }

                        else{
                            coroutineScope.launch {
                                Toast.makeText(
                                    context,
                                    "Install Google Maps",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    }) {
                        Text("Open Maps")
                    }
                }
            }

        }

        // Aggiungi uno spazio vuoto per far sì che l'espansione funzioni correttamente
        Spacer(modifier = Modifier.height(8.dp))
    }

}


@Composable
fun ListScreen(
    modifier: Modifier,
    listItems: List<ListItemData>,
    onLoadMoreItems: () -> Unit,
    isSpotter: Boolean,
    resolveReport:(String)->Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(items = listItems) { listItem ->
            ListItem(
                listItemData = listItem,
                isSpotter = isSpotter,
                resolveReport = resolveReport
            )

            Divider() // Aggiungi una linea divisoria tra gli elementi della lista
            Spacer(modifier = Modifier.height(4.dp))
            if (listItem == listItems.lastOrNull()) {
                onLoadMoreItems()
            }
        }
        item{
            Text(text = "")
        }
    }
}

/***********UTILITIES***********/

//This function is used to verify if the user reached the end of the list
fun LazyListState.isScrolledToTheEnd(): Boolean {
    val totalItems = layoutInfo.totalItemsCount
    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
    return totalItems > 0 && lastVisibleItem >= totalItems - 1
}

fun getProvinceFromGeoPoint(
    context: Context,
    location: GeoPoint,
    onProvinceReceived: (Address) -> Unit,
    onError: (String) -> Unit
) {
    GlobalScope.launch(Dispatchers.IO) {
        val geocoder = Geocoder(context)
        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val result = addresses[0]
                    launch(Dispatchers.Main) {
                        Log.i("geocoder","province: ${addresses[0]}")
                        onProvinceReceived(result)
                    }
                } else {
                    launch(Dispatchers.Main) {
                        onError("Nessun risultato trovato per le coordinate: ${location.latitude}, ${location.longitude}")
                    }
                }
            }
        } catch (e: IOException) {
            launch(Dispatchers.Main) {
                onError("Errore durante il geocoding inverso: ${e.message}")
            }
        }
    }
}


// Funzione per creare un oggetto Address fittizio
fun createDummyAddress(): Address {
    val address = Address(Locale.getDefault())
    address.locality = "Dummy City"
    address.adminArea = "Dummy Province"
    address.countryName = "Dummy Country"
    // Aggiungi altri campi di Address se necessario
    return address
}

//Used to load the images in a asyn way
suspend fun loadAndCacheImage(imageUrl: String, imageLoaded: MutableState<Boolean>) {
    withContext(Dispatchers.IO) {
        try {
            val picasso = Picasso.get()
            val bitmap: Bitmap = picasso.load(imageUrl)
                .config(Bitmap.Config.RGB_565)
                .resize(400,400)
                .onlyScaleDown()
                .get() // Carica l'immagine in modo asincrono
            imageLoaded.value = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}