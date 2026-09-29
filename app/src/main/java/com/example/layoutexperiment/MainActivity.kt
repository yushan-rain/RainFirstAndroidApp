package com.example.layoutexperiment

import android.os.Bundle
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 加载你的约束布局界面（第6题）
        setContentView(R.layout.activity_constrain)

        // 💡 如果你想切回计算器界面，把上面那行代码注释掉，换成：
        // setContentView(R.layout.activity_main)

        // 💡 如果你想看第1题（线性布局），换成：
        // setContentView(R.layout.activity_linear)

        // 💡 如果你想看第2题（表格布局），换成：
        // setContentView(R.layout.activity_table)
    }
}