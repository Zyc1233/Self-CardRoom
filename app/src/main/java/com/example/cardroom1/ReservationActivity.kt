package com.example.cardroom1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardroom1.ui.theme.CardRoom1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReservationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardRoom1Theme {
                val reservationId = intent.getLongExtra("reservationId", -1L)
                val userName = intent.getStringExtra("userName") ?: ""
                val selectedRoomsText = intent.getStringExtra("selectedRoomsText") ?: ""
                val selectedDate = intent.getStringExtra("selectedDate") ?: ""
                val selectedStartTime = intent.getStringExtra("selectedStartTime") ?: ""
                val selectedEndTime = intent.getStringExtra("selectedEndTime") ?: ""
                ReservationApp(reservationId, userName, selectedRoomsText, selectedDate, selectedStartTime, selectedEndTime)
            }
        }
    }
}


@Composable
fun ReservationLayout(
    reservationId: Long,
    userName: String,
    selectedRoomsText: String,
    selectedDate: String,
    selectedStartTime: String,
    selectedEndTime: String
) {
    val context = LocalContext.current
    val database = DatabaseHelper.getInstance(context)
    val reservation = remember { mutableStateOf<Reservation?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(reservationId) {
        withContext(Dispatchers.IO) {
            val allReservations = database.reservationDao().getAllReservations().value?: listOf()
            reservation.value = allReservations.find { it.id == reservationId }
            isLoading.value = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "预约信息",
            style = TextStyle(color = Color.Black, fontSize = 50.sp)
        )
        Spacer(Modifier.height(32.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = " 用 户 名： ", style = TextStyle(color = Color.Black, fontSize = 35.sp))
            Spacer(Modifier.width(8.dp))
            Text(text = userName, style = TextStyle(color = Color.Black, fontSize = 35.sp))
            Spacer(Modifier.width(120.dp))
        }
        Spacer(Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "预约房间：", style = TextStyle(color = Color.Black, fontSize = 35.sp))
            Spacer(Modifier.width(8.dp))
            Text(text = selectedRoomsText, style = TextStyle(color = Color.Black, fontSize = 35.sp))
            Spacer(Modifier.width(100.dp))
        }
        Spacer(Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "预约时间：", style = TextStyle(color = Color.Black, fontSize = 35.sp))
            Spacer(Modifier.width(8.dp))
            Text(text = "$selectedDate $selectedStartTime - $selectedEndTime",
                style = TextStyle(color = Color.Black, fontSize = 35.sp))
        }
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(40.dp))
            CancelButton(database, reservationId)
            Spacer(Modifier.width(16.dp))
            SRoomButton()
        }
    }
}


//取消预约
@Composable
fun CancelButton(
    database: DatabaseHelper,
    reservationId: Long
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text("确认取消预约") },
            text = { Text("您确定要取消此次预约吗？") },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val reservation = database.reservationDao().getReservationById(reservationId)
                            if (reservation != null){
                                database.reservationDao().deleteReservationById(reservationId)
                            }
                        }
                        val intent = Intent(context, IndexActivity::class.java)
                        context.startActivity(intent)
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
        Text(text = "取消预约", style = TextStyle(color = Color.Black, fontSize = 25.sp))
    }
}


@Composable
fun SRoomButton() {
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(context, RoomActivity::class.java)
            context.startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = "前往房间", style = TextStyle(color = Color.Black), fontSize = 25.sp)
    }
}


@Composable
fun ReservationApp(
    reservationId: Long,
    userName: String,
    selectedRoomsText: String,
    selectedDate: String,
    selectedStartTime: String,
    selectedEndTime: String) {
    ReservationLayout(
        reservationId,
        userName,
        selectedRoomsText,
        selectedDate,
        selectedStartTime,
        selectedEndTime)
}

@Preview(showBackground = true)
@Composable
fun ReservationPreview2() {
    CardRoom1Theme {
        ReservationLayout(
            reservationId = 0,
            userName = "张三",
            selectedRoomsText = "麻将室1",
            selectedDate = "2023-12-25",
            selectedStartTime = "14:00",
            selectedEndTime = "16:00"
        )
    }
}