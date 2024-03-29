package com.example.split

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.split.DAO.TimeLog.TimeLogAdapter
import com.example.split.DAO.TimeLog.TimeLogViewModel
import com.example.split.DAO.TimeLog.TimeLogViewModelFactory
import com.example.split.DAO.TimeLogApplication
import com.example.split.Database.DBHelper
import com.example.split.databinding.FragmentScanNFCBinding

class ScanFragment : Fragment(){

    private var _binding: FragmentScanNFCBinding? = null
    private val binding get() = _binding!!

    private lateinit var time : TextClock
    private lateinit var textView2 : TextView
    private lateinit var switchScan : SwitchCompat
    private lateinit var data: RecyclerView
    private lateinit var deleteAll: Button

    private lateinit var sync: Button

    private lateinit var sharedPref : SharedPreferences

    private lateinit var database:DBHelper

    private val timeLogViewModel: TimeLogViewModel by viewModels {
        TimeLogViewModelFactory((activity?.application as TimeLogApplication).repo)
    }

    @RequiresApi(Build.VERSION_CODES.P)
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
        deleteAll = view.findViewById(R.id.deleteAllBtn)

        database = DBHelper(activity)

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
            val loadingDialog = ShowLoadingDialog(requireActivity())
            val token = database.getToken(id)
            val ipAddress = database.getIPAddress(id)
            if(ipAddress == null){
                Toast.makeText(activity, "Empty IP Address", Toast.LENGTH_SHORT).show()
            }else if(token == null) {
                Toast.makeText(activity, "User does not have a token", Toast.LENGTH_SHORT).show()
            }else{
                val identifierDb = database.getIdentifier(id)

                if(identifierDb != null){
                    loadingDialog.showLoading()
                    val queue = Volley.newRequestQueue(activity)
                    val url = "http://$ipAddress/checkpoints/sync"
                    val jsonRequest = object : StringRequest(
                        Request.Method.POST, url,
                        Response.Listener { response ->
                            Toast.makeText(activity, "Synced Successfully", Toast.LENGTH_SHORT).show()
                            loadingDialog.hideLoading()
                            Log.i("succ", response.toString())
                        },
                        Response.ErrorListener { error ->
                            val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
                            builder
                                .setTitle("Error!")
                                .setMessage(error.toString())
                                .setNegativeButton("Close") { dialog, which ->
                                    dialog.dismiss()
                                }
                                .setCancelable(false)
                            val dialog: AlertDialog = builder.create()
                            loadingDialog.hideLoading()
                            dialog.show()
                            Log.i("err", error.toString())
                        })
                    {
                        @Override
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Authorization"] = "Bearer $token"
                            return headers
                        }
                        @Override
                        override fun getParams(): MutableMap<String, String>{
                            val splits = hashMapOf<String, String>()
                            timeLogViewModel.allLogs.value?.forEachIndexed { i, data ->
                                if (data.userId == id){
                                    splits["splits[$i][bib]"] = data.bib
                                    splits["splits[$i][time]"] = data.date + " " + data.time
                                }
                            }
                            splits.put("identifiers", identifierDb.toString())
                            println(splits)
                            return splits
                        }
                    }
                    queue.add(jsonRequest)
                }else{
                    Toast.makeText(activity, "Please set Identifiers first in the Profile Tab", Toast.LENGTH_LONG).show()
                }
            }
        }

        deleteAll.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
            builder
                .setTitle("Are you Sure?")
                .setMessage("You won't be able to revert this!")
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
