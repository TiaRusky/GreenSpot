package com.example.greenspot.presentation.common



import androidx.compose.foundation.background
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview



@Composable
fun GreenspotBottomBar(
     selectedScreen : String,

) {
    /*
    val items = listOf(
        SpotterScreens.MyHome,
        SpotterScreens.MyReports,
        SpotterScreens.LogOut
    )

    BottomNavigation (
        modifier = Modifier.background(color = MaterialTheme.colorScheme.primary)
    ){
        items.forEach { item->
            BottomNavigationItem(
                selected = item.title == selectedScreen,
                onClick = {  },

                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                }
            )
        }
    }
    */

}

//A sealed class is used can have a set of limited objects, only defined at compiling time
//Class to describe the possibile screen of the menu
sealed class SpotterScreens(
    val title: String,
    val route: String,
    val icon : ImageVector
) {
    object MyHome : SpotterScreens("My Profile", "mySpotterProfile", Icons.Default.Home)
    object LogOut : SpotterScreens("LogOut", "logout", Icons.Default.ExitToApp)
    object MyReports : SpotterScreens("My Reports", "myReports", Icons.Default.Info)
    /*TO DO*/
}
@Preview
@Composable
fun GreenspotBottomBarPreview() {
    GreenspotBottomBar(selectedScreen = SpotterScreens.MyHome.title)
}



