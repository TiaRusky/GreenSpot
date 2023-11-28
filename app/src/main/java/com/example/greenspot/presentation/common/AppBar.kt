package com.example.greenspot.presentation.common


import androidx.compose.foundation.background
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.greenspot.navgraph.LoggedSpotterScreens


@Composable
fun GreenspotBottomBar(
    navController : NavHostController,
    onSignOut: () -> Unit,
    selectedScreen : String
) {

    val items = listOf(
        LoggedSpotterScreens.MyHome,
        LoggedSpotterScreens.MyReports,
        //LoggedSpotterScreens.LogOut
    )

    BottomNavigation (
        modifier = Modifier.background(color = MaterialTheme.colorScheme.primary)
    ){
        items.forEach { item->
            BottomNavigationItem(
                selected = item.title == selectedScreen,
                onClick = {
                          navController.navigate(item.route)
                    /*TODO AVOID THE FILLING OF THE SCREEN STACK*/
                },

                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                }
            )
        }

        BottomNavigationItem(
            selected = false,
            onClick = { onSignOut() },
            icon = {
                Icon(imageVector = LoggedSpotterScreens.LogOut.icon,contentDescription = LoggedSpotterScreens.LogOut.title)
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
        onSignOut = {}
    )
}



