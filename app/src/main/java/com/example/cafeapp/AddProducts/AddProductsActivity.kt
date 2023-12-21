package com.example.cafeapp.AddProducts

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cafeapp.Models.ProductModel
import com.example.cafeapp.R
import com.example.cafeapp.ViewModels.AddProductsViewModel

class AddProductsActivity : AppCompatActivity() {

    private lateinit var viewModel: AddProductsViewModel
    private lateinit var productImg: ImageView
    private lateinit var uploadImgButton: Button
    private lateinit var productName: EditText
    private lateinit var productPrice: EditText
    private lateinit var uploadProductButton: Button
    private lateinit var getContent: ActivityResultLauncher<String>


    private var currentImageBytes: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_products)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[AddProductsViewModel::class.java]

        productImg = findViewById(R.id.productImg)
        uploadImgButton = findViewById(R.id.btnUploadImage)
        productName = findViewById(R.id.editTextProductName)
        productPrice = findViewById(R.id.editTextProductPrice)
        uploadProductButton = findViewById(R.id.upload)

        uploadProductButton.setOnClickListener { uploadProduct() }

        uploadImgButton.setOnClickListener { onUploadImageClick() }
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val fileSize = getFileSize(uri, this)
                val maxSizeInBytes = 1 * 1024 * 1024 // 1 MB

                if (fileSize <= maxSizeInBytes) {
                    val imageByteArray = uriToByteArray(uri, this)
                    currentImageBytes = imageByteArray
                    setProductImage(imageByteArray)

                } else {
                    // File size exceeds the limit, show a Toast message
                    Toast.makeText(this, "Image size exceeds 1 MB limit", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setProductImage(imageBytes: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        productImg.setImageBitmap(bitmap)
    }

    private fun getFileSize(uri: Uri, context: Context): Long {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        return cursor?.use { cursor ->
            val sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
            cursor.moveToFirst()
            cursor.getLong(sizeIndex)
        } ?: 0
    }

    private fun onUploadImageClick() {
        getContent.launch("image/*")
    }

    private fun uriToByteArray(uri: Uri, context: Context): ByteArray {
        return context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.readBytes()
        } ?: ByteArray(0)
    }

    private fun uploadProduct() {
        val editTextList = listOf(productName, productPrice)


        for (editText in editTextList) {
            if (editText.text.isEmpty()) {
                editText.error = editText.hint.toString() + " required! "
                return
            }
        }

        val product = ProductModel(0, productName.text.toString(), productPrice.text.toString().toDouble().toFloat(), currentImageBytes, true)
       try {
           viewModel.addProduct(product)
           productName.text.clear()
           productPrice.text.clear()
            productImg.setImageResource(R.drawable.placeholderimg)
           Toast.makeText(this, "Product Added! May need to restart app to reflect changes", Toast.LENGTH_LONG).show()

       }
       catch (e: Exception){
           Toast.makeText(this, "DB Error!", Toast.LENGTH_LONG).show()
       }

    }
}
