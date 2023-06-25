package com.example.split

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.split.databinding.FragmentScanNFCBinding

class ScanFragment : Fragment() {

    private var _binding: FragmentScanNFCBinding? = null

    private val binding get() = _binding!!

    private lateinit var sync: Button
    private lateinit var writeNFC: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentScanNFCBinding.inflate(inflater, container, false)

        val view = binding.root

        sync = view.findViewById(R.id.syncButton)

        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}