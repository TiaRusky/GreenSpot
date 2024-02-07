package com.example.greenspot.navgraph

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.greenspot.presentation.cleaner.profile.CleanerProfileScreen
import com.example.greenspot.presentation.cleaner.profile.ShowReportScreen
import com.example.greenspot.presentation.cleaner.sign.LoginCleanerViewModel


fun NavGraphBuilder.cleanerGraph(
    navController : NavHostController,
    lifecycleScope: LifecycleCoroutineScope,
    applicationContext: Context,
    loginCleanerViewModel : LoginCleanerViewModel
){
    navigation(
        startDestination = LoggedCleanerScreens.MyHome.route,
        route = CLEANER
    ){
        composable(route = LoggedCleanerScreens.MyHome.route){
            CleanerProfileScreen(
                navController = navController,
                onSignOut = {
                    logout(applicationContext,loginCleanerViewModel,navController)
                }
            )
        }

        composable(route = LoggedCleanerScreens.ShowReports.route){
            ShowReportScreen(
                navController = navController,
                onSignOut = {
                    logout(applicationContext,loginCleanerViewModel,navController)
                }
            )
        }
    }
}

//Logout & Redirection of the logged cleaner
private fun logout(applicationContext: Context, loginCleanerViewModel : LoginCleanerViewModel, navController :NavHostController){
    loginCleanerViewModel.logout(applicationContext)
    navController.navigate(GreenspotScreen.SignIn.name){   //Once logged out return to login screen
        popUpTo(GreenspotScreen.SignIn.name){               //Clear the screen stack to lighten the app
            inclusive = true
        }
        //Clear the state of Logged Composable
        restoreState = true
    }
}

