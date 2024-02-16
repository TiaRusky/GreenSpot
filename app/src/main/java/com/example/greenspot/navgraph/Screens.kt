package com.example.greenspot.navgraph

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector


enum class GreenspotScreen(){
    SignIn,
    SignInCleaner,
    SignUpCleaner,
    CleanerProfile,
    SpotterProfile,
    Loading,
}

const val ROOT = "root_graph"         //Identify the root navgraph (Main graph)
const val WELCOME = "welcome_graph"   //Welcome route of the app (Login and Sign up screens)
const val SPOTTER = "spotter_graph"   //Sub-graph that manages the screens for logged spotters
const val CLEANER = "cleaner_graph"   //Sub-graph that manages the screens for logged cleaners

abstract  class BaseScreen(
     val title: String,
     val route : String,
     val icon : ImageVector
)

//A sealed class is used can have a set of limited objects, only defined at compiling time
//Class to describe the possible screens for a logged spotter
sealed class LoggedSpotterScreens(
    title: String,
    route: String,
    icon : ImageVector
) : BaseScreen(title,route,icon){
    object MyHome : LoggedSpotterScreens("My Profile", "SpotterProfile", Icons.Default.Home)
    object LogOut : LoggedSpotterScreens("LogOut", "logout", Icons.Default.ExitToApp)
    object MyReports : LoggedSpotterScreens("My Reports", "myReports", Icons.Default.Info)
    object NewReport : LoggedSpotterScreens("New Report", "newReport", Icons.Default.Info)

}

sealed class LoggedCleanerScreens(
     title: String,
     route: String,
     icon: ImageVector
) : BaseScreen(title,route,icon){
    object MyHome : LoggedCleanerScreens("My Profile", "CleanerProfile", Icons.Default.Home)
    object ShowReports : LoggedCleanerScreens("Show reports","showReports", Icons.Default.Info)
    object LogOut : LoggedCleanerScreens("LogOut", "logout", Icons.Default.ExitToApp )
}
