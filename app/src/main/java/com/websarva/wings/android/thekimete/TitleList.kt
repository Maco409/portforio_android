package com.websarva.wings.android.thekimete

import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.nifcloud.mbaas.core.NCMB
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBObjectService
import com.nifcloud.mbaas.core.NCMBQuery
import kotlinx.android.synthetic.main.activity_title_list.*


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

        // 初期のリスト項目を設定
        val arrayAdapter = MyArrayAdapter(this, 0).apply {
            while (i < n) {
                var o = titleList[i].getString("Title")
                add(ListItem(o))
                i++
            }
            add(ListItem("+"))
        }

        // ListView にリスト項目と ArrayAdapter を設定
        val listView: ListView = findViewById(R.id.listView)

        listView.adapter = arrayAdapter
        var itemcount = listView.count


        listView.onItemClickListener =  AdapterView.OnItemClickListener { parent, view, pos, id ->
            val intent = Intent(view!!.context, ContentsList::class.java)

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
                    var listitem: ListItem = parent.getItemAtPosition(pos) as ListItem
                    var query = NCMBQuery<NCMBObject>("Title")
                    query.whereEqualTo("Title", listitem.title)
                    var tID = query.find()
                    intent.putExtra("KEY_TITLE", listitem.title)
                    intent.putExtra("KEY_ID", tID[0].getString("objectId"))
                }
            }
            startActivityForResult(intent,1)
        }


    }

}

    // リスト項目のデータ
    class ListItem(val title: String) {

        var description : String = "No description"

        constructor(title: String, description: String) : this(title) {
            this.description = description
        }
    }

    // リスト項目を再利用するためのホルダー
    data class ViewHolder(val titleView: TextView,val deleteIcon: Button)

    // 自作のリスト項目データを扱えるようにした ArrayAdapter
    class MyArrayAdapter : ArrayAdapter<ListItem> {

        private var inflater : LayoutInflater? = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

        constructor(context : Context, resource : Int) : super(context, resource) {}

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {


            var viewHolder : ViewHolder? = null
            var view = convertView


            // 再利用の設定

            if (view == null) {

                view = inflater!!.inflate(R.layout.list_item, parent, false)
                viewHolder = ViewHolder(
                    view.findViewById(R.id.item_title),
                    view.findViewById(R.id.delete_button)
                )
                view.tag = viewHolder
            } else {
                viewHolder = view.tag as ViewHolder
            }

            // 項目の情報を設定
            val listItem = getItem(position)
            viewHolder.titleView.text = listItem!!.title.toString()
            viewHolder.deleteIcon.setOnClickListener { _ ->
                // 削除ボタンをタップしたときの処理
                this.remove(listItem)
                this.notifyDataSetChanged()
            }
            return view!!
        }

    }


