package com.example.split

import android.content.Context
import android.content.SharedPreferences
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.split.DAO.Event.EventViewModel
import com.example.split.DAO.Event.EventViewModelFactory
import com.example.split.DAO.Participant.ParticipantAdapter
import com.example.split.DAO.Participant.ParticipantViewModel
import com.example.split.DAO.Participant.ParticipantViewModelFactory
import com.example.split.DAO.TimeLogApplication
import com.example.split.databinding.FragmentWriteNFCBinding

class WriteFragment : Fragment(), NfcAdapter.ReaderCallback {

    private var _binding: FragmentWriteNFCBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref : SharedPreferences

    private lateinit var bibInput: EditText
    private lateinit var writeNFCbtn: Button

    private lateinit var eventSpinner: Spinner

    private lateinit var writeBibRecyclerView: RecyclerView

    private lateinit var writeSyncBtn: Button

    private val eventViewModel: EventViewModel by viewModels {
        EventViewModelFactory((activity?.application as TimeLogApplication).eventRepo)
    }

    private val participantViewModel: ParticipantViewModel by viewModels {
        ParticipantViewModelFactory((activity?.application as TimeLogApplication).participantRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWriteNFCBinding.inflate(inflater, container, false)

        val view = binding.root

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        bibInput = view.findViewById(R.id.bibInput)
        writeNFCbtn = view.findViewById(R.id.writeNFCbtn)

        eventSpinner = view.findViewById(R.id.evenSpinner)

        writeBibRecyclerView = view.findViewById(R.id.writeBibRecyclerView)

        writeSyncBtn = view.findViewById(R.id.writeSyncBtn)

        sharedPref = activity?.getSharedPreferences("key", Context.MODE_PRIVATE)!!
        val id = sharedPref.getString("id", null).toString()
        val mainActivity = MainActivity()
        mainActivity.setMode(true, "")


        eventViewModel.allEvent.observe(requireActivity()) { event ->
            val adapter = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_item, event.filter { it.userId == id }.map { it.eventName })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            eventSpinner.adapter = adapter
        }

        val participantAdapter = ParticipantAdapter()
        writeBibRecyclerView.adapter = participantAdapter
        writeBibRecyclerView.layoutManager = LinearLayoutManager(activity)

        participantViewModel.allParticipant.observe(requireActivity()) { participants ->
            participants.let { it ->
                participantAdapter.submitList(it.filter { it.userID == id})
                writeBibRecyclerView.smoothScrollToPosition(0)
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

        return view
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