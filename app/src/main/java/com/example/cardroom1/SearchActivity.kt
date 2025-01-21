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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cardroom1.ui.theme.CardRoom1Theme
import kotlinx.coroutines.flow.firstOrNull

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardRoom1Theme {
            }
        }
    }
}

@Composable
fun SearchApp(navController: NavController){
    val userNameState = remember { mutableStateOf("") }
    val context = LocalContext.current
    val database = ReservationDatabase.getInstance(context)
    val reservationsFlow = database.reservationDao().getAllReservations()
    val reservationsLiveData = reservationsFlow.asLiveData() //
    val reservations by reservationsLiveData.observeAsState(initial = emptyList())
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
    SearchLayout(userNameState,reservations,navController,viewModel)
}

@Composable
fun SearchLayout(
    userName: MutableState<String>,
    reservations: List<Reservation>,
    navController: NavController,
    viewModel: ReservationViewModel
) {
    val listState = rememberLazyListState()
    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(4.dp))
            Text(text = stringResource(R.string.search), fontSize = 25.sp)
            Spacer(Modifier.width(4.dp))
            TextField(
                value = userName.value,
                onValueChange = { newText ->
                    userName.value = newText
                },
                placeholder = {
                    Text("请输入预约人姓名", fontSize = 16.sp, color = Color.Black)
                },
                modifier = Modifier.width(160.dp).height(55.dp).imePadding(),
                colors = TextFieldDefaults.colors(Color.Black)
            )
            Spacer(Modifier.width(4.dp))
            SearchButton(userName, viewModel)
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth(),
            state = listState
        ) {
            val filteredReservations = if (userName.value.isNotEmpty()) {
                // 观察搜索结果
                viewModel.reservations.value.filter { it.user.contains(userName.value) }
            } else {
                // 显示全部
                reservations
            }
            if (filteredReservations.isEmpty()) {
                item {
                    Text("没有找到匹配的预约", fontSize = 20.sp, color = Color.Red)
                }
            } else {
                items(filteredReservations) { reservation ->
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
                        SRoomButton(navController,reservation)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchButton(userNameState: MutableState<String>, viewModel: ReservationViewModel){
    Button(onClick = {
        viewModel.searchReservationsByName(userNameState.value)
    },
        colors = ButtonDefaults.buttonColors(Color.LightGray)) {
        Text(text = "搜索", fontSize = 25.sp)
    }
}


