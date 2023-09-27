package com.example.split

import android.app.AlertDialog
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
        Log.i("wr", mainActivity.rm())

        writeNFCbtn.setOnClickListener{
            if(bibInput.text.toString() == ""){
                Toast.makeText(activity, "Text Empty!", Toast.LENGTH_SHORT).show()
            }else{
                showWriteDialog()
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

    fun showWriteDialog() {
        return this.let {
            val builder = AlertDialog.Builder(activity)

            val inflater = layoutInflater

            builder.setView(inflater.inflate(R.layout.write_dialog, null))

            builder.create()
        }.show()
    }


    override fun onTagDiscovered(p0: Tag?) {
        TODO("Not yet implemented")
    }


}