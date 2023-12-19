package com.example.cafeapp.Activites.ManageProducts

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.R
import com.example.cafeapp.ViewModels.ManageProductsViewModel

class ManageProductsActivity : AppCompatActivity(), ReplaceImageClickListener {
    private val viewModel: ManageProductsViewModel by viewModels()
    private lateinit var getContent: ActivityResultLauncher<String>
    private var productId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_products)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val adapter = ManageProductsAdapter(viewModel.getProducts(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            // Handle the selected image URI here
            if (uri != null) {
                val fileSize = getFileSize(uri, this)
                val maxSizeInBytes = 1 * 1024 * 1024 // 1 MB

                if (fileSize <= maxSizeInBytes) {
                    val imageByteArray = uriToByteArray(uri, this)
                    if (viewModel.replaceProductImage(this.productId, imageByteArray)) {
                        // Image replacement successful, you may update the UI or show a message
                        Toast.makeText(this, "Image Updated! Refresh app to see changes", Toast.LENGTH_LONG).show()
                    } else {
                        // Image replacement failed, handle the error if needed
                    }
                } else {
                    // File size exceeds the limit, show a Toast message
                    Toast.makeText(this, "Image size exceeds 1 MB limit", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getFileSize(uri: Uri, context: Context): Long {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        return cursor?.use { cursor ->
            val sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
            cursor.moveToFirst()
            cursor.getLong(sizeIndex)
        } ?: 0
    }

    override fun onReplaceImageClick(productId: Int) {
        this.productId = productId
        getContent.launch("image/*")
    }

    private fun uriToByteArray(uri: Uri, context: Context): ByteArray {
        return context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.readBytes()
        } ?: ByteArray(0)
    }
}
