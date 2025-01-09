package com.example.cardroom1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.cardroom1.ui.theme.CardRoom1Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardRoom1Theme {
                RegisterApp()
            }
        }
    }
}

@Composable
fun RegisterLayout() {
    val context = LocalContext.current
    val phone = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "用户注册", style = TextStyle(fontSize = 50.sp))
            Spacer(Modifier.height(16.dp))
            CommonPhoneText(phone, stringResource(R.string.phone))
            Spacer(Modifier.height(16.dp))
            NewPasswordText(password, stringResource(R.string.first_password), context)
            Spacer(Modifier.height(16.dp))
            NewPasswordText(confirmPassword, stringResource(R.string.second_password),context)
            Spacer(Modifier.height(16.dp))
            NewRegisterButton(context, coroutineScope, phone.value, password.value, confirmPassword.value)
        }
    }
}

@Composable
fun NewRegisterButton(
    context: Context,
    coroutineScope: CoroutineScope,
    phone: String,
    password: String,
    confirmPassword: String
) {
    val dataStore = context.dataStore
    Button(
        onClick = {
            coroutineScope.launch {
                if (password == confirmPassword) {
                    val phoneKey = stringPreferencesKey("phone")
                    val passwordKey = stringPreferencesKey("password")
                    dataStore.edit { preferences ->
                        preferences[phoneKey] = phone
                        preferences[passwordKey] = password
                    }
                    context.showToast("注册成功")
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                } else {
                    context.showToast("两次密码不一致")
                }
            }
        },
        colors = ButtonDefaults.buttonColors(Color.LightGray),
        modifier = Modifier.fillMaxWidth().height(40.dp)
    ) {
        Text(text = stringResource(R.string.btn_register), color = Color.Black)
    }
}

@Composable
fun NewPasswordText(passwordState: MutableState<String>, label: String, context: Context) {
    val minLength = 6
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label)
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
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            placeholder = { Text("请输入密码") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            trailingIcon = {
                val image = if (passwordVisibility.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisibility.value) "隐藏密码" else "显示密码"
                IconButton(onClick = { passwordVisibility.value =!passwordVisibility.value }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
    }
}


@Composable
fun RegisterApp() {
    RegisterLayout()
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    CardRoom1Theme {
        RegisterLayout()
    }
}
