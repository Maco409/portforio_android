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
import kotlinx.android.synthetic.main.activity_contents_list.*
import kotlinx.android.synthetic.main.activity_title_list.*
import java.util.Collections.addAll


class ContentsList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contents_list)

        var titlev = findViewById<EditText>(R.id.titlev)
        titlev.setText(intent.getStringExtra("KEY_TITLE"),TextView.BufferType.EDITABLE)

        updateList()

    }

    fun clickOkDialog() {
        updateList()
    }

    fun updateList() {
        val mquery = NCMBQuery<NCMBObject>("massage")
        var tquery = NCMBQuery<NCMBObject>("Title")
        var i = 0
        val tID = intent.getStringExtra("KEY_ID")
        mquery.whereEqualTo("TitleID", tID)
        tquery.whereEqualTo("objectId",tID)
        var settitle=  tquery.find()
        var conLis = mquery.find()
        var n = conLis.size



        val save: Button = findViewById(R.id.save)
        save.setOnClickListener {
            val intent = Intent(this,TitleList::class.java)

            var newtitle = titlev.text.toString()
            settitle[0].put("Title",newtitle)
            settitle[0].save()
            setResult(1,intent)
            finish()

        }

        // 初期のリスト項目を設定
        val arrayAdapter = MyArrayAdapter(this, 0).apply {
            while (i < n) {
                var o = conLis[i].getString("content")
                add(ListItem(o))
                i++
            }
            add(ListItem("+"))
        }
        val listView: ListView = findViewById(R.id.clistView)
        listView.adapter = arrayAdapter
        var itemcount = listView.adapter.count


        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, pos, id ->
            val bundle = Bundle()
            when(pos) {
                itemcount -1 -> {
                    bundle.putString("KEY_TID",tID)
                }
                else -> {
                    var listitem : ListItem = parent.getItemAtPosition(pos) as ListItem
                    var query = NCMBQuery<NCMBObject>("massage")
                    query.whereEqualTo("content", listitem.koumoku)
                    var cID = query.find()
                    bundle.putString("KEY_ID", cID[0].getString("objectId"))
                    bundle.putString("KEY_KOUMOKU", listitem.koumoku)
                }
            }
            val EdTF = EditTextFragment()
            EdTF.arguments = bundle
            EdTF.show(supportFragmentManager, "EditTextFragment")
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
            if(listItem!!.koumoku == ("+")){
                viewHolder.deleteIcon.visibility = View.GONE
            }
            viewHolder.titleView.text = listItem!!.koumoku
            viewHolder.deleteIcon.setOnClickListener { _ ->
                // 削除ボタンをタップしたときの処理
                this.remove(listItem)
                this.notifyDataSetChanged()
            }

            return view!!
        }
    }



}