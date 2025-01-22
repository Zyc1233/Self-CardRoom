package com.example.cardroom1

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardroom1.ui.theme.CardRoom1Theme
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.bundleOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
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
                val viewModel: ReservationViewModel = viewModel()
                IndexApp(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}


@Composable
fun IndexApp(
    navController: NavController,
    viewModel: ReservationViewModel,
) {
    val selectedRoomsTextState = remember { mutableStateOf("") }
    val selectedDateState = remember { mutableStateOf("") }
    val selectedStartTimeState = remember { mutableStateOf("") }
    val selectedEndTimeState = remember { mutableStateOf("") }
    val userNameState = remember { mutableStateOf("") }
    val reservationId = remember { mutableLongStateOf(0L) }

    // 获取从 Reservation 界面传递回来的修改信息
    val reservationData = navController.previousBackStackEntry?.savedStateHandle?.get<Bundle>("reservationData")
    if (reservationData!= null) {
        userNameState.value = reservationData.getString("userName", "")!!
        selectedRoomsTextState.value = reservationData.getString("selectedRoomsText", "")!!
        selectedDateState.value = reservationData.getString("selectedDate", "")!!
        selectedStartTimeState.value = reservationData.getString("selectedStartTime", "")!!
        selectedEndTimeState.value = reservationData.getString("selectedEndTime", "")!!
        reservationId.longValue = reservationData.getLong("reservationId", 0L)
        navController.previousBackStackEntry?.savedStateHandle?.remove<Bundle>("reservationData")
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp).imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        ImageGroup()
        Spacer(Modifier.height(16.dp))
        RoomText(selectedRoomsTextState)
        Spacer(Modifier.height(16.dp))
        DateText(selectedDateState)
        Spacer(Modifier.height(16.dp))
        StartTimeText(selectedStartTimeState)
        Spacer(Modifier.height(16.dp))
        EndTimeText(selectedEndTimeState)
        Spacer(Modifier.height(16.dp))
        UserName(userNameState)
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            ReservationButton(
                viewModel,
                userNameState,
                selectedRoomsTextState,
                selectedDateState,
                selectedStartTimeState,
                selectedEndTimeState,
                navController
            )
            Spacer(Modifier.width(8.dp))
            ModifyButton(
                reservationId.longValue,
                userNameState,
                selectedRoomsTextState,
                selectedDateState,
                selectedStartTimeState,
                selectedEndTimeState,
                navController,
                viewModel
            )
            Spacer(Modifier.width(8.dp))
            SituationButton(navController)
        }
    }
}


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
        text = if (selectedRoomsText.value.isNotEmpty())  selectedRoomsText.value else "请选择房间类型",
        fontSize = 35.sp,
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
        text = if (selectedDate.value.isNotEmpty())  selectedDate.value else "请选择预约日期",
        fontSize = 35.sp,
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
                selectedDate.value = sdf.format(selectedCalendar.time)
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
        text = if (selectedStartTime.value.isNotEmpty())  selectedStartTime.value else "请选择开始时间",
        fontSize = 35.sp,
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
                selectedStartTime.value = sdf.format(selectedCalendar.time)
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
        text = if (selectedEndTime.value.isNotEmpty())  selectedEndTime.value else "请选择结束时间",
        fontSize = 35.sp,
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
                selectedEndTime.value = sdf.format(selectedCalendar.time)
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
            Text("请输入预约人姓名", fontSize = 14.sp)
        },
        modifier = Modifier.width(250.dp).height(60.dp),
        colors = TextFieldDefaults.colors(Color.Black)
    )
}

