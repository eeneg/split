package com.example.split

import android.app.AlertDialog
import android.content.Context

class ShowGenericDialog(private val context: Context, private val status: String,private val message: String) {
    private var dialog: AlertDialog? = null

    fun showGenericDialog() {
        val builder = AlertDialog.Builder(context)

        builder
            .setTitle(status)
            .setMessage(message)
            .setNegativeButton("Close") { dialog, which ->
                dialog.dismiss()
            }
        val dialog: AlertDialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        builder.setCancelable(false)
        dialog.show()

    }

    fun hideGenericDialog() {
        dialog?.dismiss()
    }
}