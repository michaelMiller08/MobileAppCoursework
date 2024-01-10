package com.example.cafeapp.Activites.ManageProducts

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.cafeapp.Helpers.UserRole
import com.example.cafeapp.Models.ProductModel
import com.example.cafeapp.R
import com.example.cafeapp.ViewModels.CheckoutViewModel
import com.example.cafeapp.ViewModels.ManageProductsViewModel


class CustomCheckoutDialog(totalPrice: Float,     private val products: List<ProductModel>
) : DialogFragment() {

    private lateinit var viewModel: ManageProductsViewModel
    private lateinit var checkoutViewModel: CheckoutViewModel
    private lateinit var spinnerPaymentType: Spinner
    private lateinit var textViewAmount: TextView
    private var amount: Float = totalPrice

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            viewModel = ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            )[ManageProductsViewModel::class.java]
            checkoutViewModel = ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            )[CheckoutViewModel::class.java]

            // Inflate the custom layout
            val view =
                requireActivity().layoutInflater.inflate(R.layout.custom_checkout_dialog, null)

            // Find the EditText views within the layout
            spinnerPaymentType = view.findViewById(R.id.paymentTypeSpinner)
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.payment_types_array, // Use your array resource here
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinnerPaymentType.adapter = adapter
            }

            textViewAmount = view.findViewById(R.id.paymentAmount)
            textViewAmount.text = this.amount.toString()

            val builder = AlertDialog.Builder(it)
                .setView(view)
                .setPositiveButton("Confirm") { dialog, id ->
                    if (checkoutViewModel.getCurrentUserRole() == UserRole.Admin) {
                        Toast.makeText(
                            requireContext(),
                            "Admins can not checkout! Please sign in as customer.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {

                        //remove currency signs
                        //best practice would to make sure they arent there in the first place
//                        var amount = textViewAmount.text.replace("[^0-9.]".toRegex(), "")
                        createOrder(
                            getSelectedPaymentType(),
                            this.amount
                        )

                    }


                }
                .setNegativeButton("Cancel") { dialog, id ->
                    getDialog()?.cancel()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    private fun createOrder(paymentType: String, paymentAmount: Float) {

        checkoutViewModel.createOrder(paymentType, paymentAmount, this.products)
    }

    private fun getSelectedPaymentType(): String {
        return spinnerPaymentType.selectedItem.toString()
    }
}
