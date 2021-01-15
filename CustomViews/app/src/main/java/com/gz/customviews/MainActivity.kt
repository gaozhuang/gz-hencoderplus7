@file:Suppress("NO_REFLECTION_IN_CLASS_PATH")

package com.gz.customviews

import android.content.res.Resources
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {
    private val titleList = arrayOf("DashboardView", "PieView", "RoundImageView", "SportView")
    private val contentList = arrayOf("2021-01-13", "2021-01-13", "2021-01-14", "2021-01-15")
    private val classList =
        arrayOf(DashboardView::class, PieView::class, RoundImageView::class, SportView::class)

    private lateinit var rootView: View

    private var windowSize: Int = 300.dp.toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rootView = findViewById(R.id.root_view)

        val listView = findViewById<RecyclerView>(R.id.rv_main_list).also {
            it.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
            it.adapter = MyAdapter()
        }
        listView.adapter = MyAdapter()

        windowSize = min(
            Resources.getSystem().displayMetrics.widthPixels,
            Resources.getSystem().displayMetrics.heightPixels
        )
    }

    inner class MyAdapter : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(layoutInflater.inflate(R.layout.item_list_main, parent, false))
        }

        override fun getItemCount(): Int {
            return titleList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.titleView.text = titleList[position]
            holder.contentView.text = contentList[position]
            holder.itemView.setOnClickListener(View.OnClickListener { popView(classList[position]) })
        }

        private fun popView(viewClass: KClass<out View>) {
            val popView: View = viewClass.constructors.first().call(this@MainActivity, null)
            val popWindow = PopupWindow(popView, windowSize, windowSize, false)
            popWindow.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.pop_bg
                )
            )
            popWindow.isOutsideTouchable = true
            popWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleView: TextView =
            itemView.findViewById<TextView>(R.id.tv_item_list_main_title)

        var contentView: TextView = itemView.findViewById<TextView>(R.id.tv_item_list_main_content)
    }
}
