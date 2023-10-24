package com.example.split

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.split.DAO.TimeLogAdapter
import com.example.split.DAO.TimeLogApplication
import com.example.split.DAO.TimeLogViewModel
import com.example.split.DAO.TimeLogViewModelFactory
import com.example.split.databinding.FragmentScanNFCBinding
import com.google.gson.Gson
import org.json.JSONArray

class ScanFragment : Fragment(){

    private var _binding: FragmentScanNFCBinding? = null
    private val binding get() = _binding!!

    private lateinit var time : TextClock
    private lateinit var textView2 : TextView
    private lateinit var switchScan : SwitchCompat
    private lateinit var data: RecyclerView
    private lateinit var deleteAll: Button
    private lateinit var ipField : EditText

    private lateinit var sync: Button

    private lateinit var sharedPref : SharedPreferences

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
        ipField = view.findViewById(R.id.ipField)
        deleteAll = view.findViewById(R.id.deleteAllBtn)

        sharedPref = activity?.getSharedPreferences("key", Context.MODE_PRIVATE)!!

        val id = sharedPref.getString("id", null).toString()

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
            logs.let { it ->
                timeLogAdapter.submitList(it.filter { it.userId == id})
                data.smoothScrollToPosition(0)
            }
        }

        sync.setOnClickListener {
            if(ipField.text.isEmpty()){
                Toast.makeText(activity, "Empty URL", Toast.LENGTH_SHORT).show()
            }else{
                val queue = Volley.newRequestQueue(activity)
                val url = "http://"+ipField.text+"/checkpoints/sync"
                val gson = Gson()
                val data = JSONArray()
                val jsonData = gson.toJson(timeLogViewModel.allLogs)
                data.put(jsonData)
                val jsonArrayRequest = JsonArrayRequest(Request.Method.POST, url, data,
                    { response ->
                        Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show()
                    },
                    { error ->
                        Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show()
                    })
            }
        }

        deleteAll.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
            builder
                .setMessage("You won't be able to revert this!")
                .setTitle("Are you Sure?")
                .setPositiveButton("Confirm") { dialog, which ->
                    timeLogViewModel.deleteAll(id)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
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
