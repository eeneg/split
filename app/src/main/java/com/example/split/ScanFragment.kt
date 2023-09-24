package com.example.split

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.TextClock
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.split.Databasec.DBRaceLog
import com.example.split.databinding.FragmentScanNFCBinding

class ScanFragment : Fragment(){

    private var _binding: FragmentScanNFCBinding? = null
    private val binding get() = _binding!!

    private lateinit var time : TextClock
    private lateinit var textView2 : TextView
    private lateinit var switchScan : SwitchCompat
    private lateinit var data: ListView
    private lateinit var dbRaceLog: DBRaceLog

    private lateinit var sync: Button

    private lateinit var simpleCursorAdapter: SimpleCursorAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentScanNFCBinding.inflate(inflater, container, false)

        val view = binding.root

        time = view.findViewById(R.id.textClock)
        textView2 = view.findViewById(R.id.textView2)
        sync = view.findViewById(R.id.syncButton)
        switchScan = view.findViewById(R.id.switch1)
        data = view.findViewById(R.id.list)
        dbRaceLog = DBRaceLog(activity)


        val main = MainActivity()

        switchScan.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                main.setActiveScan(true)
            }else{
                main.setActiveScan(false)
            }
        }

        simpleCursorAdapter = SimpleCursorAdapter(
            activity,
            R.layout.race_log,
            dbRaceLog.getAllLogs(),
            arrayOf("bib", "time"),
            intArrayOf(R.id.bib, R.id.time)
        )

        data.adapter = simpleCursorAdapter
        simpleCursorAdapter.notifyDataSetChanged()

        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
