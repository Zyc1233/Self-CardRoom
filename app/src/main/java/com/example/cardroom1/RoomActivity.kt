package com.example.cardroom1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cardroom1.ui.theme.CardRoom1Theme
import kotlinx.coroutines.delay

class RoomActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CardRoom1Theme {
                val navController = rememberNavController()
                RoomApp(navController)
            }
        }
    }
}

@Composable
fun RoomLayout(navController: NavController){
    Column (modifier = Modifier.fillMaxSize().padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally){
        TimeCount()
        Spacer(Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment =  Alignment.CenterVertically) {
            Text(text = "温度：", fontSize = 35.sp)
            Text(text = "16°C", fontSize = 35.sp)
        }
        Spacer(Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment =  Alignment.CenterVertically) {
            Text(text = "湿度：",  fontSize = 35.sp)
            Text(text = "46%", fontSize = 35.sp)
        }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment =  Alignment.CenterVertically) {
            Text(text = "门禁", fontSize = 35.sp)
            DoorSwitch()
        }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment =  Alignment.CenterVertically) {
            Text(text = "窗帘",fontSize = 35.sp)
            CurtainSwitch()
        }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment =  Alignment.CenterVertically) {
            Text(text = "风扇", fontSize = 35.sp)
            FanSwitch()
        }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment =  Alignment.CenterVertically) {
            Text(text = "灯光",fontSize = 35.sp)
            Spacer(Modifier.width(8.dp))
            LightSlider()
        }
        Spacer(Modifier.height(16.dp))
        RoomBackButton(navController)
    }
}

@Composable
fun TimeCount() {
    // 定义倒计时的总时间（秒）
    val totalTimeInSeconds = 3600 * 1 + 60 * 30 //1小时30分
    var seconds by remember { mutableStateOf(totalTimeInSeconds) }

    // 使用 LaunchedEffect 来实现倒计时逻辑
    LaunchedEffect(key1 = Unit) {
        while (seconds > 0) {
            delay(1000) // 每秒更新一次
            seconds--    // 减少秒数
        }
    }
    // 将总秒数转换为小时、分钟和秒
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = "剩余时间: $hours 小时 $minutes 分钟 $remainingSeconds 秒",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 28.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun DoorSwitch(){
    var switchState by remember { mutableStateOf(false) }
    val imageResource = if (switchState)R.drawable.door_on else R.drawable.door_off
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.width(16.dp))
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = "Switch Image",
            modifier = Modifier.size(50.dp)
                .clickable { switchState =! switchState }
        )
        Spacer(Modifier.width(16.dp))
        Text(text = if (switchState) "门已打开" else "门已关闭", fontSize = 25.sp)
    }
}
@Composable
fun CurtainSwitch(){
    var switchState by remember { mutableStateOf(false) }
    val imageResource = if (switchState)R.drawable.curtain_on else R.drawable.curtain_off
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.width(16.dp))
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = "Switch Image",
            modifier = Modifier.size(50.dp)
                .clickable { switchState =! switchState }
        )
        Spacer(Modifier.width(16.dp))
        Text(text = if (switchState) "窗帘已拉开" else "窗帘已封闭", fontSize = 25.sp)
    }
}
@Composable
fun FanSwitch(  ){
    var switchState by remember { mutableStateOf(false) }
    val imageResource = if (switchState)R.drawable.fan1 else R.drawable.fan2
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.width(16.dp))
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = "Switch Image",
            modifier = Modifier.size(50.dp)
                .clickable { switchState =! switchState }
        )
        Spacer(Modifier.width(16.dp))
        Text(text = if (switchState) "风扇已开启" else "风扇已关闭", fontSize = 25.sp)
    }
}



@Composable
fun LightSlider() {
    val sliderValue = remember { mutableStateOf(80f) }
    val customSliderColors = SliderDefaults.colors(
        activeTrackColor = Color.Green,
        inactiveTrackColor = Color.LightGray, // 自定义非活动轨道的颜色
        thumbColor = Color.Yellow // 自定义滑块的颜色
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Slider(
            value = sliderValue.value,
            onValueChange = { newValue ->
                sliderValue.value = newValue
            },
            valueRange = 0f..100f,
            steps = 10,
            modifier = Modifier.width(200.dp),
            colors = customSliderColors // 应用自定义颜色
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = " ${sliderValue.value.toInt()}",
            fontSize = 25.sp,
        )
    }
}


@Composable
fun RoomBackButton(navController: NavController){
    Button(onClick = {
        navController.navigate(ScreenPage.Index.route){
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    },
        colors = ButtonDefaults.buttonColors(Color.LightGray)) {
        Text(text = "退出房间", fontSize = 30.sp)
    }
}





@Composable
fun RoomApp(navController: NavController){
    RoomLayout(navController)
}


@Preview(showBackground = true)
@Composable
fun RoomPreview() {
    CardRoom1Theme {
        val navController = rememberNavController()
        RoomLayout(navController)
    }
}