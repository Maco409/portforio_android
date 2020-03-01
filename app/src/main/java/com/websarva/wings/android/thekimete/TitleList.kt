package com.websarva.wings.android.thekimete

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

        val add: ImageButton = findViewById(R.id.add_button)
        add.setOnClickListener {
            val intent = Intent(this, ContentsList::class.java)

            var title_object = NCMBObject("Title")
            title_object.put("NewFlag", "true")
            title_object.save()
            var title_query = NCMBQuery<NCMBObject>("Title")
            title_query.whereEqualTo("NewFlag", "true")
            var new_title = title_query.find()
            intent.putExtra("KEY_TITLE", "新規作成")
            intent.putExtra("KEY_ID", new_title[0].getString("objectId"))
            new_title[0].put("NewFlag", "false")
            new_title[0].save()
            startActivityForResult(intent, 2)

        }

        val save: Button = findViewById(R.id.save_button)
        save.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            setResult(2, intent)
            finish()
        }
        createList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        createList()
    }

    fun createList() {
        val query = NCMBQuery<NCMBObject>("Title")

        var i = 0
        var titleList = query.find()
        var size = titleList.size

        val layoutManager = LinearLayoutManager(this)
        listView.layoutManager = layoutManager


        val arrayAdapter = mutableListOf<ListItem>()
        while (i < size) {
            var get_titlename = titleList[i].getString("Title")
            if (get_titlename != null) {
                arrayAdapter.add(ListItem(get_titlename))
            }
            i++
        }

        val adapter = MyArrayAdapter(arrayAdapter)
        val listview: RecyclerView = findViewById(R.id.listView)

        listview.adapter = adapter

        listView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        adapter.setOnItemClickListener(object : MyArrayAdapter.OnItemClickListener {

            override fun onClick(view: View, data: ListItem, position: Int) {

                val intent = Intent(view!!.context, ContentsList::class.java)

                var query = NCMBQuery<NCMBObject>("Title")
                query.whereEqualTo("Title", data.title)
                var titleID = query.find()
                intent.putExtra("KEY_TITLE", data.title)
                intent.putExtra("KEY_ID", titleID[0].getString("objectId"))

                startActivityForResult(intent, 1)

            }

        })
    }

}

data class ListItem(val title: String)

class RecyclerViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
    val titleView: TextView = view.item_title
    val deleteIcon: Button = view.delete_button
}

class MyArrayAdapter(var items: MutableList<ListItem>) :
    RecyclerView.Adapter<RecyclerViewHolder>() {

    lateinit var listener: OnItemClickListener

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
        holder.titleView.text = data.title
        if (data.title == ("+")) {
            holder.deleteIcon.visibility = View.GONE
        }
        holder.deleteIcon.setOnClickListener { _ ->
            val dlelete_title_query = NCMBQuery<NCMBObject>("Title")
            val dlete_massage_query = NCMBQuery<NCMBObject>("massage")
            dlelete_title_query.whereEqualTo("Title", data.title)
            var dlete_title = dlelete_title_query.find()
            dlete_massage_query.whereEqualTo("TitleID", dlete_title[0].objectId)
            var dlete_massage = dlete_massage_query.find()
            dlete_title[0].deleteObject()
            var i = 0
            var size = dlete_massage.size
            if (size != 0) {
                while (i < size) {
                    dlete_massage[i].deleteObject()
                    i++
                }
            }
            items.removeAt(position)
            this.notifyItemRemoved(position)
            this.notifyItemRangeChanged(position, items.size)
        }
        holder.titleView.setOnClickListener {
            listener.onClick(it, data, position)
        }
    }

    interface OnItemClickListener {
        fun onClick(view: View, data: ListItem, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}






