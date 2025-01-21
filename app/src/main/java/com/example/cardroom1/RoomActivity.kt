package com.example.cardroom1

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cardroom1.ui.theme.CardRoom1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RoomActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardRoom1Theme {
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val reservationId = currentBackStackEntry?.arguments?.getLong("reservationId") ?: -1L
                val startTime = currentBackStackEntry?.arguments?.getString("startTime") ?: ""
                val endTime = currentBackStackEntry?.arguments?.getString("endTime") ?: ""
                Log.d("RoomActivity", "Reservation ID: $reservationId, Start Time: $startTime, End Time: $endTime")
                RoomApp(navController, reservationId)
            }
        }
    }
}

@Composable
fun RoomApp(
    navController: NavController,
    reservationId: Long,
) {
    val reservation = remember { mutableStateOf<Reservation?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val viewModel: ReservationViewModel = viewModel()

    // 在 LaunchedEffect 中加载预约信息
    LaunchedEffect(reservationId) {
        withContext(Dispatchers.IO) {
            val res = viewModel.getReservationById(reservationId)
            reservation.value = res
            isLoading.value = false
        }
    }

    // 如果正在加载，显示进度指示器
    if (isLoading.value) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        // 如果加载完成，显示 RoomLayout
        reservation.value?.let { res ->
            RoomLayout(
                navController = navController,
                room = res.room,
                date = res.date,
                startTime = res.time1,
                endTime = res.time2
            )
        } ?: Text("未找到预约信息")
    }
}


@Composable
fun RoomLayout(
    navController: NavController,
    room: String,
    date: String,
    startTime: String,
    endTime: String
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 16.dp).padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimeCount(startTime, endTime)
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "当前房间：$room", fontSize = 25.sp)
        }
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "预约时间：", fontSize = 25.sp)
            Text(text = "$date   $startTime-$endTime", fontSize = 25.sp)
        }
        Spacer(Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "温度：", fontSize = 35.sp)
            Text(text = "16°C", fontSize = 35.sp)
        }
        Spacer(Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "湿度：", fontSize = 35.sp)
            Text(text = "75%", fontSize = 35.sp)
        }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "门禁", fontSize = 35.sp)
            DoorSwitch()
        }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "窗帘", fontSize = 35.sp)
            CurtainSwitch()
        }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "风扇", fontSize = 35.sp)
            FanSwitch()
        }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "灯光", fontSize = 35.sp)
            Spacer(Modifier.width(8.dp))
            LightSlider()
        }
        Spacer(Modifier.height(16.dp))
        RoomBackButton(navController)
    }
}



@Composable
fun TimeCount(startTime: String, endTime: String) {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.CHINA)
    val startCalendar = Calendar.getInstance()
    val endCalendar = Calendar.getInstance()

    // 解析开始时间和结束时间并设置到 Calendar 实例中
    startCalendar.time = dateFormat.parse(startTime)?: Date()
    endCalendar.time = dateFormat.parse(endTime)?: Date()

    // 确保考虑日期因素，设置开始和结束时间的日期部分相同
    val startDate = startCalendar.get(Calendar.DATE)
    endCalendar.set(Calendar.DATE, startDate)

    // 计算剩余时间（以毫秒为单位）
    val startMillis = startCalendar.timeInMillis
    val endMillis = endCalendar.timeInMillis
    var remainingMillis by remember { mutableLongStateOf(endMillis - startMillis) }

    var hours by remember { mutableIntStateOf(0) }
    var minutes by remember { mutableIntStateOf(0) }
    var secondsLeft by remember { mutableIntStateOf(0) }

    LaunchedEffect(remainingMillis) {
        if (remainingMillis > 0) {
            val countDownTimer = object : CountDownTimer(remainingMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    remainingMillis = millisUntilFinished
                    val totalSeconds = (millisUntilFinished / 1000).toInt()
                    hours = totalSeconds / 3600
                    minutes = (totalSeconds % 3600) / 60
                    secondsLeft = totalSeconds % 60
                }

                override fun onFinish() {
                    remainingMillis = 0
                    hours = 0
                    minutes = 0
                    secondsLeft = 0
                    Log.d("TimeCount", "时间已到")
                }
            }
            countDownTimer.start()
        }
    }


    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = if (remainingMillis > 0) {
                "剩余时间：$hours 小时 $minutes 分钟 $secondsLeft 秒"
            } else {
                "时间已到"
            },
            fontSize = 25.sp
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
    val sliderValue = remember { mutableFloatStateOf(80f) }
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
            value = sliderValue.floatValue,
            onValueChange = { newValue ->
                sliderValue.floatValue = newValue
            },
            valueRange = 0f..100f,
            steps = 10,
            modifier = Modifier.width(200.dp),
            colors = customSliderColors // 应用自定义颜色
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = " ${sliderValue.floatValue.toInt()}",
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



