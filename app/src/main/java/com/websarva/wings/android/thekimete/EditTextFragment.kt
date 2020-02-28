package com.websarva.wings.android.thekimete


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
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
    var cotext : Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.cotext = cotext
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments
        val builder = AlertDialog.Builder(activity)

        when (bundle!!.get("KEY_ID")) {

            null -> {
                activity?.let {
                    val view = it.layoutInflater.inflate(R.layout.fragment_edit_text, null)
                    var editText: EditText = view.findViewById(R.id.edit_text) // ←ここでedit_textを取得
                    editText.setText("", TextView.BufferType.EDITABLE)
                    builder.setView(view)
                        .setPositiveButton("OK") { _, _ ->

                            val text = editText.text.toString()
                            var nObject = NCMBObject("massage")
                            val contentsList = context as ContentsList

                            nObject.put("content", text)
                            nObject.put("TitleID",bundle.getString("KEY_TID"))
                            nObject.save()
                            bundle?.putString("KEY_RE", text)

                            contentsList.clickOkDialog()
                        }
                        .setNegativeButton("キャンセル") { _, _ ->
                        }

                }
            }

            else -> {

                activity?.let {
                    val view = it.layoutInflater.inflate(R.layout.fragment_edit_text, null)
                    var editText: EditText = view.findViewById(R.id.edit_text) // ←ここでedit_textを取得
                    editText.setText(bundle?.getString("KEY_KOUMOKU"), TextView.BufferType.EDITABLE)
                    builder.setView(view)
                        .setPositiveButton("OK") { _, _ ->

                            val text = editText.text.toString()
                            var query = NCMBQuery<NCMBObject>("massage")
                            val contentsList = context as ContentsList
                            query.whereEqualTo("objectId", bundle?.getString("KEY_ID"))
                            var updatte = query.find()
                            updatte[0].put("content", text)
                            updatte[0].save()
                            bundle?.putString("KEY_RE", text)
                            bundle?.putString("KEY_RID", bundle?.getString("KEY_ID"))


                            contentsList.clickOkDialog()
                        }
                        .setNegativeButton("キャンセル") { _, _ ->
                        }

                }
            }
        }
                return builder.create()


    }

    }



