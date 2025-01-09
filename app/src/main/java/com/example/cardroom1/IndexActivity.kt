package com.example.cardroom1

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardroom1.ui.theme.CardRoom1Theme
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class IndexActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardRoom1Theme {
                val navController = rememberNavController()
                val reservationId = intent.getLongExtra("reservationId", -1L)
                val userName = intent.getStringExtra("userName") ?: ""
                val selectedRoomsText = intent.getStringExtra("selectedRoomsText") ?: ""
                val selectedDate = intent.getStringExtra("selectedDate") ?: ""
                val selectedStartTime = intent.getStringExtra("selectedStartTime") ?: ""
                val selectedEndTime = intent.getStringExtra("selectedEndTime") ?: ""
                IndexApp(
                    navController = navController,
                    reservationId = reservationId,
                    userName = userName,
                    selectedRoomsText = selectedRoomsText,
                    selectedDate = selectedDate,
                    selectedStartTime = selectedStartTime,
                    selectedEndTime = selectedEndTime
                )
            }
        }
    }
}


@Composable
fun IndexApp(
    navController: NavController,
    reservationId: Long = -1L,
    userName: String = "",
    selectedRoomsText: String = "",
    selectedDate: String = "",
    selectedStartTime: String = "",
    selectedEndTime: String = ""
) {
    val context = LocalContext.current
    val database = DatabaseHelper.getInstance(context)
    val reservationsLiveData = database.reservationDao().getAllReservations()
    val initialReservations = reservationsLiveData.observeAsState(initial = listOf()).value
    val reservations: MutableState<List<Reservation>> = remember {
        mutableStateOf(initialReservations)
    }
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val newReservations = database.reservationDao().getAllReservations().value?: listOf()
            reservations.value = newReservations
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        Text(text = if (reservationId == -1L) "房间预约" else "修改预约", style = TextStyle(color = Color.Black, fontSize = 50.sp))
        Spacer(Modifier.height(16.dp))
        ImageGroup()
        Spacer(Modifier.height(16.dp))
        val selectedRoomsTextState = remember { mutableStateOf(selectedRoomsText) }
        RoomText(selectedRoomsTextState)
        Spacer(Modifier.height(16.dp))
        val selectedDateState = remember { mutableStateOf(selectedDate) }
        DateText(selectedDateState)
        Spacer(Modifier.height(16.dp))
        val selectedStartTimeState = remember { mutableStateOf(selectedStartTime) }
        StartTimeText(selectedStartTimeState)
        Spacer(Modifier.height(16.dp))
        val selectedEndTimeState = remember { mutableStateOf(selectedEndTime) }
        EndTimeText(selectedEndTimeState)
        Spacer(Modifier.height(16.dp))
        val userNameState = remember { mutableStateOf(userName) }
        UserName(userNameState)
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            ReservationButton(
                database,
                reservations,
                userNameState,
                selectedRoomsTextState,
                selectedDateState,
                selectedStartTimeState,
                selectedEndTimeState
            )
            Spacer(Modifier.width(8.dp))
            ModifyButton(
                reservationId = reservationId,
                userName = userNameState,
                selectedRoomsText = selectedRoomsTextState,
                selectedDate = selectedDateState,
                selectedStartTime = selectedStartTimeState,
                selectedEndTime = selectedEndTimeState
            )
            Spacer(Modifier.width(8.dp))
            SituationButton(navController)
        }
    }
}

