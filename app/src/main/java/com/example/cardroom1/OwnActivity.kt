package com.example.cardroom1

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardroom1.ui.theme.CardRoom1Theme

class OwnActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CardRoom1Theme {
                OwnApp()
            }
        }
    }
}


@Composable
fun OwnApp(){
    OwnLayout()
}




@Composable
fun OwnLayout(){
    Column(modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(16.dp))
        Text(text = "个人信息", style = TextStyle(fontSize = 50.sp))
        Spacer(Modifier.height(16.dp))
        UserHead()
        Spacer(Modifier.height(16.dp))
//        UserPhone()
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserHead() {
    var scaled by remember { mutableStateOf(false) }
    val imgScale by animateFloatAsState(
        targetValue = if (scaled) 1.25f else 1f,
        label = "UserHeadImageScaleAnimation"
    )
    Box(modifier = Modifier
        .border(5.dp, Color.Blue, CircleShape)
        .clip(CircleShape)
        .pointerInteropFilter {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    scaled = true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    scaled = false
                }
                else -> false
            }
            true
        }) {
        Image(
            modifier = Modifier
                .size(300.dp)
                .scale(imgScale),
            painter = painterResource(id = R.drawable.people),
            contentDescription = null
        )
    }
}


@Preview(showBackground = true)
@Composable
fun OwnPreview() {
    CardRoom1Theme {
        OwnLayout()
    }
}