package com.example.greenspot.presentation.cleaner.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchButtonComponent(
    value: String,
    onButtonClicked: () -> Unit,
    painterResource: Painter
){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        onClick = {
            onButtonClicked.invoke()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary, contentColor = Color.White)
    ) {
        Icon(
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp),
            painter = painterResource,
            contentDescription = "",
            tint = Color.Unspecified
        )
        Text(
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.background,
            fontStyle = FontStyle.Normal,
            text = value
        )
    }

}