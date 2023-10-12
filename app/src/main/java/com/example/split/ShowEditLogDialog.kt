package com.example.split

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.split.DAO.TimeLog
import com.example.split.DAO.TimeLogApplication
import com.example.split.DAO.TimeLogViewModel
import com.example.split.DAO.TimeLogViewModelFactory
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ShowEditLogDialog(log: TimeLog, activity: Context): DialogFragment() {

    val iLog = log

    val hour = LocalTime.parse(iLog.time).format(DateTimeFormatter.ofPattern("kk"))

    val mins = LocalTime.parse(iLog.time).format(DateTimeFormatter.ofPattern("mm"))

    val secs = LocalTime.parse(iLog.time).format(DateTimeFormatter.ofPattern("ss"))

    private val timeLogViewModel: TimeLogViewModel by viewModels {
        TimeLogViewModelFactory((activity.applicationContext as TimeLogApplication).repo)
    }
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {

            val builder = AlertDialog.Builder(it)

            val inflater = layoutInflater

            val subView = inflater.inflate(R.layout.edit_log_dialog, null)

            val edit_bib: EditText = subView.findViewById(R.id.edit_bib)
            val edit_hour: NumberPicker = subView.findViewById(R.id.numpicker_hours)
            val edit_mins: NumberPicker = subView.findViewById(R.id.numpicker_mins)
            val edit_secs: NumberPicker = subView.findViewById(R.id.numpicker_secs)

            edit_bib.setText(iLog.bib)

            edit_hour.maxValue = 24
            edit_hour.minValue = 0
            edit_mins.maxValue = 60
            edit_mins.minValue = 0
            edit_secs.maxValue = 60
            edit_secs.minValue = 0

            edit_hour.value = hour.toInt()
            edit_mins.value = mins.toInt()
            edit_secs.value = secs.toInt()

            builder.setView(subView)
                .setTitle("Edit Log")
                .setNeutralButton("Delete"
                ) { _, _ ->
                    timeLogViewModel.delete(iLog)
                    Toast.makeText(subView.context, "Deleted!", Toast.LENGTH_SHORT).show()
                }
                .setPositiveButton("Save"
                ) { _, _ ->
                    if(edit_hour.value.toString().padStart(2,'0') + ":" + edit_mins.value.toString().padStart(2,'0') + ":" + edit_secs.value.toString().padStart(2,'0') == iLog.time && edit_bib.text.toString() == iLog.bib){
                        Toast.makeText(subView.context, "No changes made", Toast.LENGTH_SHORT).show()
                    }else{
                        timeLogViewModel.update(edit_hour.value.toString().padStart(2,'0') + ":" + edit_mins.value.toString().padStart(2,'0') + ":" + edit_secs.value.toString().padStart(2,'0'), edit_bib.text.toString(), iLog.id)
                        Toast.makeText(subView.context, "Saved!", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel"
                ) { _, _ ->
                    dialog?.cancel()
                }

            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")

    }

    override fun dismiss() {
        super.dismiss()
    }


}