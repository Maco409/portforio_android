package com.websarva.wings.android.thekimete


import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBQuery
import kotlinx.android.synthetic.main.activity_title_list.*

/**
 * A simple [Fragment] subclass.
 */
class EditTextFragment : DialogFragment() {



    private var cID: String? = null
    private var message: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cID = it.getString(ARG_cId)
            message = it.getString(ARG_MESSAGE)
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
            activity?.let {
            val view = it.layoutInflater.inflate(R.layout.fragment_edit_text, null)
                var editText: EditText = view.findViewById(R.id.edit_text) // ←ここでedit_textを取得
                editText.setText(message,TextView.BufferType.EDITABLE)
            builder.setView(view)
                .setPositiveButton("OK") { _, _ ->

                    val text = editText.text.toString()
                    var query = NCMBQuery<NCMBObject>("massage")
                    query.whereEqualTo("objectId",cID)
                    var updatte = query.find()
                    updatte[0].put("content",text)
                    updatte[0].save()

                }
                .setNegativeButton("キャンセル") { _, _ ->

                }

        }
        return builder.create()

    }


    companion object {
        const val TAG = "EditTextFragment"
        private const val ARG_cId = "argcId"
        private const val ARG_MESSAGE = "argMessage"
        fun newInstance(title: String, message: String) = EditTextFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_cId, cID)
                putString(ARG_MESSAGE, message)
            }
        }
    }
}



