package com.websarva.wings.android.thekimete


import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.nifcloud.mbaas.core.NCMB
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBQuery
import kotlin.math.min
import kotlin.random.Random

class ResultFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments
        val builder = AlertDialog.Builder(activity)
        val tq = NCMBQuery<NCMBObject>("Title")
        val query = NCMBQuery<NCMBObject>("massage")
        tq.whereEqualTo("Title",bundle?.getString("KEY_TNname"))
        val tID = tq.find()
        query.whereEqualTo("TitleID",tID[0].getString("objectId"))
        val contents = query.find()

        var tcount = contents.size



        var number = bundle?.getString("KEY_N")

        builder.setTitle("決まりました！")
        var mincount = min(contents.size, number!!.toInt())
        var con : String  = ""

        for (i in 0 until mincount) {
            var randomInt = Random.nextInt(contents.size)
            con = con + "\n" + contents[randomInt].getString("content")
            contents.removeAt(randomInt)
        }

        builder.setMessage(con)

        builder.setPositiveButton("ok",DialogButtonClickListener())
        val dialog = builder.create()
        return dialog
    }

    private inner class DialogButtonClickListener : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
        }
    }

}
