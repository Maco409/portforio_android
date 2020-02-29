package com.websarva.wings.android.thekimete


import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBQuery
import kotlin.math.min
import kotlin.random.Random

class ResultFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments
        val builder = AlertDialog.Builder(activity)
        val title_query = NCMBQuery<NCMBObject>("Title")
        val massage_query = NCMBQuery<NCMBObject>("massage")
        title_query.whereEqualTo("Title", bundle?.getString("KEY_TNname"))
        val title_list = title_query.find()

        if (title_list.size == 0) {
            builder.setTitle("新しいリストを作ってください")
        } else {
            massage_query.whereEqualTo("TitleID", title_list[0].getString("objectId"))
            val contents = massage_query.find()

            var picknumber = bundle?.getString("KEY_N")

            builder.setTitle("決まりました！")

            when (picknumber!!.toInt()) {
                0 -> {
                    builder.setMessage("\nThe Best Answer is in your heart...")
                }

                else -> {

                    var mincount = min(contents.size, picknumber!!.toInt())
                    var con: String = ""

                    for (i in 0 until mincount) {
                        var randomInt = Random.nextInt(contents.size)
                        con = con + "\n" + contents[randomInt].getString("content")
                        contents.removeAt(randomInt)
                    }

                    builder.setMessage(con)
                }
            }
        }

        builder.setPositiveButton("ok", DialogButtonClickListener())
        val dialog = builder.create()
        return dialog
    }

    private inner class DialogButtonClickListener : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
        }
    }

}
