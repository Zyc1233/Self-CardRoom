package com.example.cardroom1

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cardroom1.ui.theme.CardRoom1Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardRoom1Theme {
                val navController = rememberNavController()
                RegisterApp(navController)
            }
        }
    }
}

@Composable
fun RegisterLayout(navController: NavController) {
    val context = LocalContext.current
    val phone = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            CommonPhoneText(phone)
            Spacer(Modifier.height(16.dp))
            NewPasswordText(password, context)
            Spacer(Modifier.height(16.dp))
            NewPasswordText(confirmPassword,context)
            Spacer(Modifier.height(16.dp))
            NewRegisterButton(
                context,
                coroutineScope,
                phone.value,
                password.value,
                confirmPassword.value,
                navController
            )
        }
    }
}

@Composable
fun NewRegisterButton(
    context: Context,
    coroutineScope: CoroutineScope,
    phone: String,
    password: String,
    confirmPassword: String,
    navController: NavController
) {
    val dataStore = context.dataStore
    Button(
        onClick = {
            coroutineScope.launch {
                Log.d("RegisterActivity", "开始注册流程")
                if (phone.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    context.showToast("请将信息填写完整！")
                } else if (password == confirmPassword) {
                    try {
                        val phoneKey = stringPreferencesKey("phone")
                        val passwordKey = stringPreferencesKey("password")
                        dataStore.edit { preferences ->
                            preferences[phoneKey] = phone
                            preferences[passwordKey] = password
                        }
                        context.showToast("注册成功")
                        navController.navigate(ScreenPage.Login.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        Log.d("RegisterActivity", "注册成功，跳转到登录页面")
                    } catch (e: Exception) {
                        context.showToast("注册失败: ${e.message}")
                        Log.e("RegisterActivity", "注册失败: ${e.message}", e)
                    }
                } else {
                    context.showToast("两次密码不一致")
                    Log.d("RegisterActivity", "两次密码不一致")
                }
            }
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray),
        modifier = Modifier.fillMaxWidth().height(50.dp)
    ) {
        Text(text = stringResource(R.string.btn_register), color = Color.Black, fontSize = 25.sp)
    }
}


@Composable
fun NewPasswordText(passwordState: MutableState<String>,context: Context) {
    val minLength = 6
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.new_pasword),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        val passwordVisibility = remember { mutableStateOf(false) }
        val updatePassword = { newPassword: String ->
            passwordState.value = newPassword
            if (newPassword.length < minLength && newPassword.isNotEmpty()) {
                Toast.makeText(context, "密码长度至少为$minLength 位", Toast.LENGTH_SHORT).show()
            }
        }
        TextField(
            value = passwordState.value,
            onValueChange = { newPassword ->
                updatePassword(newPassword)
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            placeholder = { Text("请输入密码", fontSize = 16.sp) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            trailingIcon = {
                val image =
                    if (passwordVisibility.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisibility.value) "隐藏密码" else "显示密码"
                IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
fun RegisterApp(navController: NavController) {
    RegisterLayout(navController)
}


