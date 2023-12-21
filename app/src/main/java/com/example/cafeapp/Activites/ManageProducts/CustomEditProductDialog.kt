package com.example.cafeapp.Activites.ManageProducts

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.cafeapp.R
import com.example.cafeapp.ViewModels.ManageProductsViewModel

class CustomEditProductDialog(private val productId: Int) : DialogFragment() {

    private lateinit var viewModel: ManageProductsViewModel
    private lateinit var editTextProductTitle: EditText
    private lateinit var editTextProductPrice: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            viewModel = ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            )[ManageProductsViewModel::class.java]

            // Inflate the custom layout
            val view =
                requireActivity().layoutInflater.inflate(R.layout.custom_edit_product_dialog, null)

            // Find the EditText views within the layout
            editTextProductTitle = view.findViewById(R.id.editTextTitle)
            editTextProductPrice = view.findViewById(R.id.editTextPrice)

            val builder = AlertDialog.Builder(it)
                .setView(view)
                .setPositiveButton("Save") { dialog, id ->
                    // Call the saveUpdates function here
                    if (saveUpdates()) {

                    }
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    getDialog()?.cancel()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun saveUpdates(): Boolean {
        val priceText = editTextProductPrice.text.toString().trim()

        val price = if (priceText.isNotEmpty()) {
            try {
                priceText.toFloat()
            } catch (e: NumberFormatException) {
                 Toast.makeText(
                    requireContext(),
                    "Invalid Number!",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
        } else {
            0.0f
        }


        val result =
            viewModel.updateProduct(productId, editTextProductTitle.text.toString().trim(), price)
        if (result) Toast.makeText(
            requireContext(),
            "Database updated! Please restart app to see changes",
            Toast.LENGTH_LONG
        ).show()
        return result
    }
}
