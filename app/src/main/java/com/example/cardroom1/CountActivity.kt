package com.example.cardroom1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
    CountLayout()
}

@Composable
fun CountLayout() {
    // 房间类型和价格
    val rooms = listOf(
        Room("麻将室", 30),
        Room("象棋室", 20),
        Room("扑克室", 30),
        Room("桌游室", 40)
    )
    var selectedRoom by remember { mutableStateOf(rooms.first()) }
    var inputTime by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf("总费用: 0元") }
    // 添加VIP状态变量
    var isVip by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ZoomableImage()
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

                // 添加VIP单选框
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isVip,
                        onCheckedChange = { isVip = it }
                    )
                    Text(text = "我是VIP（享受8折优惠）", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 计算按钮
                Button(
                    onClick = {
                        val time = inputTime.toIntOrNull() ?: 0
                        result = calculateFee(selectedRoom, time, isVip)
                        showResult = true
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
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
@Composable
fun ZoomableImage() {
    var isZoomed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isZoomed) 1.5f else 1f,
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clickable { isZoomed = !isZoomed }
    ) {
        Image(
            painter = painterResource(R.drawable.roomcount),
            contentDescription = "roomCount",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
        )
    }
}

// 将 Room 数据类移到外部
data class Room(val name: String, val pricePerHour: Int)

// 修改calculateFee函数，增加VIP参数
fun calculateFee(room: Room, time: Int, isVip: Boolean): String {
    if (time <= 0) return "总费用: 0元"

    // 如果时间不足1小时，按1小时计算
    val totalMinutes = if (time < 60) 60 else time

    // 计算总费用
    val hours = totalMinutes / 60
    val baseFee = room.pricePerHour * hours

    // 计算剩余分钟数（不足1小时的部分）
    val remainingMinutes = totalMinutes % 60

    // 如果剩余分钟数大于0，按半小时收费
    val extraFee = if (remainingMinutes > 0) {
        (remainingMinutes + 29) / 30 * 5 // 不足半小时按半小时计算
    } else {
        0
    }

    // 如果是VIP，享受8折优惠
    val totalFee = baseFee + extraFee
    val finalFee = if (isVip) totalFee * 0.8 else totalFee
    return "总费用: ${finalFee.toInt()}元"
}