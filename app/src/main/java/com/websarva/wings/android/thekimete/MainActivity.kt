package com.websarva.wings.android.thekimete

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.nifcloud.mbaas.core.NCMB
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBQuery
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NCMB.initialize(
            this.getApplicationContext(),
            "45c70179afdfbee2e89e9baa3d02560c7f7c287d498cab8c36fe3cedf5acd985",
            "85beadbe849665472bb9ae2a734bfb27c3a7e268f888af328dabc460b6267912"
        )

        setContentView(R.layout.activity_main)

        val title_query = NCMBQuery<NCMBObject>("Title")
        var i = 1
        var title_name = title_query.find()
        var size = title_name.size
        var spinnerItems: ArrayList<String> = ArrayList()
        spinnerItems.add("LIST NEW をタッチ！")
        while (i < size) {
            var getTitleName = title_name[i].getString("Title")
            spinnerItems.add(getTitleName)
            i++
        }

        val spinner_picknumbers = arrayOf(0, 1, 2, 3, 4, 5)

        val listname_select = findViewById<Spinner>(R.id.listname_select)
        val picknumber_select = findViewById<Spinner>(R.id.picknumber_select)

        // ArrayAdapter
        val titlelistAdapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item, spinnerItems
        )
        val picknumberAdapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item, spinner_picknumbers
        )

        titlelistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        picknumberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        listname_select.adapter = titlelistAdapter
        picknumber_select.adapter = picknumberAdapter

        listname_select.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val spinnerParent = parent as Spinner
                val item = spinnerParent.selectedItem as String
                list_name.text = item
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        picknumber_select.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?, position: Int, id: Long
            ) {
                val picknumber_spinnerParent = parent as Spinner
                val picknumber_item = picknumber_spinnerParent.selectedItem as Int
                picknumber.text = picknumber_item.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        val list_set: Button = findViewById(R.id.list_set)

        list_set.setOnClickListener {
            val intent = Intent(this, TitleList::class.java)
            startActivity(intent)
        }

        val kimete: Button = findViewById(R.id.kimete)
        kimete.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("KEY_N", picknumber.text.toString())
            bundle.putString("KEY_TNname", list_name.text.toString())
            val result = ResultFragment()
            result.arguments = bundle
            result.show(supportFragmentManager, "ResultFragment")

        }
    }
}
