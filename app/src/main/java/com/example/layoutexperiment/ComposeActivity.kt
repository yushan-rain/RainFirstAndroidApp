package com.example.layoutexperiment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.UUID

// 1. 定义任务数据类
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val isDone: Boolean = false
)

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CourseTaskScreen()
            }
        }
    }
}

@Composable
fun CourseTaskScreen() {
    // 2. 状态管理
    // 初始状态：3项任务，完成1项
    val tasks = remember {
        mutableStateListOf(
            Task(name = "学习 Column 和 Row", isDone = true),
            Task(name = "学习状态管理", isDone = false),
            Task(name = "完成 Compose 实验", isDone = false)
        )
    }
    var inputText by remember { mutableStateOf("") }

    // 计算已完成数量
    val doneCount = tasks.count { it.isDone }

    // 3. 页面主布局
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 标题
        Text(
            text = "课程学习任务",
            color = Color(0xFFD32F2F), // 红色
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 输入框 + 添加按钮
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("请输入学习任务") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    // 添加任务逻辑
                    if (inputText.isNotBlank()) {
                        tasks.add(Task(name = inputText.trim()))
                        inputText = "" // 清空输入框
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F) // 红色按钮
                )
            ) {
                Text("添加", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 已完成计数
        Text(
            text = "已完成：$doneCount / ${tasks.size}",
            color = Color.Gray,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 任务列表 (LazyColumn)
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(
                items = tasks,
                key = { _, task -> task.id }
            ) { index, task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 复选框
                    Checkbox(
                        checked = task.isDone,
                        onCheckedChange = { isChecked ->
                            // 更新完成状态
                            tasks[index] = task.copy(isDone = isChecked)
                        }
                    )
                    // 任务名称 (带删除线逻辑)
                    Text(
                        text = task.name,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        textDecoration = if (task.isDone) {
                            TextDecoration.LineThrough // 勾选后加删除线
                        } else {
                            TextDecoration.None
                        },
                        color = if (task.isDone) Color.Gray else Color.Black
                    )
                    // 删除按钮
                    TextButton(
                        onClick = { tasks.removeAt(index) }
                    ) {
                        Text(
                            text = "删除",
                            color = Color(0xFFD32F2F)
                        )
                    }
                }
            }
        }
    }
}