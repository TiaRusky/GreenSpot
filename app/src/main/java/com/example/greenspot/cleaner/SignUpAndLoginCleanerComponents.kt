package com.example.greenspot.cleaner

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//composable for text
@Composable
fun NormalTextComponent(value:String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        color = Color(0xff1d1617), //colorText
        textAlign = TextAlign.Center
    )
}

//composable for text bold
@Composable
fun HeadingTextComponent(value:String) {
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
        color = Color(0xff1d1617), //colorText
        textAlign = TextAlign.Center
    )
}

//composable to create fields to insert something about signup
@Composable
fun MyTextFieldComponent(labelValue: String, painterResource: Painter) {

    val textValue = remember { //used to remember the last value inserted in the field
        mutableStateOf("")
    }

    OutlinedTextField( //used to insert a field with border (field to insert the first name)
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF92A3FD),
            focusedLabelColor = Color(0xFF92A3FD),
            cursorColor = Color(0xFF92A3FD)
        ),
        keyboardOptions = KeyboardOptions.Default,
        value = textValue.value,
        onValueChange = { //used when the value change
            textValue.value = it
        },
        leadingIcon = { //used to insert the icons
            Icon(painter = painterResource, contentDescription = "")
        }
    )
}

@Composable
fun PasswordTextFieldComponent(labelValue: String, painterResource: Painter) {

    val password = remember { //used to remember the last value inserted in the field
        mutableStateOf("")
    }

    val passwordVisible = remember { //used to hide the password in the field
        mutableStateOf(false)
    }

    OutlinedTextField( //used to insert a field with border (field to insert the first name)
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF92A3FD),
            focusedLabelColor = Color(0xFF92A3FD),
            cursorColor = Color(0xFF92A3FD)
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        value = password.value,
        onValueChange = { //used when the value change
            password.value = it
        },
        leadingIcon = { //used to insert the icons
            Icon(painter = painterResource, contentDescription = "")
        },
        trailingIcon = {
            val iconImage = if(passwordVisible.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }

            var description = if(passwordVisible.value) {
                "Hide password"
            } else {
                "Show password"
            }

            IconButton(
                onClick = {
                    passwordVisible.value = !passwordVisible.value
                }
            ){
                Icon(
                    imageVector = iconImage, contentDescription = description
                )
            }
        },
        visualTransformation = if(passwordVisible.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    )
}

//used for the button register and login
@Composable
fun ButtonComponent(value: String) {
    Button(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = RoundedCornerShape(50.dp)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Color(0xFF9DCEFF), Color(0xFF92A3FD))),
                    shape = RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

//composable used for write the line with or
@Composable
fun DividerTextComponent() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = Color(0xff7b6f72),
            thickness = 1.dp
        )
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = "Or",
            fontSize = 18.sp,
            color = Color(0xFF1D1617)
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = Color(0xff7b6f72),
            thickness = 1.dp
        )
    }
}

@Composable
fun ClickableLoginTextComponent(tryToLogin:Boolean = true, onTextSelected: () -> Unit) {
    val initialText = if(tryToLogin) {
        "Already have an account? "
    } else {
        "Don't have an account yet? "
    }
    val loginText = if(tryToLogin) {
        "Login"
    } else {
        "Register"
    }

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(Color(0xFF92A3FD))) {
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }
    }
    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        text = annotatedString,
        onClick = {
            onTextSelected()
        },
    )
}

@Composable
fun UnderLinedTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        color = Color(0xFF7B6F72),
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline
    )

}