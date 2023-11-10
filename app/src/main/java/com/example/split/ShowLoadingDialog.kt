package com.example.split

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater

class ShowLoadingDialog(private val context: Context) {
    private var dialog: AlertDialog? = null

    fun showLoading() {
        val builder = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.fragment_loading_dialog, null)

        builder.setView(view)
        builder.setCancelable(false) // Make the dialog non-cancelable

        dialog = builder.create()
        dialog?.setCanceledOnTouchOutside(false) // Disable cancel on outside touch

        dialog?.show()
    }

    fun hideLoading() {
        dialog?.dismiss()
    }
}