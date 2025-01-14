package com.example.cardroom1

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cardroom1.ui.theme.CardRoom1Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ForgetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardRoom1Theme {
                val navController = rememberNavController()
                ForgetApp(navController)
            }
        }
    }
}

@Composable
fun ForgetLayout(navController: NavController) {
    val context = LocalContext.current
    val phone = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val code = remember { mutableStateOf("") }
    val generatedCode = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            CommonPhoneText(phone, stringResource(R.string.phone))
            Spacer(modifier = Modifier.height(8.dp))
            NewPasswordText(newPassword, stringResource(R.string.first_password), context)
            Spacer(modifier = Modifier.height(8.dp))
            NewPasswordText(confirmPassword, stringResource(R.string.second_password), context)
            Spacer(modifier = Modifier.height(8.dp))
            ForgetCode(context, code, generatedCode)
            Spacer(modifier = Modifier.height(8.dp))
            ReSetButton(
                context,
                coroutineScope,
                phone.value,
                newPassword.value,
                confirmPassword.value,
                code.value,
                generatedCode.value,
                navController
            )
        }
    }
}


@Composable
fun ForgetCode(
    context: Context,
    codeState: MutableState<String>,
    generatedCodeState: MutableState<String>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(5.dp))
        Text(stringResource(R.string.code))
        Spacer(modifier = Modifier.width(4.dp))
        TextField(
            value = codeState.value,
            onValueChange = {
                if (it.length == 6) {
                    codeState.value = it
                } else {
                    val random = kotlin.random.Random
                    generatedCodeState.value = (100000..999999).random(random).toString()
                    codeState.value = generatedCodeState.value
                    Toast.makeText(context, "请输入验证码", Toast.LENGTH_SHORT).show()
                }
            },
            placeholder = { Text(text = "请输入验证码", fontSize = 16.sp, color = Color.Black) },
            modifier = Modifier
                .width(140.dp)
                .height(55.dp)
                .imePadding(),
            colors = TextFieldDefaults.colors(Color.White)
        )
        Spacer(modifier = Modifier.width(8.dp))
        ForgetCodeButton(codeState, generatedCodeState)
    }
}

@Composable
fun ForgetCodeButton(codeState: MutableState<String>, generatedCodeState: MutableState<String>) {
    val context = LocalContext.current
    Button(
        onClick = {
            val random = kotlin.random.Random
            generatedCodeState.value = (100000..999999).random(random).toString()
            codeState.value = generatedCodeState.value
            Toast.makeText(context, "验证码已重新发送", Toast.LENGTH_SHORT).show()
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray),
        modifier = Modifier
            .width(120.dp)
            .height(55.dp)
    ) {
        Text(
            text = stringResource(R.string.btn_code),
            style = TextStyle(fontSize = 14.sp),
            color = Color.Black
        )
    }
}

@Composable
fun ReSetButton(
    context: Context,
    coroutineScope: CoroutineScope,
    phone: String,
    newPassword: String,
    confirmPassword: String,
    code: String,
    generatedCode: String,
    navController: NavController
) {
    val dataStore = context.dataStore
    Button(
        onClick = {
            coroutineScope.launch {
                if (phone.isBlank() || newPassword.isBlank() || confirmPassword.isBlank() || code.isBlank()) {
                    context.showToast("请将信息填写完整！")
                } else if (newPassword == confirmPassword && code == generatedCode) {
                    val phoneKey = stringPreferencesKey("phone")
                    val passwordKey = stringPreferencesKey("password")
                    dataStore.edit { preferences ->
                        preferences[phoneKey] = phone
                        preferences[passwordKey] = newPassword
                    }
                    val userDao = DatabaseProvider.getDatabase(context)
                    try {
                        userDao.resetUserPassword(phone, newPassword)
                        Toast.makeText(context, "密码重置成功", Toast.LENGTH_SHORT).show()
                        navController.navigate(ScreenPage.Login.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        Log.v("Forget","重置成功")
                    } catch (e: Exception) {
                        Toast.makeText(context, "用户不存在或重置失败", Toast.LENGTH_SHORT).show()
                        Log.v("Forget","重置失败")
                    }
                } else {
                    Toast.makeText(context, "两次密码不一致或验证码错误", Toast.LENGTH_SHORT).show()
                    Log.v("Forget","信息错误")
                }
            }
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text(
            text = stringResource(R.string.btn_reset),
            color = Color.Black,
            fontSize = 25.sp
        )
    }
}

@Composable
fun ForgetApp(navController: NavController) {
    ForgetLayout(navController)
}


@Preview(showBackground = true)
@Composable
fun ForgetPreview() {
    CardRoom1Theme {
        val navController = rememberNavController()
        ForgetLayout(navController)
    }
}