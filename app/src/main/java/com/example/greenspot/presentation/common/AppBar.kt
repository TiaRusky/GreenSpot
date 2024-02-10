package com.example.greenspot.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.greenspot.navgraph.BaseScreen
import com.example.greenspot.navgraph.LoggedCleanerScreens
import com.example.greenspot.navgraph.LoggedSpotterScreens


@Composable
fun GreenspotBottomBar(
    navController: NavHostController,
    onSignOut: () -> Unit,
    selectedScreen: String,
    isSpotter : Boolean //Allow to use the same bar in different way both for cleaners and spotters
) {
    //Define the screen based of if the user is spotter or cleaner
    val items : List<BaseScreen> = if(isSpotter){
        listOf(
            LoggedSpotterScreens.MyHome,
            LoggedSpotterScreens.MyReports,
        )
    }
    else{
        listOf(
            LoggedCleanerScreens.MyHome,
            LoggedCleanerScreens.ShowReports
        )
    }
    //List of items that will compose the bottomNavigationBar


    BottomNavigation(
        modifier = Modifier.height(40.dp),
        backgroundColor = MaterialTheme.colorScheme.primary     //BottomNavigation color
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                selected = item.title == selectedScreen,
                onClick = {
                    //Start the navigation only if the destination screen != starting screen
                    if (item.title != selectedScreen) {
                        navController.navigate(item.route) {
                            //Clear the stack screen to keep the app light
                            popUpTo(navController.graph.findStartDestination().id) {
                            }
                            //Avoid multiple copy of destinations when selecting the same item
                            //So, there will be at most one copy of a given destination on the top of the back stack
                            launchSingleTop = true
                        }
                    }
                },

                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, //align icon and text
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (item.title == selectedScreen) {  //Make visible the current page in the appbar
                                Color.Black
                            } else {
                                Color.Gray
                            },
                            modifier = Modifier.size(28.dp)
                        )

                        /*Text(
                            text = item.title,
                            color = if (item.title == selectedScreen) {
                                Color.Black
                            } else {
                                Color.Gray
                            },
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 9.sp
                        )*/

                    }
                }
            )
        }

        BottomNavigationItem(
            selected = false,
            onClick = { onSignOut() },
            icon = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = LoggedSpotterScreens.LogOut.icon,
                        contentDescription = LoggedSpotterScreens.LogOut.title,
                        tint = Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )

                    /*Text(
                        text = LoggedSpotterScreens.LogOut.title,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 9.sp
                    )*/


                }
            }
        )
    }


}


@Preview
@Composable
fun GreenspotBottomBarPreview() {
    GreenspotBottomBar(
        navController = rememberNavController(),
        selectedScreen = LoggedSpotterScreens.MyHome.title,
        onSignOut = {},
        isSpotter = true
    )
}
