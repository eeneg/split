package com.example.split

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.split.databinding.FragmentWriteNFCBinding

class WriteFragment : Fragment(), NfcAdapter.ReaderCallback {

    private var _binding: FragmentWriteNFCBinding? = null
    private val binding get() = _binding!!

    private lateinit var bibInput: EditText
    private lateinit var writeNFCbtn: Button

    private lateinit var approach_nfc_text: TextView
    private lateinit var nfc_icon: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWriteNFCBinding.inflate(inflater, container, false)

        val view = binding.root

        bibInput = view.findViewById(R.id.bibInput)
        writeNFCbtn = view.findViewById(R.id.writeNFCbtn)
        approach_nfc_text = view.findViewById(R.id.approach_nfc_text)
        nfc_icon = view.findViewById(R.id.nfc_icon)

        approach_nfc_text.visibility = View.INVISIBLE
        nfc_icon.visibility = View.INVISIBLE

        writeNFCbtn.setOnClickListener{
            val mainActivity = MainActivity()
            if(bibInput.text.toString() == ""){
                Toast.makeText(activity, "Text Empty!", Toast.LENGTH_SHORT).show()
            }else{
                mainActivity.setMode(true, bibInput.text.toString())
                mainActivity.setActiveScan(true)
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