package com.websarva.wings.android.thekimete

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.nifcloud.mbaas.core.NCMB

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NCMB.initialize(this.getApplicationContext(),"45c70179afdfbee2e89e9baa3d02560c7f7c287d498cab8c36fe3cedf5acd985","85beadbe849665472bb9ae2a734bfb27c3a7e268f888af328dabc460b6267912");
        setContentView(R.layout.activity_main)

        var set : Button = findViewById(R.id.ListSet)

        set.setOnClickListener {
            val intent = Intent(this,TitleList::class.java)
            startActivity(intent)
        }
    }
}
