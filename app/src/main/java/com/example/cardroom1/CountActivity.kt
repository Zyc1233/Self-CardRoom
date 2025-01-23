package com.example.cardroom1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardroom1.ui.theme.CardRoom1Theme

class CountActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardRoom1Theme {
                CountApp()
            }
        }
    }
}

@Composable
fun CountApp() {
    val image = painterResource(R.drawable.cardroom)
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = image,
            contentDescription = "Card Room Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
    CountLayout()
}

@Composable
fun CountLayout() {
    val startTime = remember { mutableLongStateOf(System.currentTimeMillis()) }
    val currentTime = remember { mutableLongStateOf(System.currentTimeMillis()) }
    val timeUsed by remember { derivedStateOf { (currentTime.longValue - startTime.longValue) / 1000.0 / 60.0 } } // 转换为分钟
    val totalCharge by remember {
        derivedStateOf {
            val hours = timeUsed.toInt() / 60
            val minutes = timeUsed.toInt() % 60
            val extraCharges = if (minutes >= 30) 5 else 0
            20 * hours + extraCharges
        }
    }

    // 房间类型和价格
    val rooms = listOf(
        Room("麻将室", 20),
        Room("象棋室", 15),
        Room("扑克室", 20),
        Room("桌游室", 30)
    )
    var selectedRoom by remember { mutableStateOf(rooms.first()) }
    var inputTime by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf("总费用: 0元") }



    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 房间选择下拉菜单
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { expanded = true },
                        modifier = Modifier.width(200.dp).height(50.dp)) {
                        Text(text = selectedRoom.name, fontSize = 28.sp, textAlign = TextAlign.Center, color = Color.Black)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        rooms.forEach { room ->
                            DropdownMenuItem(
                                text = { Text(text = room.name) },
                                onClick = {
                                    selectedRoom = room
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 时间输入框
                TextField(
                    value = inputTime,
                    onValueChange = { inputTime = it },
                    label = { Text(text = "输入使用时间（分钟）") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 计算按钮
                Button(
                    onClick = {
                        val time = inputTime.toIntOrNull() ?: 0
                        result = calculateFee(selectedRoom, time)
                        showResult = true
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text(text = "计算费用", fontSize = 28.sp, textAlign = TextAlign.Center, color = Color.Black)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 显示结果
                if (showResult) {
                    Text(text = result, fontSize = 24.sp, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

// 将 Room 数据类移到外部
data class Room(val name: String, val pricePerHour: Int)

// 费用计算函数
fun calculateFee(room: Room, time: Int): String {
    if (time <= 0) return "总费用: 0元"
    val hours = time / 60
    val minutes = time % 60
    val baseFee = room.pricePerHour * hours
    val extraFee = if (minutes > 0) 10 + (minutes / 30) * 5 else 0
    return "总费用: ${baseFee + extraFee}元"
}

