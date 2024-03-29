package com.example.greenspot.presentation.cleaner.sign

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
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
        color = androidx.compose.material3.MaterialTheme.colorScheme.tertiary, //colorText
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
        color = androidx.compose.material3.MaterialTheme.colorScheme.tertiary, //colorText
        textAlign = TextAlign.Center
    )
}

//composable to create fields to insert something about signup
//onTextSelected --> whenever the text is changed, we will just pass a string as a callback inside our screen
@Composable
fun MyTextFieldComponent(labelValue: String,
                         painterResource: Painter,
                         onTextChanged: (String) -> Unit,
                         errorStatus: Boolean = false
) {

    val textValue = rememberSaveable { //used to remember the last value inserted in the field
        mutableStateOf("")
    }

    OutlinedTextField( //used to insert a field with border (field to insert the first name)
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)),
        label = {
            Text(
                text = labelValue,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f) //colore del text email
            )
                },

        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.onBackground
        ),

        keyboardOptions = KeyboardOptions.Default,
        value = textValue.value,
        onValueChange = { //used when user insert a name in the field
            textValue.value = it
            onTextChanged(it)
        },
        leadingIcon = { //used to insert the icons
            Icon(
                painter = painterResource,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f) //colore icona email
            )
        },
        isError = !errorStatus //if there is an error in validation, errorStatus is false and isError became true
    )
}

@Composable
fun PasswordTextFieldComponent(labelValue: String,
                               painterResource: Painter,
                               onTextSelected: (String) -> Unit,
                               errorStatus: Boolean = false
) {

    val password = rememberSaveable { //used to remember the last value inserted in the field
        mutableStateOf("")
    }

    val passwordVisible = rememberSaveable { //used to hide the password in the field
        mutableStateOf(false)
    }

    OutlinedTextField( //used to insert a field with border (field to insert the first name)
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)),
        label = {
            Text(
                text = labelValue,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f) //colore del text password
            )
                },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.onBackground
        ),

        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        value = password.value,
        onValueChange = { //used when the value change
            password.value = it
            onTextSelected(it)
        },
        leadingIcon = { //used to insert the icons
            Icon(
                painter = painterResource,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f) //colore icona password
            )
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
        },
        isError = !errorStatus //if there is an error in validation, errorStatus is false and isError became true
    )
}

//used for the button register and login
@Composable
fun ButtonComponent(value: String,
                    onButtonClicked : () -> Unit,
                    isEnabled : Boolean = false
) {
    Button(
        onClick = {
            onButtonClicked.invoke()
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = RoundedCornerShape(50.dp),
        enabled = isEnabled //used to enabled the register button when all the fields are valid
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimary,
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
            color = MaterialTheme.colorScheme.secondary,
            thickness = 1.dp
        )
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = "Or",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.secondary
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = MaterialTheme.colorScheme.secondary,
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
        withStyle(style = SpanStyle(MaterialTheme.colorScheme.primary)) {
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
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.tertiary
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
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline
    )

}