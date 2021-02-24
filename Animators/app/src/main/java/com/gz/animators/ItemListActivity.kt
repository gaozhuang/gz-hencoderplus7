package com.gz.animators

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView

class ItemListActivity : AppCompatActivity() {
    private val itemList = arrayListOf<AnimatableItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        setupRecyclerView(findViewById(R.id.item_list))
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        initItems()
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(itemList)
    }

    private fun initItems() {
        itemList.add(
            AnimatableItem(
                "圆的缩放动画（View自带animate动画）",
                "2021-01-20",
                Intent(this, CircleAnimatorActivity::class.java)
            )
        )
        itemList.add(
            AnimatableItem(
                "折叠头像动画（AnimatorSet顺序动画）",
                "2021-01-20",
                Intent(this, CameraAnimatorActivity::class.java)
            )
        )
        itemList.add(
            AnimatableItem(
                "点的移动动画（自定义TypeEvaluator）",
                "2021-01-20",
                Intent(this, PointAnimatorActivity::class.java)
            )
        )
        itemList.add(
            AnimatableItem(
                "文字动画（动画本质）",
                "2021-01-21",
                Intent(this, TextAnimatorActivity::class.java)
            )
        )
    }

    class SimpleItemRecyclerViewAdapter(private val values: List<AnimatableItem>) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.title
            holder.contentView.text = item.content

            with(holder.itemView) {
                tag = item
                setOnClickListener(View.OnClickListener { v ->
                    v.context.startActivity(item.intent)
                })
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.findViewById(R.id.id_text)
            val contentView: TextView = view.findViewById(R.id.content)
        }
    }
}