//@Composable
//fun IndexLayout() {
//    val context = LocalContext.current
//    val database = DatabaseHelper.getInstance(context)
//    val reservationsLiveData = database.reservationDao().getAllReservations()
//    val initialReservations = reservationsLiveData.observeAsState(initial = listOf()).value
//    val reservations: MutableState<List<Reservation>> = remember {
//        mutableStateOf(initialReservations)
//    }
//    val coroutineScope = rememberCoroutineScope()
//
//    LaunchedEffect(Unit) {
//        coroutineScope.launch(Dispatchers.IO) {
//            val newReservations = database.reservationDao().getAllReservations().value?: listOf()
//            reservations.value = newReservations
//        }
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize().padding(8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(Modifier.height(16.dp))
//        Text(text = "房间预约", style = TextStyle(color = Color.Black, fontSize = 50.sp))
//        Spacer(Modifier.height(16.dp))
//        ImageGroup()
//        Spacer(Modifier.height(16.dp))
//        val selectedRoomsText = remember { mutableStateOf("") }
//        RoomText(selectedRoomsText)
//        Spacer(Modifier.height(16.dp))
//        val selectedDate = remember { mutableStateOf("") }
//        DateText(selectedDate)
//        Spacer(Modifier.height(16.dp))
//        val selectedStartTime = remember { mutableStateOf("") }
//        StartTimeText(selectedStartTime)
//        Spacer(Modifier.height(16.dp))
//        val selectedEndTime = remember { mutableStateOf("") }
//        EndTimeText(selectedEndTime)
//        Spacer(Modifier.height(16.dp))
//        val userName = remember { mutableStateOf("") }
//        UserName(userName)
//        Spacer(Modifier.height(16.dp))
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            ReservationButton(database, reservations, userName, selectedRoomsText, selectedDate, selectedStartTime, selectedEndTime)
//            Spacer(Modifier.width(8.dp))
//            IModifyButton()
//            Spacer(Modifier.width(8.dp))
//            SituationButton()
//        }
//    }
//}


@Composable
fun ImageGroup(){
    val images = listOf(
        R.drawable.majiang,
        R.drawable.puke,
        R.drawable.xiangqi,
        R.drawable.zhuoyou
    )
    val pageCount = images.size
    val pagerState = rememberPagerState { pageCount }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.height(200.dp)
        ) { page ->
            Box {
                Image(
                    painter = rememberAsyncImagePainter(images[page]),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .width(100.dp)
                        .align(Alignment.BottomCenter)
                ){
                    repeat(images.size){ index ->
                        Box(modifier = Modifier
                            .size(20.dp)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == pagerState.currentPage)
                                    Color(0xFF28E9C9) else
                                    Color.White.copy(alpha = 0.5f))
                        )
                    }
                }
            }
        }

        LaunchedEffect(key1 = Unit) {
            while (true){
                delay(3000L)
                if (!pagerState.isScrollInProgress){
                    scope.launch {
                        pagerState.animateScrollToPage((pagerState.currentPage + 1)% pageCount)
                    }
                }
            }
        }
    }
}


//选择房间
@Composable
fun RoomText(selectedRoomsText: MutableState<String>) {
    val showDialog = remember { mutableStateOf(false) }
    val roomTypes = listOf("麻将室1", "麻将室2", "象棋室1", "象棋室2", "扑克室1", "扑克室2", "桌游室1", "桌游室2")
    val selectedRooms = remember { mutableStateListOf<String>() }

    Text(
        text = if (selectedRoomsText.value.isNotEmpty()) selectedRoomsText.value else "请选择房间类型",
        style = TextStyle(color = Color.Black, fontSize = 35.sp),
        modifier = Modifier.clickable {
            showDialog.value = true
        }
    )

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
            },
            title = {
                Text(text = "选择房间")
            },
            text = {
                Column {
                    roomTypes.forEach { roomType ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedRooms.contains(roomType),
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedRooms.add(roomType)
                                    } else {
                                        selectedRooms.remove(roomType)
                                    }
                                }
                            )
                            Text(text = roomType)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val result = selectedRooms.joinToString(", ")
                        selectedRoomsText.value = result
                        showDialog.value = false
                    }
                ) {
                    Text(text = "确定")
                }
            }
        )
    }
}

//选择日期
@Composable
fun DateText(selectedDate: MutableState<String>) {
    val showDateDialog = remember { mutableStateOf(false) }

    Text(
        text = if (selectedDate.value.isNotEmpty()) selectedDate.value else "请选择预约日期",
        style = TextStyle(color = Color.Black, fontSize = 35.sp),
        modifier = Modifier.clickable { showDateDialog.value = true }
    )

    if (showDateDialog.value) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            { _, year, month, day ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, day)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
                selectedDate.value = sdf.format(selectedCalendar.getTime())
                showDateDialog.value = false
            },
            initialYear,
            initialMonth,
            initialDay
        ).show()
    }
}

