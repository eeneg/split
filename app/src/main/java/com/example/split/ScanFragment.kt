package com.example.split

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextClock
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.split.DAO.TimeLogAdapter
import com.example.split.DAO.TimeLogApplication
import com.example.split.DAO.TimeLogViewModel
import com.example.split.DAO.TimeLogViewModelFactory
import com.example.split.Databasec.DBRaceLog
import com.example.split.databinding.FragmentScanNFCBinding

class ScanFragment : Fragment(){

    private var _binding: FragmentScanNFCBinding? = null
    private val binding get() = _binding!!

    private lateinit var time : TextClock
    private lateinit var textView2 : TextView
    private lateinit var switchScan : SwitchCompat
    private lateinit var data: RecyclerView
    private lateinit var dbRaceLog: DBRaceLog

    private lateinit var sync: Button

    private val timeLogViewModel: TimeLogViewModel by viewModels {
        TimeLogViewModelFactory((activity?.application as TimeLogApplication).repo)
    }

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

        val timeLogAdapter = TimeLogAdapter()
        data.adapter = timeLogAdapter
        data.layoutManager = LinearLayoutManager(activity)

        timeLogViewModel.allLogs.observe(requireActivity()) { logs ->
            logs.let {
                timeLogAdapter.submitList(it)
                data.smoothScrollToPosition(0)
            }
        }

        val queue = Volley.newRequestQueue(activity)
        val url = "http://172.22.100.126/checkpoints/sync"

        val stringRequest = StringRequest(
            Request.Method.POST, url,
            { response ->
                Log.i("res", response.substring(0, 500))
            },
            { e ->
                Log.i("err", e.toString())
            })

        sync.setOnClickListener {
//            val log = TimeLog("0001", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString())
//            timeLogViewModel.insert(log)
            queue.add(stringRequest)
        }

        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        val mainActivity = MainActivity()
        mainActivity.setActiveScan(false)
        _binding = null
    }

}
