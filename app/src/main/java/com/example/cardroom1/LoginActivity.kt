package com.example.cardroom1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cardroom1.ui.theme.CardRoom1Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name = "user_preferences")

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardRoom1Theme {
                val navController = rememberNavController()
                LoginApp(navController)
            }
        }
    }
}




@Composable
fun LoginApp(navController: NavController) {
    val loginOptions = listOf("密码登录", "验证码登录")
    var selectedLoginOption by remember { mutableStateOf(loginOptions.first()) }
    val phone = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            items(loginOptions) { option ->
                LoginOptionRadioButton(
                    text = option,
                    selected = option == selectedLoginOption,
                    onSelected = { selectedLoginOption = it }
                )
            }
        }

        when (selectedLoginOption) {
            "密码登录" -> PasswordLoginLayout(phone, password, navController)
            "验证码登录" -> CodeLoginLayout(phone, navController)
        }
    }
}


@Composable
fun LoginOptionRadioButton(
    text: String,
    selected: Boolean,
    onSelected: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 8.dp)
            .clickable { onSelected(text) }
    ) {
        RadioButton(
            selected = selected,
            onClick = null // 点击事件在外部处理
        )
        Text(text = text)
    }
}

@Composable
fun PasswordLoginLayout(
    phoneState: MutableState<String>,
    passwordState: MutableState<String>,
    navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(64.dp))
            Text(text = "密码登录", style = TextStyle(fontSize = 50.sp))
            Spacer(modifier = Modifier.height(32.dp))
            CommonPhoneText(phoneState, stringResource(R.string.phone))
            Spacer(modifier = Modifier.height(24.dp))
            CommonPasswordText(passwordState, stringResource(R.string.password))
            Spacer(modifier = Modifier.height(24.dp))
            RememberPassword()
            Spacer(modifier = Modifier.height(16.dp))
            LoginAndRegisterButtons(
                context,
                coroutineScope,
                phoneState.value,
                passwordState.value,
                navController)
        }
    }
}


@Composable
fun CodeLoginLayout(
    phoneState: MutableState<String>,
    navController: NavController) {
    val context = LocalContext.current
    val phone = remember { mutableStateOf("") }
    val code = remember { mutableStateOf("") }
    val generatedCode = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(80.dp))
            Text(text = "验证码登录", style = TextStyle(fontSize = 50.sp))
            Spacer(modifier = Modifier.height(50.dp))
            CommonPhoneText(phoneState, stringResource(R.string.phone))
            Spacer(modifier = Modifier.height(24.dp))
            CodeText(coroutineScope, code, generatedCode) // 传递generatedCode
            Spacer(modifier = Modifier.height(24.dp))
            CodeLoginButton(context, coroutineScope, phoneState.value, code.value, generatedCode, navController)
        }
    }
}


@Composable
fun CommonPhoneText(
    phoneState: MutableState<String>,
    label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label)
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
            value = phoneState.value,
            onValueChange = { newPhone ->
                if (newPhone.length <= 11) {
                    phoneState.value = newPhone
                }
            },
            placeholder = { Text("请输入手机号码") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
    }
}

@Composable
fun CommonPasswordText(
    passwordState: MutableState<String>,
    label: String) {
    val passwordVisibility = remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label)
        Spacer(Modifier.width(5.dp))
        TextField(
            value = passwordState.value,
            onValueChange = { newPassword ->
                passwordState.value = newPassword
            },
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            placeholder = { Text("请输入密码") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.width(150.dp).height(55.dp),
            trailingIcon = {
                val image = if (passwordVisibility.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisibility.value) "隐藏密码" else "显示密码"
                IconButton(onClick = { passwordVisibility.value =!passwordVisibility.value }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
        ForgetButton()
    }
}

@Composable
fun RememberPassword() {
    var rmPassword by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = rmPassword,
            onCheckedChange = { rmPassword = it }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.btn_remember),
            style = TextStyle(fontSize = 32.sp),
            color = Color.Black
        )
    }
}

@Composable
fun LoginAndRegisterButtons(
    context: Context,
    coroutineScope: CoroutineScope,
    phone: String,
    password: String,
    navController: NavController,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        LoginButton(
            context,
            coroutineScope,
            phone,
            password,
            navController)
        RegisterButton(context)
    }
}

