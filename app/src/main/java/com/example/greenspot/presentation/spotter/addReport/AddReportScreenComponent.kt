package com.example.greenspot.presentation.spotter.addReport

import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter.State.Empty.painter

@Composable
fun AddReportTextComponent(value: String) {
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
        color = MaterialTheme.colorScheme.tertiary, //colorText
        textAlign = TextAlign.Center
    )
}

@Composable
fun CaptionComponent(
    labelValue: String,
    //painterResource: Painter,
    //onTextChanged: (String) -> Unit,
    //errorStatus: Boolean = false,
    textValue: String,
    onChange : (String)->Unit,
) {


    OutlinedTextField(
        //used to insert a field with border (field to insert the first name)
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
            cursorColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.primary,
        ),
        keyboardOptions = KeyboardOptions.Default,
        value = textValue,
        onValueChange = {            //used when user insert a name in the field
            onChange(it)
        },
    )
}

@Composable
fun InsertPhotoButtonComponent(
    value: String,
    onButtonClicked: () -> Unit,
    painterResource: Painter
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        onClick = {
            onButtonClicked.invoke()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primary, contentColor = Color.White)
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

@Composable
fun CancelButton() {

}