package com.websarva.wings.android.thekimete

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.text.method.TextKeyListener.clear
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBQuery
import kotlinx.android.synthetic.main.activity_title_list.*
import java.util.Collections.addAll


class ContentsList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contents_list)

        val query = NCMBQuery<NCMBObject>("massage")
        var i = 0
        val tID = intent.getStringExtra("KEY_ID")
        query.whereEqualTo("TitleID",tID)
        var conLis = query.find()
        var n = conLis.size

        // 初期のリスト項目を設定
        val arrayAdapter = MyArrayAdapter(this, 0).apply {
            while (i < n) {
            var o = conLis[i].getString("content")
            add(ListItem(o))
            i++
        }

        }

        var titlev = findViewById<TextView>(R.id.titlev)
        titlev.text = intent.getStringExtra("KEY_TITLE")

        val save : Button = findViewById(R.id.save)
        save.setOnClickListener {
            val intent = Intent(this,TitleList::class.java)
            startActivity(intent)
        }

        // ListView にリスト項目と ArrayAdapter を設定
        val listView: ListView = findViewById(R.id.clistView)
        listView.adapter = arrayAdapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, pos, id ->
            var listitem : ListItem = parent.getItemAtPosition(pos) as ListItem
            var query = NCMBQuery<NCMBObject>("massage")
            query.whereEqualTo("content",listitem.koumoku)
            var cID = query.find()
            //EditTextFragment.newInstance(cID[0].getString("objectId"), listitem.koumoku).show(this.supportFragmentManager, EditTextFragment.TAG)
            val bundle = Bundle()
            bundle.putString("KEY_ID",cID[0].getString("objectId"))
            bundle.putString("KEY_KOUMOKU",listitem.koumoku)
            val EdTF = EditTextFragment()
            EdTF.arguments = bundle
            EdTF.show(supportFragmentManager,"ResultFragment")
        }
    }

    override fun onResume() {
        super.onResume()
        print("aaa")

    }




    // リスト項目のデータ
    class ListItem(var koumoku: String) {

        var description: String = "No description"

        constructor(koumoku: String, description: String) : this(koumoku) {
            this.description = description
        }
    }

    // リスト項目を再利用するためのホルダー
    data class ViewHolder(var titleView: TextView, val deleteIcon: Button)

    // 自作のリスト項目データを扱えるようにした ArrayAdapter
    class MyArrayAdapter : ArrayAdapter<ListItem> {

        private var inflater: LayoutInflater? =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

        constructor(context: Context, resource: Int) : super(context, resource) {}

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {


            var viewHolder: ViewHolder? = null
            var view = convertView
            var mHandler = Handler(Looper.getMainLooper())

            // 再利用の設定
            if (view == null) {

                view = inflater!!.inflate(R.layout.contents_item, parent, false)

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
            viewHolder.titleView.text = listItem!!.koumoku
            //listItem.koumoku.addTextChangedListener(object : TextWatcher {
//                override fun afterTextChanged(p0: Editable?) {
//                    //ここに処理を書く
//                    val text = listItem.koumoku.text.toString()
//                    var query = NCMBQuery<NCMBObject>("massage")
//                    query.whereEqualTo("objectId",cID)
//                    var updatte = query.find()
//                    updatte[0].put("content",text)
//                    updatte[0].save()
//
//
//                }
//
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//                }
//            })
            viewHolder.deleteIcon.setOnClickListener { _ ->
                // 削除ボタンをタップしたときの処理
                this.remove(listItem)
                this.notifyDataSetChanged()
            }

            return view!!
        }
    }



}