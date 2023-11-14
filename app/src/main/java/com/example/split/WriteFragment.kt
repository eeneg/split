package com.example.split

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.split.DAO.Event.EventViewModel
import com.example.split.DAO.Event.EventViewModelFactory
import com.example.split.DAO.Participant.Participant
import com.example.split.DAO.Participant.ParticipantAdapter
import com.example.split.DAO.Participant.ParticipantViewModel
import com.example.split.DAO.Participant.ParticipantViewModelFactory
import com.example.split.DAO.Participants.ParticipantDataInterface
import com.example.split.DAO.TimeLogApplication
import com.example.split.Database.DBHelper
import com.example.split.databinding.FragmentWriteNFCBinding
import com.google.gson.Gson
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import org.json.JSONArray
import org.json.JSONObject

class WriteFragment : Fragment(), NfcAdapter.ReaderCallback, ParticipantDataInterface {

    private var _binding: FragmentWriteNFCBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref : SharedPreferences

    private lateinit var bibInput: EditText
    private lateinit var writeNFCbtn: Button

    private lateinit var writeSearchField: EditText
    private lateinit var writeSearchBtn: Button
    private lateinit var writeSearchClearBtn: Button
    private lateinit var asd: TextView

    private lateinit var writeBibRecyclerView: RecyclerView

    private lateinit var writeSyncBtn: Button
    private lateinit var writeEraseBtn: Button

    private val eventViewModel: EventViewModel by viewModels {
        EventViewModelFactory((activity?.application as TimeLogApplication).eventRepo)
    }

    private val participantViewModel: ParticipantViewModel by viewModels {
        ParticipantViewModelFactory((activity?.application as TimeLogApplication).participantRepo)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWriteNFCBinding.inflate(inflater, container, false)

        val view = binding.root

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        bibInput = view.findViewById(R.id.bibInput)
        writeNFCbtn = view.findViewById(R.id.writeNFCbtn)

        writeSearchField = view.findViewById(R.id.writeSearchField)
        writeSearchBtn = view.findViewById(R.id.writeSearchBtn)
        writeSearchClearBtn = view.findViewById(R.id.writeSearchClearBtn)
        asd = view.findViewById(R.id.asd)

        writeBibRecyclerView = view.findViewById(R.id.writeBibRecyclerView)

        writeSyncBtn = view.findViewById(R.id.writeSyncBtn)
        writeEraseBtn = view.findViewById(R.id.writeEraseBtn)

        sharedPref = activity?.getSharedPreferences("key", Context.MODE_PRIVATE)!!
        val id = sharedPref.getString("id", null).toString()
        val mainActivity = MainActivity()
        mainActivity.setMode(true, "")

        val participantAdapter = ParticipantAdapter(this)
        writeBibRecyclerView.adapter = participantAdapter
        writeBibRecyclerView.layoutManager = LinearLayoutManager(activity)

        participantViewModel.allParticipant.observe(requireActivity()) { participants ->
            writeSearchBtn.setOnClickListener {
                if(writeSearchField.text.isNotEmpty()){
                    participantAdapter.submitList(participants.filter { (it.userID == id) && (it.bib.contains(writeSearchField.text.toString())) })
                }else{
                    participantAdapter.submitList(participants.filter { it.userID == id })
                }
            }
            writeSearchClearBtn.setOnClickListener {
                writeSearchField.setText("")
                writeSearchField.clearFocus()
                participants.let { it ->
                    participantAdapter.submitList(it.filter { it.userID == id})
                    writeBibRecyclerView.smoothScrollToPosition(0)
                }
            }
            participants.let { it ->
                participantAdapter.submitList(it.filter { it.userID == id})
                writeBibRecyclerView.smoothScrollToPosition(0)
            }
        }

        val qrCode = registerForActivityResult(ScanContract()) { result ->
            if (result.contents == null) {
                Toast.makeText(activity, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                val gson = Gson()
                val data = JSONObject(result.contents)
                val event = data.getJSONObject("event").getString("id")
                val eventName = data.getJSONObject("event").getString("name")
                syncRequest(event, eventName, id)
            }
        }


        writeNFCbtn.setOnClickListener{
            if(bibInput.text.toString() == ""){
                Toast.makeText(activity, "Text Empty!", Toast.LENGTH_SHORT).show()
            }else{
                ShowWriteDialog().show((activity as AppCompatActivity).supportFragmentManager, "write dialog")
                mainActivity.setMode(true, bibInput.text.toString())
                bibInput.setText("")
                bibInput.clearFocus()
            }
        }

        writeEraseBtn.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
            builder
                .setTitle("Are you Sure?")
                .setMessage("You won't be able to revert this!")
                .setPositiveButton("Confirm") { dialog, which ->
                    participantViewModel.deleteAll(id)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        writeSyncBtn.setOnClickListener {
            val options = ScanOptions()
            options.setPrompt("Scan qr code")
            options.setBeepEnabled(true)
            qrCode.launch(options)
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun syncRequest(id: String, eventName: String, userId: String){
        val database = DBHelper(activity)
        val ipAddress = database.getIPAddress(userId)
        val url = "http://$ipAddress/api/events/$id"
        val requestQueue: RequestQueue = Volley.newRequestQueue(activity)
        val loadingDialog = ShowLoadingDialog(requireActivity())
        loadingDialog.showLoading()
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val data = JSONObject(response)
                val participants = data.getJSONArray("participants")
                loadingDialog.hideLoading()
                handleParticipants(participants, id, eventName, userId)
            },
            { error ->
                ShowGenericDialog(requireActivity(), "Error!", error.toString()).showGenericDialog()
                loadingDialog.hideLoading()
            })
        requestQueue.add(stringRequest)
    }
    fun handleParticipants(participants: JSONArray, eventId: String, eventName: String, userId:String){
        for (i in 0 until participants.length()){
            val data = participants.getJSONObject(i)
            val name = data.getJSONObject("name").getString("full")
            val participant = Participant(data.getString("bib"), name, eventId, eventName, userId)
            participantViewModel.insert(participant)
        }
    }
    override fun onDataItemClicked(data: String) {
        bibInput.setText(data)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        val mainActivity = MainActivity()
        mainActivity.setMode(false, "")
        mainActivity.setActiveScan(false)
        _binding = null
    }

    override fun onTagDiscovered(p0: Tag?) {
        TODO("Not yet implemented")
    }


}