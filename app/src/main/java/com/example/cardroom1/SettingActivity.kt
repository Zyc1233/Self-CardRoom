package com.example.cardroom1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardroom1.ui.theme.CardRoom1Theme


class SettingActivity : ComponentActivity() {
    private val viewModel: SettingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardRoom1Theme {
                SettingApp(viewModel)
            }
        }
    }
}


@Composable
fun SettingApp(viewModel: SettingViewModel) {
    SettingLayout(viewModel)
}


@Composable
fun SettingLayout(viewModel: SettingViewModel) {
    val appStyle by viewModel.appStyle.observeAsState(AppStyle("默认", "默认", "默认"))

    var themeColor by remember { mutableStateOf(appStyle.themeColor) }
    var fontFamily by remember { mutableStateOf(appStyle.fontFamily) }
    var fontSize by remember { mutableStateOf(appStyle.fontSize) }

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("取消")
                }
                Button(onClick = { showDialog = false }) {
                    Text("确定")
                }
            },
            title = { Text("确认更改") },
            text = { Text("设置已保存，请重启程序") }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ColorDropMenu(selectedOption = themeColor, onOptionSelected = { themeColor = it })
        Spacer(Modifier.height(8.dp))
        FontDropMenu(selectedOption = fontFamily, onOptionSelected = { fontFamily = it })
        Spacer(Modifier.height(8.dp))
        FontSizeDropMenu(selectedOption = fontSize, onOptionSelected = { fontSize = it })
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.updateAppStyle(themeColor, fontFamily, fontSize)
                viewModel.updateThemeColorScheme(themeColor)
                showDialog = true
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF2A97EE)),
            modifier = Modifier.fillMaxWidth().height(55.dp)
        ) {
            Text(text = "保存", fontSize = 25.sp)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorDropMenu(selectedOption: String, onOptionSelected: (String) -> Unit) {
    val options = listOf("深色", "浅色", "默认", "暖色", "冷色")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded =!expanded }
    ) {
        TextField(
            value = selectedOption,
            onValueChange = { },
            label = { Text("主题颜色", fontSize = 20.sp) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontDropMenu(selectedOption: String, onOptionSelected: (String) -> Unit) {
    val options = listOf("粗体", "斜体", "默认", "渐变")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded =!expanded }
    ) {
        TextField(
            value = selectedOption,
            onValueChange = { },
            label = { Text("字体样式", fontSize = 20.sp) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontSizeDropMenu(selectedOption: String, onOptionSelected: (String) -> Unit) {
    val options = listOf("超大", "大", "默认", "小号", "超小")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded =!expanded }
    ) {
        TextField(
            value = selectedOption,
            onValueChange = { },
            label = { Text("字体大小", fontSize = 20.sp) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