@Composable
fun LoginButton(
    context: Context,
    coroutineScope: CoroutineScope,
    phone: String,
    password: String,
    navController: NavController
) {
    val dataStore = context.dataStore
    Button(
        onClick = {
            coroutineScope.launch {
                val phoneKey = stringPreferencesKey("phone")
                val passwordKey = stringPreferencesKey("password")
                val storedPhone = dataStore.data
                    .map { preferences -> preferences[phoneKey] }.first()
                val storedPassword = dataStore.data
                    .map { preferences -> preferences[passwordKey] }.first()
                if (storedPhone == phone && storedPassword == password) {
                    context.showToast("登录成功")
                    navController.navigate(ScreenPage.Reservation.route){
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
//                    val intent = Intent(context,IndexActivity::class.java)
//                    context.startActivity(intent)
                } else {
                    context.showToast("密码错误或账号未注册")
                }
            }
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = stringResource(R.string.btn_login), style = TextStyle(color = Color.Black, fontSize = 32.sp))
    }
}


@Composable
fun ForgetButton() {
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(context, ForgetActivity::class.java)
            context.startActivity(intent)

        },
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = stringResource(R.string.btn_forget), style = TextStyle(color = Color.Black))
    }
}

@Composable
fun RegisterButton(context: Context) {
    Button(
        onClick = {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray)
    ) {
        Text(text = stringResource(R.string.btn_register), style = TextStyle(color = Color.Black, fontSize = 32.sp))
    }
}

@Composable
fun CodeText(
    coroutineScope: CoroutineScope,
    codeState: MutableState<String>,
    generatedCode: MutableState<String>
) {
    val showCodeDialog = remember { mutableStateOf(false) }
    val isCoolingDown = remember { mutableStateOf(false) }
    val cooldownDuration = 60
    val cooldownTimer = remember { mutableIntStateOf(cooldownDuration) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.code))
        Spacer(modifier = Modifier.width(16.dp))
        TextField(
            value = codeState.value,
            onValueChange = { codeState.value = it },
            placeholder = { Text("请输入验证码", style = TextStyle(fontSize = 14.sp)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.width(130.dp).height(55.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        CodeButton(
            coroutineScope,
            showCodeDialog,
            generatedCode,
            isCoolingDown,
            cooldownTimer
        )
    }

    if (showCodeDialog.value) {
        AlertDialog(
            onDismissRequest = { showCodeDialog.value = false },
            title = { Text(text = "验证码") },
            text = { Text(text = "您的验证码是: ${generatedCode.value}") },
            confirmButton = {
                Button(
                    onClick = { showCodeDialog.value = false }
                ) {
                    Text(text = "确定")
                }
            }
        )
    }
}

@Composable
fun CodeButton(
    coroutineScope: CoroutineScope,
    showCodeDialog: MutableState<Boolean>,
    generatedCode: MutableState<String>,
    isCoolingDown: MutableState<Boolean>,
    cooldownTimer: MutableIntState
) {
    val context = LocalContext.current

    Button(
        onClick = {
            if (!isCoolingDown.value) {
                isCoolingDown.value = true
                val random = kotlin.random.Random
                generatedCode.value = (100000..999999).random(random).toString()
                showCodeDialog.value = true
                Toast.makeText(context, "验证码已发送", Toast.LENGTH_SHORT).show()
                coroutineScope.launch {
                    while (cooldownTimer.intValue > 0) {
                        delay(1000)
                        cooldownTimer.intValue--
                    }
                    isCoolingDown.value = false
                }
            } else {
                Toast.makeText(context, "请稍后再获取验证码", Toast.LENGTH_SHORT).show()
            }
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray),
        modifier = Modifier.width(120.dp).height(45.dp)
    ) {
        Text(
            text = if (isCoolingDown.value) {
                "${cooldownTimer.intValue}s 后重试"
            } else {
                "获取验证码"
            },
            style = TextStyle(fontSize = 13.sp),
            color = Color.Black
        )
    }
}


@Composable
fun CodeLoginButton(
    context: Context,
    coroutineScope: CoroutineScope,
    phone: String,
    code: String,
    generatedCode: MutableState<String>,
    navController: NavController
) {
    Button(
        onClick = {
            coroutineScope.launch {
                if (code == generatedCode.value) {
                    context.showToast("登录成功")
                    navController.navigate(ScreenPage.Reservation.route){
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
//                    val intent = Intent(context,IndexActivity::class.java)
//                    context.startActivity(intent)
                } else {
                    context.showToast("手机号或验证码错误")
                }
            }
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray),
        modifier = Modifier.fillMaxWidth().height(40.dp)
    ) {
        Text(text = stringResource(R.string.btn_login), color = Color.Black)
    }
}


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    CardRoom1Theme {
//        LoginApp()
    }
}