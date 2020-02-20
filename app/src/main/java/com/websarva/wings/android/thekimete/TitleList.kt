package com.websarva.wings.android.thekimete

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity



class TitleList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_list)

        // 初期のリスト項目を設定
        val arrayAdapter = MyArrayAdapter(this, 0).apply {
            add(ListItem("タイトル"))
            add(ListItem("・・・"))
            add(ListItem("・・・"))
            add(ListItem("・・・"))
            add(ListItem("・・・"))
        }

        // ListView にリスト項目と ArrayAdapter を設定
        val listView : ListView = findViewById(R.id.listView)
        listView.adapter = arrayAdapter

    }

    // リスト項目のデータ
    class ListItem(val title : String) {

        var description : String = "No description"

        constructor(title: String, description: String) : this(title) {
            this.description = description
        }
    }

    // リスト項目を再利用するためのホルダー
    data class ViewHolder(val titleView: TextView, val goedit: Button, val deleteIcon: Button)

    // 自作のリスト項目データを扱えるようにした ArrayAdapter
    class MyArrayAdapter : ArrayAdapter<ListItem> {

        private var inflater : LayoutInflater? = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

        constructor(context : Context, resource : Int) : super(context, resource) {}

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {


            var viewHolder : ViewHolder? = null
            var view = convertView
            var mHandler = Handler(Looper.getMainLooper())


            // 再利用の設定

            if (view == null) {

                view = inflater!!.inflate(R.layout.list_item, parent, false)

                viewHolder = ViewHolder(
                    view.findViewById(R.id.item_title),
                    view.findViewById(R.id.goed),
                    view.findViewById(R.id.delete_button)
                )
                view.tag = viewHolder
            } else {
                viewHolder = view.tag as ViewHolder
            }

            // 項目の情報を設定
            val listItem = getItem(position)
            viewHolder.titleView.text = listItem!!.title

            viewHolder.goedit.setOnClickListener {
                mHandler.post(Runnable {
                val intent = Intent(this, ContentsList::class.java)
                startActivity(intent)
                })
            }

            viewHolder.deleteIcon.setOnClickListener { _ ->
                // 削除ボタンをタップしたときの処理
                this.remove(listItem)
                this.notifyDataSetChanged()
            }




            return view!!
        }
    }
}