//选择开始时间
@Composable
fun StartTimeText(selectedStartTime: MutableState<String>) {
    val showTimeDialog = remember { mutableStateOf(false) }

    Text(
        text = if (selectedStartTime.value.isNotEmpty()) selectedStartTime.value else "请选择开始时间",
        style = TextStyle(color = Color.Black, fontSize = 35.sp),
        modifier = Modifier.clickable { showTimeDialog.value = true }
    )

    if (showTimeDialog.value) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            { _, hour, minute ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(Calendar.HOUR_OF_DAY, hour)
                selectedCalendar.set(Calendar.MINUTE, minute)
                val sdf = SimpleDateFormat("HH:mm", Locale.CHINA)
                selectedStartTime.value = sdf.format(selectedCalendar.getTime())
                showTimeDialog.value = false
            },
            initialHour,
            initialMinute,
            true
        ).show()
    }
}

//选择结束时间
@Composable
fun EndTimeText(selectedEndTime: MutableState<String>) {
    val showTimeDialog = remember { mutableStateOf(false) }

    Text(
        text = if (selectedEndTime.value.isNotEmpty()) selectedEndTime.value else "请选择结束时间",
        style = TextStyle(color = Color.Black, fontSize = 35.sp),
        modifier = Modifier.clickable { showTimeDialog.value = true }
    )

    if (showTimeDialog.value) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            { _, hour, minute ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(Calendar.HOUR_OF_DAY, hour)
                selectedCalendar.set(Calendar.MINUTE, minute)
                val sdf = SimpleDateFormat("HH:mm", Locale.CHINA)
                selectedEndTime.value = sdf.format(selectedCalendar.getTime())
                showTimeDialog.value = false
            },
            initialHour,
            initialMinute,
            true
        ).show()
    }
}

//预约人姓名
@Composable
fun UserName(userName: MutableState<String>) {
    TextField(
        value = userName.value,
        onValueChange = { newText ->
            userName.value = newText
        },
        placeholder = {
            Text("请输入预约人姓名", style = TextStyle(fontSize = 14.sp))
        },
        modifier = Modifier.width(250.dp).height(50.dp),
        colors = TextFieldDefaults.colors(Color.Black)
    )
}

