package com.example.cardroom1

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cardroom1.ui.theme.CardRoom1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ReservationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardRoom1Theme {
                val navController = rememberNavController()
                val viewModel: ReservationViewModel = viewModel()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val reservationId = currentBackStackEntry?.arguments?.getLong("reservationId")?: -1L
                ReservationApp(
                    navController = navController,
                    viewModel = viewModel,
                    reservationId
                )
            }
        }
    }
}

@Composable
fun ReservationApp(
    navController: NavController,
    viewModel: ReservationViewModel,
    reservationId: Long
) {
    val reservation = remember { mutableStateOf<Reservation?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    LaunchedEffect(reservationId) {
        launch(Dispatchers.IO) {
            val res = viewModel.getReservationById(reservationId)
            reservation.value = res
            isLoading.value = false
        }
    }

    if (isLoading.value) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        reservation.value?.let { res ->
            ReservationLayout(
                reservationId = res.id,
                userName = res.user,
                selectedRoomsText = res.room,
                selectedDate = res.date,
                selectedStartTime = res.time1,
                selectedEndTime = res.time2,
                navController = navController,
                viewModel = viewModel
            )
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
    selectedEndTime: String,
    navController: NavController,
    viewModel: ReservationViewModel
) {
    val reservation = remember { mutableStateOf<Reservation?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    LaunchedEffect(reservationId) {
        withContext(Dispatchers.IO) {
            val allReservations = viewModel.getReservationById(reservationId)
            reservation.value = allReservations
            isLoading.value = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
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
            CancelButton(reservationId, navController, viewModel)
            Spacer(Modifier.width(16.dp))
            RoomButton(navController)
        }
    }
}


// 取消预约
@Composable
fun CancelButton(
    reservationId: Long,
    navController: NavController,
    viewModel: ReservationViewModel
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
                            viewModel.deleteReservation(reservationId)
                        }
                        navController.navigate(ScreenPage.Index.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
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
        Text(text = "取消预约", style = TextStyle(color = Color.Black, fontSize = 25.sp))
    }
}



@Composable
fun RoomButton(navController: NavController) {
    Button(
        onClick = {
            navController.navigate(ScreenPage.Room.route){
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


