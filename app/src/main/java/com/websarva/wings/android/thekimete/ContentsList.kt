package com.websarva.wings.android.thekimete

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBQuery
import kotlinx.android.synthetic.main.activity_contents_list.*


class ContentsList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contents_list)

        var title_edit = findViewById<EditText>(R.id.title_edit)
        title_edit.setText(intent.getStringExtra("KEY_TITLE"), TextView.BufferType.EDITABLE)

        updateList()

    }

    fun clickOkDialog() {
        updateList()
    }

    fun updateList() {
        val massege_query = NCMBQuery<NCMBObject>("massage")
        var title_query = NCMBQuery<NCMBObject>("Title")
        var i = 0
        val titleID = intent.getStringExtra("KEY_ID")
        massege_query.whereEqualTo("TitleID", titleID)
        title_query.whereEqualTo("objectId", titleID)
        var settitle = title_query.find()
        var contents_list = massege_query.find()
        var size = contents_list.size


        val save: Button = findViewById(R.id.save)
        save.setOnClickListener {
            val intent = Intent(this, TitleList::class.java)

            var newtitle = title_edit.text.toString()
            settitle[0].put("Title", newtitle)
            settitle[0].save()
            setResult(1, intent)
            finish()
        }

        val add: ImageButton = findViewById(R.id.add_button)
        add.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("KEY_TID", titleID)
            val EditTextFragment = EditTextFragment()
            EditTextFragment.arguments = bundle
            EditTextFragment.show(supportFragmentManager, "EditTextFragment")
        }

        val arrayAdapter = MyArrayAdapter(this, 0).apply {
            while (i < size) {
                var get_contentname = contents_list[i].getString("content")
                if (get_contentname != null) {
                    add(ListItem(get_contentname))
                }
                i++
            }
        }
        val listView: ListView = findViewById(R.id.clistView)
        listView.adapter = arrayAdapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, pos, id ->
            val bundle = Bundle()

            var listitem: ListItem = parent.getItemAtPosition(pos) as ListItem
            var query = NCMBQuery<NCMBObject>("massage")
            query.whereEqualTo("content", listitem.koumoku)
            var cID = query.find()
            bundle.putString("KEY_ID", cID[0].getString("objectId"))
            bundle.putString("KEY_KOUMOKU", listitem.koumoku)

            val EdTF = EditTextFragment()
            EdTF.arguments = bundle
            EdTF.show(supportFragmentManager, "EditTextFragment")
        }
    }

    class ListItem(var koumoku: String)

    data class ViewHolder(var titleView: TextView, val deleteIcon: Button)

    class MyArrayAdapter : ArrayAdapter<ListItem> {

        private var inflater: LayoutInflater? =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

        constructor(context: Context, resource: Int) : super(context, resource) {}

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {


            var viewHolder: ViewHolder? = null
            var view = convertView

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

            val listItem = getItem(position)
            if (listItem!!.koumoku == ("+")) {
                viewHolder.deleteIcon.visibility = View.GONE
            }
            viewHolder.titleView.text = listItem!!.koumoku
            viewHolder.deleteIcon.setOnClickListener { _ ->
                val delete_query = NCMBQuery<NCMBObject>("massage")
                delete_query.whereEqualTo("content", listItem!!.koumoku)
                var dlete_item = delete_query.find()
                dlete_item[0].deleteObject()
                this.remove(listItem)
                this.notifyDataSetChanged()
            }

            return view!!
        }
    }
}