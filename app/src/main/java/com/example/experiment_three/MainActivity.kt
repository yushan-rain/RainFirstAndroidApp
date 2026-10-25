package com.example.experiment_three // 你的包名

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "my_channel_id"
    private val REQUEST_CODE_NOTIFICATION = 100

    // 提升为全局变量，方便在菜单中操作
    private lateinit var dataList: ArrayList<Map<String, Any>>
    private lateinit var adapter: SimpleAdapter
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        createNotificationChannel()
        requestNotificationPermission()

        // 1. 准备数据源
        dataList = ArrayList()
        val names = arrayOf("Lion", "Tiger", "Monkey", "Dog", "Cat", "Elephant")
        val icons = intArrayOf(
            R.drawable.lion, R.drawable.tiger, R.drawable.monkey,
            R.drawable.dog, R.drawable.cat, R.drawable.elephant
        )

        for (i in names.indices) {
            val map = HashMap<String, Any>()
            map["name"] = names[i]
            map["icon"] = icons[i]
            dataList.add(map)
        }

        // 2. 配置 SimpleAdapter
        adapter = SimpleAdapter(
            this,
            dataList,
            R.layout.list_item,
            arrayOf("name", "icon"),
            intArrayOf(R.id.tv_name, R.id.iv_icon)
        )
        listView = findViewById(R.id.list_view)
        listView.adapter = adapter

        // ================= 新增：点击按钮跳转到实验五 =================
        findViewById<android.widget.Button>(R.id.btn_go_recycler).setOnClickListener {
            startActivity(android.content.Intent(this, RecyclerActivity::class.java))
        }
        // 4. 点击事件 (发通知)
        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as Map<*, *>
            val selectedName = selectedItem["name"].toString()
            Toast.makeText(this@MainActivity, "选中了: $selectedName", Toast.LENGTH_SHORT).show()
            sendNotification(selectedName)
        }
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL

        listView.setMultiChoiceModeListener(object : AbsListView.MultiChoiceModeListener {
            // 进入长按模式时调用
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                // 加载我们刚才写的垃圾桶菜单
                mode?.menuInflater?.inflate(R.menu.context_menu, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            // 点击上下文菜单中的按钮时调用
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                if (item?.itemId == R.id.menu_delete) {
                    // 获取所有被选中的位置
                    val checkedPositions = SparseBooleanArray()
                    for (i in 0 until listView.count) {
                        if (listView.isItemChecked(i)) {
                            checkedPositions.put(i, true)
                        }
                    }

                    // 倒序删除数据，避免索引越界
                    for (i in checkedPositions.size() - 1 downTo 0) {
                        val position = checkedPositions.keyAt(i)
                        dataList.removeAt(position)
                    }

                    // 刷新列表
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this@MainActivity, "删除了选中的项目", Toast.LENGTH_SHORT).show()

                    mode?.finish() // 退出 ActionMode
                    return true
                }
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                // 退出模式时的操作（可留空）
            }

            // 当选中状态改变时调用（更新顶部的 "X selected" 文字）
            override fun onItemCheckedStateChanged(
                mode: ActionMode?,
                position: Int,
                id: Long,
                checked: Boolean
            ) {
                val checkedCount = listView.checkedItemCount
                mode?.title = "$checkedCount selected"
            }
        })
    }

    // ================= 实验三：菜单相关 =================
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val tvTest = findViewById<TextView>(R.id.tv_test)
        when (item.itemId) {
            R.id.font_small -> tvTest.textSize = 10f
            R.id.font_medium -> tvTest.textSize = 16f
            R.id.font_large -> tvTest.textSize = 20f
            R.id.color_red -> tvTest.setTextColor(Color.RED)
            R.id.color_black -> tvTest.setTextColor(Color.BLACK)
            R.id.menu_normal -> Toast.makeText(this, "点击了普通菜单项", Toast.LENGTH_SHORT).show()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    // ================= 实验二：弹窗相关 =================
    private fun showCustomDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_signin, null)
        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Sign in") { _, _ ->
                val username = dialogView.findViewById<EditText>(R.id.et_username).text.toString()
                val password = dialogView.findViewById<EditText>(R.id.et_password).text.toString()
                Toast.makeText(this, "登录: $username, 密码: $password", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    // ================= 实验一：通知相关 =================
    private fun sendNotification(title: String) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText("你刚刚在列表中点击了 $title")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "列表通知"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_NOTIFICATION)
            }
        }
    }
}