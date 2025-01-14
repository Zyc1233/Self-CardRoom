package com.example.cardroom1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cardroom1.ui.theme.CardRoom1Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class SituationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardRoom1Theme {
                val navController = rememberNavController()
                SituationApp(navController)
            }
        }
    }
}

@Composable
fun SituationApp(navController: NavController) {
    val context = LocalContext.current
    val database = ReservationDatabase.getInstance(context)
    val reservationsFlow = database.reservationDao().getAllReservations() // 这里假设返回的是 Flow
    val reservationsLiveData = reservationsFlow.asLiveData() // 将 Flow 转换为 LiveData
    val reservations by reservationsLiveData.observeAsState(initial = emptyList()) // 确保初始值类型匹配
    val modifiedReservationId = navController.previousBackStackEntry?.savedStateHandle?.get<Long>("modifiedReservationId")
    val viewModel: ReservationViewModel = viewModel()
    LaunchedEffect(modifiedReservationId) {
        if (modifiedReservationId != null) {
            Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show()
            navController.previousBackStackEntry?.savedStateHandle?.remove<Long>("modifiedReservationId")
            // 刷新预约列表
            val newReservations = database.reservationDao().getAllReservations().firstOrNull() ?: emptyList()
            (reservationsLiveData as MutableLiveData<List<Reservation>>).postValue(newReservations)
        }
    }
    SituationLayout(reservations, navController, viewModel) // 传递 ViewModel 实例
}


@Composable
fun SituationLayout(
    reservations: List<Reservation>,
    navController: NavController,
    viewModel: ReservationViewModel
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // 使用 derivedStateOf 派生状态
    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index == totalItems - 1
        }
    }

    val scrollFraction by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val firstVisibleIndex = listState.firstVisibleItemIndex
            firstVisibleIndex / (totalItems - 1).toFloat()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxWidth(),
                state = listState
            ) {
                items(reservations) { reservation ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "用户: ${reservation.user}", style = TextStyle(color = Color.Black, fontSize = 20.sp))
                        Spacer(Modifier.width(16.dp))
                        Text(text = "房间: ${reservation.room}", style = TextStyle(color = Color.Black, fontSize = 20.sp))
                        Spacer(Modifier.width(16.dp))
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "日期: ${reservation.date}", style = TextStyle(color = Color.Black, fontSize = 20.sp))
                        Spacer(Modifier.width(16.dp))
                        Text(text = "时间: ${reservation.time1} - ${reservation.time2}", style = TextStyle(color = Color.Black, fontSize = 20.sp))
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SCancelButton(reservation, viewModel)
                        Spacer(Modifier.width(4.dp))
                        SModifyButton(reservation, navController)
                        Spacer(Modifier.width(4.dp))
                        RoomButton(navController)
                    }
                }
            }

            // 自定义滚动条
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(16.dp)
                    .background(Color.LightGray)
                    .pointerInput(listState) {
                        detectVerticalDragGestures { _, dragAmount ->
                            coroutineScope.launch {
                                listState.scrollBy(dragAmount)
                            }
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .background(Color.Black)
                        .align(Alignment.TopCenter)
                        .offset {
                            IntOffset(0, (scrollFraction * (40)).roundToInt())
                        }
                )
            }
        }
        if (isAtBottom) {
            Text(text = "已到底部", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        TopBack(coroutineScope, listState)
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackButton(navController)
            Spacer(Modifier.width(16.dp))
            UpdateButton()
        }
    }
}



@Composable
fun TopBack(coroutineScope: CoroutineScope, listState: LazyListState) {
    val context = LocalContext.current
    Image(
        modifier = Modifier
            .padding(end = 10.dp, bottom = 10.dp)
            .width(45.dp)
            .height(45.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable {
                coroutineScope.launch {
                    listState.animateScrollToItem(index = 0)
                    Toast.makeText(context,"已到达顶部",Toast.LENGTH_SHORT).show()
                }
            },
        painter = painterResource(id = R.drawable.top),
        contentDescription = "返回顶部图标"
    )
}






@Composable
fun SCancelButton(reservation: Reservation, viewModel: ReservationViewModel) {
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text("确认取消预约") },
            text = { Text("您确定要取消此次预约吗？") },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.deleteReservation(reservation.id)
                            Toast.makeText(context, "取消成功", Toast.LENGTH_SHORT).show()
                            openDialog.value = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color.LightGray)
                ) {
                    Text(text = "确定", style = TextStyle(color = Color.Black, fontSize = 25.sp))
                }
            },
            dismissButton = {
                Button(
                    onClick = { openDialog.value = false },
                    colors = ButtonDefaults.buttonColors(Color.LightGray)
                ) {
                    Text(text = "取消", style = TextStyle(color = Color.Black, fontSize = 25.sp))
                }
            }
        )
    }

    Button(
        onClick = { openDialog.value = true },
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = "取消预约", style = TextStyle(color = Color.Black, fontSize = 16.sp))
    }
}



@Composable
fun SModifyButton(
    reservation: Reservation,
    navController: NavController
) {
    Button(
        onClick = {
            val bundle = Bundle().apply {
                putLong("reservationId", reservation.id)
                putString("userName", reservation.user)
                putString("selectedRoomsText", reservation.room)
                putString("selectedDate", reservation.date)
                putString("selectedStartTime", reservation.time1)
                putString("selectedEndTime", reservation.time2)
            }
            navController.navigate(ScreenPage.Index.route) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
            navController.previousBackStackEntry?.savedStateHandle?.set("reservationData", bundle)
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = "修改预约", style = TextStyle(color = Color.Black, fontSize = 16.sp))
    }
}



@Composable
fun BackButton(navController: NavController) {
    Button(
        onClick = {
            isUserLoggedIn.value = false
            navController.navigate(ScreenPage.Login.route){
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = "退出登录", style = TextStyle(color = Color.Black, fontSize = 30.sp))
    }
}

@Composable
fun UpdateButton(onUpdate: () -> Unit = {}) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Button(
        onClick = {
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    val database = ReservationDatabase.getInstance(context)
                    database.reservationDao().getAllReservations().firstOrNull() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        onUpdate()
                        Toast.makeText(context, "预约信息已更新", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = "更新", style = TextStyle(color = Color.Black, fontSize = 30.sp))
    }
}

@Composable
fun RoomButton(
    navController: NavController
) {
    Button(
        onClick = {
            navController.navigate(ScreenPage.Room.route) { // 跳转到房间界面
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = "前往房间", style = TextStyle(color = Color.Black), fontSize = 25.sp)
    }
}




