package com.websarva.wings.android.thekimete


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBQuery


class EditTextFragment : DialogFragment() {
    var set_context: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.set_context = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments
        val builder = AlertDialog.Builder(activity)


        when (bundle!!.get("KEY_ID")) {
            null -> {
                activity?.let {
                    val view = it.layoutInflater.inflate(R.layout.fragment_edit_text, null)
                    var editText: EditText = view.findViewById(R.id.edit_text)
                    editText.setText("入力してください", TextView.BufferType.EDITABLE)
                    val ok_button = view.findViewById<ImageButton>(R.id.ok)
                    ok_button.setOnClickListener() {
                        val text = editText.text.toString()
                        var massage_object = NCMBObject("massage")
                        val contentsList = context as ContentsList

                        massage_object.put("content", text)
                        massage_object.put("TitleID", bundle.getString("KEY_TID"))
                        massage_object.save()
                        bundle?.putString("KEY_RE", text)

                        contentsList.clickOkDialog()
                        dismiss()

                    }
                    val cancel_button = view.findViewById<ImageButton>(R.id.cancel)
                    cancel_button.setOnClickListener() {
                        dismiss()
                    }
                    builder.setView(view)
                }
            }

            else -> {

                activity?.let {
                    val view = it.layoutInflater.inflate(R.layout.fragment_edit_text, null)
                    var editText: EditText = view.findViewById(R.id.edit_text)
                    editText.setText(bundle?.getString("KEY_KOUMOKU"), TextView.BufferType.EDITABLE)
                    val ok_button = view.findViewById<ImageButton>(R.id.ok)
                    ok_button.setOnClickListener() {
                        val text = editText.text.toString()
                        var massage_query = NCMBQuery<NCMBObject>("massage")
                        val contentsList = context as ContentsList
                        massage_query.whereEqualTo("objectId", bundle?.getString("KEY_ID"))
                        var update = massage_query.find()
                        update[0].put("content", text)
                        update[0].save()
                        bundle?.putString("KEY_RE", text)
                        bundle?.putString("KEY_RID", bundle?.getString("KEY_ID"))

                        contentsList.clickOkDialog()
                        dismiss()
                    }
                    val cancel_button = view.findViewById<ImageButton>(R.id.cancel)
                    cancel_button.setOnClickListener() {
                        dismiss()
                    }
                    builder.setView(view)
                }
            }
        }
        return builder.create()

    }

}



