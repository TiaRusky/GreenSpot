package com.example.greenspot.presentation.spotter

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeadTextComponent(value: String){
    androidx.compose.material.Text(
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

@Composable
fun GridItem(text: String, number: Int, painterResource: Painter) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ){
        Row{
            Image(
                painter = painterResource,
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(84.dp)
                    .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ){
                Text(
                    text = "$text: $number",
                    fontSize = 30.sp,
                    color = Color(0xff1d1617),
                    fontStyle = FontStyle.Normal,
                )
            }
        }
    }
}

@Composable
fun ShakePhoneIconComponent(painterResource: Painter){
    var isShaking = true
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Image(
                painter = painterResource,
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(140.dp)
                    .offset(animateShakeEffect(isShaking).value.dp),
                colorFilter = ColorFilter.tint(Color.Gray.copy(alpha = 0.6f))
            )
        }
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = "Shake the phone to public a new post",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            color = Color.Gray.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun animateShakeEffect(isShaking: Boolean): State<Float> {
    val shakeAnimation = rememberInfiniteTransition()

    return shakeAnimation.animateFloat(
        initialValue = 0f, //Il valore iniziale dell'animazione è 0
        targetValue = 10f, //valore finale dell'animazione è 10
        animationSpec = infiniteRepeatable( //usato per rendere l'animazione infinita
            animation = tween(300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse //l'animazione inverte la direzione quando raggiunge il valore massimo specificato da targetValue
        )
    )
}

