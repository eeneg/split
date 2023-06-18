package com.example.split

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.split.databinding.FragmentScanNFCBinding

class scanNFC : Fragment() {

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
        writeNFC = view.findViewById(R.id.writeNFC)

        writeNFC.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_scanNFC2_to_writeNFC2)
        }
        setHasOptionsMenu(true)
        return view

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }



}