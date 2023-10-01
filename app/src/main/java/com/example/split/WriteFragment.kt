package com.example.split

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.split.databinding.FragmentWriteNFCBinding

class WriteFragment : Fragment(), NfcAdapter.ReaderCallback {

    private var _binding: FragmentWriteNFCBinding? = null
    private val binding get() = _binding!!

    private lateinit var bibInput: EditText
    private lateinit var writeNFCbtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWriteNFCBinding.inflate(inflater, container, false)

        val view = binding.root

        bibInput = view.findViewById(R.id.bibInput)
        writeNFCbtn = view.findViewById(R.id.writeNFCbtn)

        val mainActivity = MainActivity()
        mainActivity.setMode(true, "")

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