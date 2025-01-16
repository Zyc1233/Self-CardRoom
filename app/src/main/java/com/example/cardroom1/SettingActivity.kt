package com.example.cardroom1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cardroom1.ui.theme.CardRoom1Theme

class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardRoom1Theme {
                SettingApp()
            }
        }
    }
}


@Composable
fun SettingApp(){
    SettingLayout()
}

@Composable
fun SettingLayout(){
    Column(modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
      LightDropdownMenu()
    }
}


@Composable
fun LightDropdownMenu(){
    var selectedOption by remember { mutableStateOf("Option 1") }
    var isExposedDropdownOpen by remember { mutableStateOf(false) }
    DropdownMenu(
        expanded = isExposedDropdownOpen,
        onDismissRequest = {isExposedDropdownOpen = false}
    ) {
        DropdownMenuItem(onClick = {
            selectedOption = "Option 1"
            isExposedDropdownOpen = false
        }, text = {
            Text("温馨")
        })
        DropdownMenuItem(onClick = {
            selectedOption = "Option 2"
            isExposedDropdownOpen = false
        }, text = {
            Text("恐怖")
        })
        DropdownMenuItem(onClick = {
            selectedOption = "Option 3"
            isExposedDropdownOpen = false
        }, text = {
            Text("浪漫")
        })
        DropdownMenuItem(onClick = {
            selectedOption = "Option 3"
            isExposedDropdownOpen = false
        }, text = {
            Text("休闲")
        })
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CardRoom1Theme {
        SettingLayout()
    }
}