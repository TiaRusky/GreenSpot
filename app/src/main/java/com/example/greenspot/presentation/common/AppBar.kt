package com.example.greenspot.presentation.common


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        backgroundColor = Color.White //BottomNavigation color
    ){
        items.forEach { item->
            BottomNavigationItem(
                selected = item.title == selectedScreen,
                onClick = {
                          navController.navigate(item.route)
                    /*TODO AVOID THE FILLING OF THE SCREEN STACK*/
                    //Si potrebbe continuare ad usare poputo
                },

                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, //align icon and text
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (item.title == selectedScreen) {
                                Color.Black
                            } else {
                                Color.Gray
                            },
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = item.title,
                            color = if (item.title == selectedScreen){
                                Color.Black
                            } else {
                                Color.Gray
                            },
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 9.sp
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
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = LoggedSpotterScreens.LogOut.title,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 9.sp
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
        onSignOut = {}
    )
}
