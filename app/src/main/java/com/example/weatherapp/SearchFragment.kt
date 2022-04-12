package com.example.weatherapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Search"
        binding = FragmentSearchBinding.bind(view)
        viewModel = SearchViewModel()
    }

    override fun onResume() {
        super.onResume()
        binding.zipCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.toString()?.let {viewModel.updateZipCode(it) }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        viewModel.enableButton.observe(this) { enable ->
            binding.searchButton.isEnabled = enable
        }

        binding.searchButton.setOnClickListener{
            val action = SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(viewModel.getZipCode())
            findNavController().navigate(action)
        }
    }
}