package com.websarva.wings.android.thekimete

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.nifcloud.mbaas.core.NCMB
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBQuery
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.lang.reflect.Array
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {


    // spinner選択肢
    //private val spinnerItems = arrayOf("Spinner", "Android", "Apple", "Windows")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        NCMB.initialize(this.getApplicationContext(),"45c70179afdfbee2e89e9baa3d02560c7f7c287d498cab8c36fe3cedf5acd985","85beadbe849665472bb9ae2a734bfb27c3a7e268f888af328dabc460b6267912");
        setContentView(R.layout.activity_main)

        val adb = findViewById<Button>(R.id.addbutton)

//        adb.setOnClickListener {
//
//        }

        val query = NCMBQuery<NCMBObject>("Title")
        var i = 0
        var titlename = query.find()
        var size = titlename.size
        var spinnerItems : ArrayList<String> = ArrayList()
        while (i < size) {
            var o = titlename[i].getString("Title")
            spinnerItems.add(o)
            i++
        }
        val spinnerNumbers = arrayOf(0,1,2,3,4,5)

        val listnameselect = findViewById<Spinner>(R.id.listnameselect)
        val numberselect = findViewById<Spinner>(R.id.numberselect)

        // ArrayAdapter
        val adapter = ArrayAdapter(applicationContext,
            android.R.layout.simple_spinner_item, spinnerItems)
        val nadapter = ArrayAdapter(applicationContext,
            android.R.layout.simple_spinner_item, spinnerNumbers)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        nadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // spinner に adapter をセット
        // Kotlin Android Extensions
        listnameselect.adapter = adapter
        numberselect.adapter = nadapter

        // リスナーを登録
        listnameselect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            //　アイテムが選択された時
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?, position: Int, id: Long
            ) {
                val spinnerParent = parent as Spinner
                val item = spinnerParent.selectedItem as String
                // Kotlin Android Extensions
                listname.text = item
            }
            //　アイテムが選択されなかった
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }

        // リスナーを登録
        numberselect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            //　アイテムが選択された時
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?, position: Int, id: Long
            ) {
                val nspinnerParent = parent as Spinner
                val nitem = nspinnerParent.selectedItem as Int
                // Kotlin Android Extensions
                number.text = nitem.toString()
            }
            //　アイテムが選択されなかった
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }


        val set : Button = findViewById(R.id.ListSet)

        set.setOnClickListener {
            val intent = Intent(this,TitleList::class.java)
            startActivity(intent)
        }

        val kimete : Button = findViewById(R.id.kimete)
        kimete.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("KEY_N",number.text.toString())
            bundle.putString("KEY_TNname",listname.text.toString())
            val result = ResultFragment()
            result.arguments = bundle
            result.show(supportFragmentManager,"ResultFragment")

        }
    }
}
