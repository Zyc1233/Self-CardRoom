package com.example.cardroom1

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
fun OwnApp() {
    var currentAvatarUrl by remember { mutableStateOf("https://example.com/default-avatar.png") }
    OwnLayout(currentAvatarUrl) { newUrl ->
        currentAvatarUrl = newUrl
    }
}

@Composable
fun OwnLayout(currentAvatarUrl: String, onAvatarChange: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        Text(text = "个人信息", style = TextStyle(fontSize = 50.sp))
        Spacer(Modifier.height(16.dp))
        UserHead(currentAvatarUrl)
        ChangeHead(currentAvatarUrl, onAvatarChange)
        Spacer(Modifier.height(64.dp))
        BackButton()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserHead(currentAvatarUrl: String) {
    var scaled by remember { mutableStateOf(false) }
    val imgScale by animateFloatAsState(
        targetValue = if (scaled) 1.25f else 1f,
        label = "UserHeadImageScaleAnimation"
    )

    Box(
        modifier = Modifier
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
            }
    ) {
        if (currentAvatarUrl.startsWith("drawable://")) {
            val drawableResId = currentAvatarUrl.substring("drawable://".length).toInt()
            val painter = painterResource(id = drawableResId)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .scale(imgScale)
            )
        } else {
            AsyncImage(
                model = currentAvatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .scale(imgScale),
                error = painterResource(id = R.drawable.image5)
            )
        }
    }
}

@Composable
fun ChangeHead(currentAvatarUrl: String, onAvatarChange: (String) -> Unit) {
    val context = LocalContext.current
    val drawableList = listOf(
        R.drawable.image1, R.drawable.image2, R.drawable.image3,
        R.drawable.image4, R.drawable.people, R.drawable.image6,
        R.drawable.image7, R.drawable.image8, R.drawable.image9)
    var showAvatarDialog by remember { mutableStateOf(false) }
    val openDialog: () -> Unit = { showAvatarDialog = true }
    val closeDialog: () -> Unit = { showAvatarDialog = false }

    Button(
        onClick = openDialog,
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = "更换头像", color = Color.Black, fontSize = 16.sp)
    }

    if (showAvatarDialog) {
        AlertDialog(
            onDismissRequest = closeDialog,
            title = { Text("选择头像") },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(drawableList) { drawableResId ->
                        val painter = painterResource(id = drawableResId)
                        Image(
                            painter = painter,
                            contentDescription = "Avatar option",
                            modifier = Modifier
                                .size(100.dp)
                                .clickable {
                                    val drawableUri = "drawable://$drawableResId"
                                    onAvatarChange(drawableUri)
                                    closeDialog()
                                }
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(
                    onClick = closeDialog
                ) {
                    Text("取消")
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun OwnPreview() {
    CardRoom1Theme {
        OwnLayout("https://example.com/default-avatar.png") { }
    }
}