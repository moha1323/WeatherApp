package com.example.weatherapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController

class ErrorDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage("Error fetching data for that zip code/latitude & longitude")
            .setPositiveButton(R.string.ok) { dialog: DialogInterface, which: Int ->
                findNavController().navigate(R.id.searchFragment)
            }
            .create()

    companion object{
        const val TAG = "ErrorDialogFragment"
    }
}

