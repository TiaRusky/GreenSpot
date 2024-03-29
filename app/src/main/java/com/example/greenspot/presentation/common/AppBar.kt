package com.example.greenspot.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        modifier = Modifier.height(50.dp),
        backgroundColor = MaterialTheme.colorScheme.background   //BottomNavigation color
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                selected = item.title == selectedScreen,
                onClick = {
                    //Start the navigation only if the destination screen != starting screen
                    if (item.title != selectedScreen) {
                        navController.navigate(item.route) {
                            //Clear the stack screen to keep the app light
                            if(isSpotter){
                                popUpTo(LoggedSpotterScreens.MyHome.route) {
                                    inclusive  = false
                                }
                            }
                            else{
                                popUpTo(LoggedCleanerScreens.MyHome.route) {
                                    inclusive  = false
                                }
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
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.outline
                            },
                            modifier = Modifier.size(30.dp)
                        )

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
                        modifier = Modifier.size(30.dp)
                    )

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