// 预约
@Composable
fun ReservationButton(
    viewModel: ReservationViewModel,
    userName: MutableState<String>,
    selectedRoomsText: MutableState<String>,
    selectedDate: MutableState<String>,
    selectedStartTime: MutableState<String>,
    selectedEndTime: MutableState<String>,
    navController: NavController
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isLoading = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) } // 新增：用于控制弹窗的显示

    Button(
        onClick = {
            showDialog.value = true // 点击按钮时显示弹窗
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray),
        enabled =!isLoading.value
    ) {
        if (isLoading.value) {
            CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(20.dp))
        } else {
            Text(text = "预约", fontSize = 21.sp)
        }
    }

    if (showDialog.value) { // 显示弹窗
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "确认预约") },
            text = { Text("是否确认预约？") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                        if (!isLoading.value) {
                            isLoading.value = true
                            coroutineScope.launch {
                                try {
                                    val newReservation = Reservation(
                                        user = userName.value,
                                        room = selectedRoomsText.value,
                                        date = selectedDate.value,
                                        time1 = selectedStartTime.value,
                                        time2 = selectedEndTime.value
                                    )
                                    Log.d("ReservationButton", "开始验证预约信息：$newReservation")
                                    if (!NavigationUtil.validateReservation(newReservation)) {
                                        Toast.makeText(context, "时间格式错误或起始时间晚于结束时间", Toast.LENGTH_SHORT).show()
                                        isLoading.value = false
                                        Log.d("ReservationButton", "预约信息验证失败：$newReservation")
                                        return@launch
                                    }
                                    Log.d("ReservationButton", "开始检查重复预约：$newReservation")
                                    if (viewModel.isDuplicateReservation(newReservation)) {
                                        Toast.makeText(context, "该时间段已有预约，无法重复预约", Toast.LENGTH_SHORT).show()
                                        isLoading.value = false
                                        Log.d("ReservationButton", "存在重复预约：$newReservation")
                                        return@launch
                                    }
                                    val reservationId = viewModel.insertReservation(newReservation)
                                    if (reservationId!= null) {
                                        val bundle = bundleOf(
                                            "reservationId" to reservationId,
                                            "userName" to userName.value,
                                            "selectedRoomsText" to selectedRoomsText.value,
                                            "selectedDate" to selectedDate.value,
                                            "selectedStartTime" to selectedStartTime.value,
                                            "selectedEndTime" to selectedEndTime.value
                                        )
                                        Log.d("NavigationUtil", "Put data: $bundle")

                                        navController.navigate(route = "${ScreenPage.Reservation.route}/$reservationId")
                                        Toast.makeText(context,"预约成功",Toast.LENGTH_SHORT).show()

                                        Log.d("ReservationButton", "预约成功，插入数据库: ID=${reservationId}, 房间=${newReservation.room}, 日期=${newReservation.date}, 时间=${newReservation.time1}-${newReservation.time2}")
                                    } else {
                                        Log.d("ReservationButton", "预约失败，插入预约信息返回 null")
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "预约失败: ${e.message}", Toast.LENGTH_SHORT).show()
                                    Log.e("ReservationButton", "预约失败，异常信息: ${e.message}", e)
                                } finally {
                                    isLoading.value = false
                                }
                            }
                        }
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog.value = false }
                ) {
                    Text("取消")
                }
            }
        )
    }
}

// 修改
@Composable
fun ModifyButton(
    reservationId: Long,
    userName: MutableState<String>,
    selectedRoomsText: MutableState<String>,
    selectedDate: MutableState<String>,
    selectedStartTime: MutableState<String>,
    selectedEndTime: MutableState<String>,
    navController: NavController,
    viewModel: ReservationViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isLoading = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) } // 新增：用于控制弹窗的显示

    Button(
        onClick = {
            showDialog.value = true // 点击按钮时显示弹窗
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray),
        enabled =!isLoading.value
    ) {
        if (isLoading.value) {
            CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(20.dp))
        } else {
            Text(text = "修改", fontSize = 21.sp)
        }
    }

    if (showDialog.value) { // 显示弹窗
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "确认修改") },
            text = { Text("是否确认修改预约信息？") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                        if (!isLoading.value) {
                            isLoading.value = true
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
                                    if (!NavigationUtil.validateReservation(modifiedReservation)) {
                                        Toast.makeText(context, "时间格式错误或起始时间晚于结束时间", Toast.LENGTH_SHORT).show()
                                        return@launch
                                    }
                                    if (viewModel.isDuplicateReservation(modifiedReservation)) {
                                        Toast.makeText(context, "该时间段已有预约，无法重复预约", Toast.LENGTH_SHORT).show()
                                        return@launch
                                    }
                                    viewModel.updateReservation(modifiedReservation)
                                    val bundle = bundleOf(
                                        "reservationId" to reservationId,
                                        "userName" to userName.value,
                                        "selectedRoomsText" to selectedRoomsText.value,
                                        "selectedDate" to selectedDate.value,
                                        "selectedStartTime" to selectedStartTime.value,
                                        "selectedEndTime" to selectedEndTime.value
                                    )
                                    Log.d("NavigationUtil", "Modify data: $bundle")
                                    navController.navigate(ScreenPage.List.route)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "修改预约失败: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isLoading.value = false
                                }
                            }
                        }
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog.value = false }
                ) {
                    Text("取消")
                }
            }
        )
    }
}


//查看预约情况
@Composable
fun SituationButton(navController: NavController) {
    Button(
        onClick = {
            navController.navigate(ScreenPage.List.route)
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = "查看预约情况",  fontSize = 21.sp)
    }
}


