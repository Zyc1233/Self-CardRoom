package com.example.cardroom1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardroom1.ui.theme.CardRoom1Theme

class AboutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardRoom1Theme {
              AboutApp()
            }
        }
    }
}
@Composable
fun AboutLayout() {
    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = """
                1. 预约房间
                - 点击“房间预约”进入预约界面。
                - 选择房间类型、日期、开始时间和结束时间。
                - 输入预约人姓名并点击“预约”按钮。
                - 预约成功后，系统会跳转到预约详情页面。
            """.trimIndent(),
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = """
                2. 查看预约记录
                - 点击“查看预约情况”进入预约列表。
                - 预约列表会显示所有预约记录。
            """.trimIndent(),
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = """
                3. 删除预约
                - 在预约列表中找到需要删除的预约记录。
                - 点击“取消预约”按钮并确认删除。
            """.trimIndent(),
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = """
                4. 修改预约
                - 在预约列表中找到需要修改的预约记录。
                - 点击“修改预约”按钮并修改相关信息。
                - 点击“修改”按钮完成修改。
            """.trimIndent(),
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = """
                5. 前往房间
                - 在预约列表中找到需要前往的房间。
                - 点击“前往房间”按钮进入房间详情页面。
                - 页面会显示房间信息和倒计时。
                - 可以通过页面上的按钮控制设备。
            """.trimIndent(),
            fontSize = 18.sp
        )
    }
}


@Composable
fun AboutApp() {
    AboutLayout()
}

@Preview(showBackground = true)
@Composable
fun AboutPreview() {
    CardRoom1Theme {
        AboutLayout()
    }
}