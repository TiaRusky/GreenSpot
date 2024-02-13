package com.example.greenspot.presentation.spotter.addReport

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddReportTextComponent(value: String){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        color = MaterialTheme.colorScheme.onBackground, //colorText
        textAlign = TextAlign.Center
    )
}

@Composable
fun CaptionComponent(labelValue: String,
                         //painterResource: Painter,
                         //onTextChanged: (String) -> Unit,
                         //errorStatus: Boolean = false
) {

    val textValue = remember { //used to remember the last value inserted in the field
        mutableStateOf("")
    }

    OutlinedTextField(
        //used to insert a field with border (field to insert the first name)
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)),
        label = { androidx.compose.material.Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF92A3FD),
            focusedLabelColor = Color(0xFF92A3FD),
            cursorColor = Color(0xFF92A3FD)
        ),
        keyboardOptions = KeyboardOptions.Default,
        value = textValue.value,
        onValueChange = { //used when user insert a name in the field
            textValue.value = it
            //onTextChanged(it)
        },
    )
}

@Composable
fun InsertPhotoButtonComponent(value: String,
                      onButtonClicked : () -> Unit,
                      painterResource: Painter
) {
    Button(
        onClick = {
            onButtonClicked.invoke()
        },
        shape = RoundedCornerShape(40.dp)
    ){
        Icon(
            modifier = Modifier
                .padding(8.dp)
                .size(84.dp)
                .clip(RoundedCornerShape(corner = CornerSize(16.dp))),
            painter = painterResource,
            contentDescription = "",
            tint = Color.Unspecified
        )
        Text(
            fontSize = 30.sp,
            //color = MaterialTheme.colorScheme.onPrimaryContainer,
            color = Color.White,
            fontStyle = FontStyle.Normal,
            text = value
        )
    }
}

@Composable
fun CancelButton(){

}