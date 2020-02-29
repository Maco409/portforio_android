package com.websarva.wings.android.thekimete

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBQuery
import kotlinx.android.synthetic.main.activity_title_list.*
import kotlinx.android.synthetic.main.list_item.view.*


class TitleList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_list)


        createList()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode,resultCode,data)
        createList()
    }

    fun createList(){
        val query = NCMBQuery<NCMBObject>("Title")

        var i = 0
        var titleList = query.find()
        var n = titleList.size

        // LayoutManagerの設定
        val layoutManager = LinearLayoutManager(this)
        listView.layoutManager = layoutManager

//        // Adapterの設定
//        val sampleList = mutableListOf<String>()
//        for (i in 0..10) {
//            sampleList.add(i.toString())
//        }
        // 初期のリスト項目を設定
        val arrayAdapter = mutableListOf<ListItem>()
            while (i < n) {
                var o = titleList[i].getString("Title")
                arrayAdapter.add(ListItem(o))
                i++
            }
            arrayAdapter.add(ListItem("+"))

        val adapter = MyArrayAdapter(arrayAdapter)
        listView.adapter = adapter


        // 区切り線の表示
        listView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))


        // ListView にリスト項目と ArrayAdapter を設定
      val listview: RecyclerView = findViewById(R.id.listView)


        //listView.adapter = arrayAdapter
        var itemcount = adapter.itemCount




        adapter.setOnItemClickListener(object : MyArrayAdapter.OnItemClickListener {

            override fun onClick(view: View, data: ListItem) {

            val intent = Intent(view!!.context, ContentsList::class.java)

//posにタッチされた項目のポジションを入れたい
            when(pos) {
                itemcount - 1 -> {
                    var obj = NCMBObject("Title")
                    obj.put("NewFlag","true")
                    obj.save()
                    var query = NCMBQuery<NCMBObject>("Title")
                    query.whereEqualTo("NewFlag","true")
                    var new = query.find()
                    intent.putExtra("KEY_TITLE","新規作成")
                    intent.putExtra("KEY_ID",new[0].getString("objectId"))
                    new[0].put("NewFlag","false")
                    new[0].save()

                }
                else -> {


                    var query = NCMBQuery<NCMBObject>("Title")
                    query.whereEqualTo("Title", data.title)
                    var tID = query.find()
                    intent.putExtra("KEY_TITLE", data.title)
                    intent.putExtra("KEY_ID", tID[0].getString("objectId"))
                }
            }

            startActivityForResult(intent,1)

            }

        })
    }

}

    // リスト項目のデータ
    data class ListItem(val title: String) {

        var description : String = "No description"

        constructor(title: String, description: String) : this(title) {
            this.description = description
        }
    }

    // リスト項目を再利用するためのホルダー
    class RecyclerViewHolder(var view: View): RecyclerView.ViewHolder(view){
        val titleView: TextView = view.item_title
        val deleteIcon: Button = view.delete_button
    }

    // 自作のリスト項目データを扱えるようにした ArrayAdapter
    class MyArrayAdapter(var items: MutableList<ListItem>) : RecyclerView.Adapter<RecyclerViewHolder>() {

        lateinit var listener : OnItemClickListener

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
            setOnItemClickListener(listener)
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.list_item, parent, false)

            return RecyclerViewHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

            var data = items[position]
            holder.titleView.text = items.get(position).toString()
            holder.deleteIcon.setOnClickListener { _ ->
                              // 削除ボタンをタップしたときの処理
                this.notifyItemRemoved(position)
            }
            holder.titleView.setOnClickListener {
                listener.onClick(it,data)
            }
        }

        interface OnItemClickListener{
            fun onClick(view: View,data: ListItem)
        }
        fun setOnItemClickListener(listener: OnItemClickListener) {
            this.listener = listener
        }
    }

//        private var inflater : LayoutInflater? = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
//
//        constructor(context : Context, resource : Int) : super(context, resource) {}
//
//        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//
//
//            var viewHolder : ViewHolder? = null
//            var view = convertView
//
//
//            // 再利用の設定
//
//            if (view == null) {
//
//                view = inflater!!.inflate(R.layout.list_item, parent, false)
//                viewHolder = ViewHolder(
//                    view.findViewById(R.id.item_title),
//                    view.findViewById(R.id.delete_button)
//                )
//                view.tag = viewHolder
//            } else {
//                viewHolder = view.tag as ViewHolder
//            }
//
//            // 項目の情報を設定
//            val listItem = getItem(position)
//            viewHolder.titleView.text = listItem!!.title.toString()
//            viewHolder.deleteIcon.setOnClickListener { _ ->
//                // 削除ボタンをタップしたときの処理
//                this.remove(listItem)
//                this.notifyDataSetChanged()
//            }
//            return view!!
//        }




