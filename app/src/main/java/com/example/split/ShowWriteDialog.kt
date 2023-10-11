package com.example.split

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ShowWriteDialog() : DialogFragment() {
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val mainActivity = MainActivity()

        return activity?.let {

            val builder = AlertDialog.Builder(it)

            val inflater = layoutInflater

            builder.setView(inflater.inflate(R.layout.write_dialog, null))
                .setNegativeButton("Cancel"
                ) { _, _ ->
                    dialog?.cancel()
                    mainActivity.setMode(true, "")
                }

            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")

    }

    override fun dismiss() {
        super.dismiss()
    }


}