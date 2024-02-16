package com.example.greenspot.presentation.cleaner.profile

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greenspot.R

@Composable
fun SearchButtonComponent(
    value: String,
    onButtonClicked: () -> Unit,
    painterResource: Painter
){
    Button(
        modifier = Modifier
            .width(70.dp)
            .heightIn(15.dp),
        onClick = {
            onButtonClicked.invoke()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary, contentColor = Color.White)
    ) {
        Icon(
            modifier = Modifier
                .padding(8.dp)
                .width(150.dp),
            painter = painterResource,
            contentDescription = "",
            tint = Color.Unspecified
        )
        /*Text(
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.background,
            fontStyle = FontStyle.Normal,
            text = value
        )*/
    }

}

@Preview
@Composable
fun SearchButtonComponentPreview(){
    SearchButtonComponent(
        value = "AAA",
        onButtonClicked = {},
        painterResource = painterResource(id = R.drawable.ic_search)
    )
}