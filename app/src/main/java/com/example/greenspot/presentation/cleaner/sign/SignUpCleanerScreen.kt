package com.example.greenspot.presentation.cleaner.sign

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.greenspot.R
import com.example.greenspot.navgraph.GreenspotScreen

@Composable
fun SignUpCleanerScreen(
    navController: NavHostController
) {

    Surface (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ){
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier
                    .size(150.dp,150.dp),
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_trees),
                    contentDescription = "logo"
                )
            }
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            NormalTextComponent(value = "Hello there,")
            HeadingTextComponent(value = "Create an account")
            Spacer(
                modifier = Modifier
                    .height(40.dp)
            )
            MyTextFieldComponent(
                labelValue = "Cleaner company name",
                painterResource = painterResource(id = R.drawable.profile)
            )
            MyTextFieldComponent(
                labelValue = "Email",
                painterResource = painterResource(id = R.drawable.message)
            )
            PasswordTextFieldComponent(
                labelValue = "Password",
                painterResource = painterResource(id = R.drawable.ic_lock)
            )
            Spacer(
                modifier = Modifier
                    .height(100.dp)
            )
            ButtonComponent(
                value = "Register"
            )
            DividerTextComponent()
            ClickableLoginTextComponent(tryToLogin = true, onTextSelected = {
                navController.navigate(GreenspotScreen.SignInCleaner.name){
                    popUpTo(navController.graph.findStartDestination().id)
                }
            })
        }

    }

}

@Preview
@Composable
fun DefaultPreviewOfSignUpCleanerScreen(){
    SignUpCleanerScreen(
        navController = rememberNavController()
    )
}
