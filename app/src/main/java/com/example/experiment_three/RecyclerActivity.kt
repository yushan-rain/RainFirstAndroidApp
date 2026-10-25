package com.example.experiment_three

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 加载刚才写的 XML 布局
        setContentView(R.layout.activity_recycler)

        // 通过 ID 找到 RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view)

        val animalList = mutableListOf(
            Animal("Lion", R.drawable.lion),
            Animal("Tiger", R.drawable.tiger),
            Animal("Monkey", R.drawable.monkey),
            Animal("Dog", R.drawable.dog),
            Animal("Cat", R.drawable.cat),
            Animal("Elephant", R.drawable.elephant)
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AnimalAdapter(animalList)
    }
}