//预约
@Composable
fun ReservationButton(
    database: DatabaseHelper,
    reservations: MutableState<List<Reservation>>,
    userName: MutableState<String>,
    selectedRoomsText: MutableState<String>,
    selectedDate: MutableState<String>,
    selectedStartTime: MutableState<String>,
    selectedEndTime: MutableState<String>
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            if (userName.value.isBlank()
                || selectedRoomsText.value.isBlank()
                || selectedDate.value.isBlank()
                || selectedStartTime.value.isBlank()
                || selectedEndTime.value.isBlank()) {
                Toast.makeText(context, "请将信息输入完整", Toast.LENGTH_SHORT).show()
                return@Button
            }
            try {
                val sdf = SimpleDateFormat("HH:mm", Locale.CHINA)
                val startTime = sdf.parse(selectedStartTime.value)
                if (startTime == null) {
                    Toast.makeText(context, "时间格式解析出错，请检查开始时间格式", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val endTime = sdf.parse(selectedEndTime.value)
                if (endTime == null) {
                    Toast.makeText(context, "时间格式解析出错，请检查结束时间格式", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (startTime.after(endTime)) {
                    Toast.makeText(context, "起始时间不能晚于结束时间，请重新选择时间后再操作", Toast.LENGTH_SHORT).show()
                    return@Button
                }
            } catch (e: Exception) {
                Toast.makeText(context, "时间格式解析出错，请检查输入的时间格式", Toast.LENGTH_SHORT).show()
                return@Button
            }

            coroutineScope.launch {
                val newReservation = Reservation(
                    user = userName.value,
                    room = selectedRoomsText.value,
                    date = selectedDate.value,
                    time1 = selectedStartTime.value,
                    time2 = selectedEndTime.value
                )
                val isDuplicate = ReservationManager.isDuplicateReservation(context, newReservation)
                if (isDuplicate) {
                    Toast.makeText(context, "该时间段已有预约，无法重复预约", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                val reservationId = ReservationManager.insertReservation(context, newReservation)
                if (reservationId!= -1L) {
                    val intent = Intent(context, ReservationActivity::class.java)
                    intent.putExtra("reservationId", reservationId)
                    intent.putExtra("userName", userName.value)
                    intent.putExtra("selectedRoomsText", selectedRoomsText.value)
                    intent.putExtra("selectedDate", selectedDate.value)
                    intent.putExtra("selectedStartTime", selectedStartTime.value)
                    intent.putExtra("selectedEndTime", selectedEndTime.value)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "插入数据库失败", Toast.LENGTH_SHORT).show()
                }
            }
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = "预约", style = TextStyle(color = Color.Black, fontSize = 21.sp))
    }
}



@Composable
fun IModifyButton(){
    Button(onClick = {
    },
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = "修改", style = TextStyle(color = Color.Black, fontSize = 21.sp))
    }
}


//修改
@Composable
fun ModifyButton(
    reservationId: Long,
    userName: MutableState<String>,
    selectedRoomsText: MutableState<String>,
    selectedDate: MutableState<String>,
    selectedStartTime: MutableState<String>,
    selectedEndTime: MutableState<String>
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            if (userName.value.isBlank()
                || selectedRoomsText.value.isBlank()
                || selectedDate.value.isBlank()
                || selectedStartTime.value.isBlank()
                || selectedEndTime.value.isBlank()) {
                Toast.makeText(context, "请将信息输入完整", Toast.LENGTH_SHORT).show()
                return@Button
            }
            val sdf = SimpleDateFormat("HH:mm", Locale.CHINA)
            try {
                val startTime = sdf.parse(selectedStartTime.value)
                val endTime = sdf.parse(selectedEndTime.value)
                if (startTime == null || endTime == null) {
                    Toast.makeText(context, "时间格式解析出错，请检查输入的时间格式", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (startTime.after(endTime)) {
                    Toast.makeText(context, "起始时间不能晚于结束时间，请重新选择时间后再操作", Toast.LENGTH_SHORT).show()
                    return@Button
                }
            } catch (e: Exception) {
                Toast.makeText(context, "时间格式解析出错，请检查输入的时间格式", Toast.LENGTH_SHORT).show()
                return@Button
            }
            coroutineScope.launch {
                try {
                    val modifiedReservation = Reservation(
                        id = reservationId,
                        user = userName.value,
                        room = selectedRoomsText.value,
                        date = selectedDate.value,
                        time1 = selectedStartTime.value,
                        time2 = selectedEndTime.value
                    )
                    // 检查是否存在重复预约
                    if (ReservationManager.isDuplicateReservation(context, modifiedReservation)) {
                        Toast.makeText(context, "该时间段已有预约，无法重复预约", Toast.LENGTH_SHORT).show()
                        return@launch
                    }
                    // 更新预约
                    ReservationManager.updateReservation(context, modifiedReservation)
                    // 跳转到 SituationActivity 并传递必要的信息
                    val intent = Intent(context, SituationActivity::class.java)
                    intent.putExtra("reservationId", reservationId)
                    intent.putExtra("userName", userName.value)
                    intent.putExtra("selectedRoomsText", selectedRoomsText.value)
                    intent.putExtra("selectedDate", selectedDate.value)
                    intent.putExtra("selectedStartTime", selectedStartTime.value)
                    intent.putExtra("selectedEndTime", selectedEndTime.value)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "修改预约失败: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = "修改", style = TextStyle(color = Color.Black, fontSize = 21.sp))
    }
}

//查看预约情况
@Composable
fun SituationButton(navController: NavController) {
//    val context = LocalContext.current
    Button(
        onClick = {
            navController.navigate(ScreenPage.List.route) { // 跳转到预约情况界面
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
//            val intent = Intent(context, SituationActivity::class.java)
//            context.startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = "查看预约情况", style = TextStyle(color = Color.Black, fontSize = 21.sp))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CardRoom1Theme {
//        IndexLayout()
    }
}
