package com.example.cardroom1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cardroom1.ui.theme.CardRoom1Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    val database = DatabaseHelper.getInstance(context)
    val reservationsLiveData = database.reservationDao().getAllReservations()
    val reservations by reservationsLiveData.observeAsState(initial = listOf())
    SituationLayout(reservations,navController)
}

@Composable
fun SituationLayout(
    reservations: List<Reservation>,
    navController: NavController) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val visibleItems = layoutInfo.visibleItemsInfo.size
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index?: 0
            lastVisibleIndex + visibleItems >= totalItems
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = "预约情况",
            style = TextStyle(color = Color.Black, fontSize = 50.sp)
        )
        Spacer(Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth().height(450.dp),
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
                    SCancelButton(reservation)
                    Spacer(Modifier.width(4.dp))
                    SModifyButton(reservation)
                    Spacer(Modifier.width(4.dp))
                    RoomButton(navController)
                }
            }
        }
        if (isAtBottom) {
            Text(text = "已到底部", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        TopBack(coroutineScope, listState)
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackButton()
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
fun SCancelButton(reservation: Reservation) {
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
                            withContext(Dispatchers.IO) {
                                ReservationManager.deleteReservationById(context, reservation.id)
                            }
                        }
                        Toast.makeText(context, "取消成功", Toast.LENGTH_SHORT).show()
                        openDialog.value = false
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
fun SModifyButton(reservation: Reservation) {
    val context = LocalContext.current
    val intent = Intent(context, IndexActivity::class.java)
    intent.putExtra("reservationId", reservation.id)
    intent.putExtra("userName", reservation.user)
    intent.putExtra("selectedRoomsText", reservation.room)
    intent.putExtra("selectedDate", reservation.date)
    intent.putExtra("selectedStartTime", reservation.time1)
    intent.putExtra("selectedEndTime", reservation.time2)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val modifiedId = result.data?.getLongExtra("modifiedReservationId", -1L)
            if (modifiedId!= -1L) {
                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show()
            }
        }
    }
    Button(
        onClick = {
            launcher.launch(intent)
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = "修改预约", style = TextStyle(color = Color.Black, fontSize = 16.sp))
    }
}


@Composable
fun BackButton() {
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
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
                    val database = DatabaseHelper.getInstance(context)
                    database.reservationDao().getAllReservations().value ?: listOf()
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
//    val context = LocalContext.current
    Button(
        onClick = {
            navController.navigate(ScreenPage.Room.route) { // 跳转到房间界面
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
//            val intent = Intent(context, RoomActivity::class.java)
//            context.startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = "前往房间", style = TextStyle(color = Color.Black), fontSize = 25.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun SituationPreview() {
    CardRoom1Theme {
//        SituationLayout(listOf(
//            Reservation(id = 1, user = "张三", room = "麻将室1", date = "2023-12-25", time1 = "14:00", time2 = "16:00"),
//            Reservation(id = 2, user = "李四", room = "象棋室1", date = "2023-12-26", time1 = "15:00", time2 = "17:00")
//        ))
    }
}


