package com.example.experiment_three

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// 传入数据列表
class AnimalAdapter(private val animalList: MutableList<Animal>) : RecyclerView.Adapter<AnimalAdapter.ViewHolder>() {

    // 1. 内部类 ViewHolder
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_rec_name)
        val ivIcon: ImageView = view.findViewById(R.id.iv_rec_icon)
    }

    // 2. 创建 ViewHolder (加载布局)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false)
        return ViewHolder(view)
    }

    // 3. 绑定数据
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animal = animalList[position]
        holder.tvName.text = animal.name
        holder.ivIcon.setImageResource(animal.imageResId)

        // 实验要求：单击显示名称，增加一条记录并立即更新
        holder.tvName.setOnClickListener {
            // 获取当前点击的位置
            val currentPos = holder.adapterPosition
            if (currentPos != RecyclerView.NO_POSITION) {
                // 在当前位置的下方插入一条新数据
                animalList.add(currentPos + 1, Animal("新动物", R.drawable.lion))
                // 通知适配器：我插入了一条数据，请立即刷新界面
                notifyItemInserted(currentPos + 1)
            }
        }
    }

    // 4. 返回列表项总数
    override fun getItemCount(): Int {
        return animalList.size
    }
}