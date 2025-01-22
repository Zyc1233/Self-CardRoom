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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddIcCall
import androidx.compose.material.icons.filled.CoPresent
import androidx.compose.material.icons.filled.ContentPasteSearch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.cardroom1.ui.theme.CardRoom1Theme

class OwnActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CardRoom1Theme {
                val navController = rememberNavController()
                OwnApp(navController)
            }
        }
    }
}

@Composable
fun OwnApp(navController: NavController) {
    var currentAvatarUrl by remember { mutableStateOf("https://example.com/default-avatar.png") }
    OwnLayout(currentAvatarUrl,navController) { newUrl ->
        currentAvatarUrl = newUrl
    }
}

@Composable
fun OwnLayout(currentAvatarUrl: String,navController: NavController, onAvatarChange: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        UserHead(currentAvatarUrl)
        Spacer(Modifier.height(4.dp))
        ChangeHead(onAvatarChange)
        Spacer(Modifier.height(8.dp))
        OwnName()
        Spacer(Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()){
            Column(modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick ={
                    navController.navigate(ScreenPage.Register.route)
                } ) {
                    Icon(Icons.Filled.CoPresent,null)
                }
                Text(text = "注册登录", fontSize = 20.sp)
            }

            Column(modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick ={
                    navController.navigate(ScreenPage.Search.route)
                } ) {
                    Icon(Icons.Filled.ContentPasteSearch,null)
                }
                Text(text = "查看记录", fontSize = 20.sp)
            }

            Column(modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick ={
                    navController.navigate(ScreenPage.About.route)
                }) {
                    Icon(Icons.Filled.AddIcCall,null)
                }
                Text(text = "联系我们", fontSize = 20.sp)
            }

            Column(modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick ={
                    navController.navigate(ScreenPage.Setting.route)
                }) {
                    Icon(Icons.Filled.Settings,null)
                }
                Text(text = "设置", fontSize = 20.sp)
            }
        }
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
fun ChangeHead(onAvatarChange: (String) -> Unit) {
    val drawableList = listOf(
        R.drawable.image1, R.drawable.image2, R.drawable.image3,
        R.drawable.image4, R.drawable.people, R.drawable.image6,
        R.drawable.image7, R.drawable.image8, R.drawable.image9)
    var showAvatarDialog by remember { mutableStateOf(false) }
    val openDialog: () -> Unit = { showAvatarDialog = true }
    val closeDialog: () -> Unit = { showAvatarDialog = false }

    Button(
        onClick = openDialog,
        colors = ButtonDefaults.buttonColors(Color.LightGray),
        shape = RoundedCornerShape(8.dp) // 修改按钮形状
    ) {
        Text(text = "更换头像", color = Color.Black, fontSize = 16.sp)
    }

    if (showAvatarDialog) {
        AlertDialog(
            onDismissRequest = closeDialog,
            title = { Text("选择头像") }, // 修改标题样式
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(16.dp), // 增加内容内边距
                    verticalArrangement = Arrangement.spacedBy(16.dp), // 增加垂直间距
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // 增加水平间距
                ) {
                    items(drawableList) { drawableResId ->
                        val painter = painterResource(id = drawableResId)
                        Image(
                            painter = painter,
                            contentDescription = "Avatar option",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp)) // 修改图片形状
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
                    Text("取消") // 修改按钮文本样式
                }
            }
        )
    }
}


@Composable
fun OwnName() {
    var showDialog by remember { mutableStateOf(false) }
    var nickname by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "昵称：$nickname",
            modifier = Modifier.clickable { showDialog = true }
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("填写昵称") },
            text = {
                BasicTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}
