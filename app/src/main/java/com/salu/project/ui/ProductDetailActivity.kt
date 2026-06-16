package com.salu.project.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.salu.project.R
import com.salu.project.api.RetrofitClient
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var ivProductImage: ImageView
    private lateinit var tvProductName: TextView
    private lateinit var tvProductPrice: TextView
    private lateinit var tvProductDescription: TextView
    private lateinit var rvRelatedProducts: RecyclerView
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        ivProductImage = findViewById(R.id.ivProductImageDetail)
        tvProductName = findViewById(R.id.tvProductNameDetail)
        tvProductPrice = findViewById(R.id.tvProductPriceDetail)
        tvProductDescription = findViewById(R.id.tvProductDescriptionDetail)
        rvRelatedProducts = findViewById(R.id.rvRelatedProducts)

        val productName = intent.getStringExtra("PRODUCT_NAME")
        val productPrice = intent.getDoubleExtra("PRODUCT_PRICE", 0.0)
        val productDescription = intent.getStringExtra("PRODUCT_DESCRIPTION")
        val productImage = intent.getStringExtra("PRODUCT_IMAGE")
        val categoryId = intent.getStringExtra("CATEGORY_ID")

        tvProductName.text = productName
        tvProductPrice.text = "$$productPrice"
        tvProductDescription.text = productDescription

        Glide.with(this)
            .load(productImage)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(ivProductImage)

        adapter = ProductAdapter(emptyList()) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra("PRODUCT_NAME", product.name)
                putExtra("PRODUCT_PRICE", product.price)
                putExtra("PRODUCT_DESCRIPTION", product.description)
                putExtra("PRODUCT_IMAGE", product.image)
                putExtra("CATEGORY_ID", product.category)
            }
            startActivity(intent)
        }
        rvRelatedProducts.adapter = adapter

        if (categoryId != null) {
            fetchRelatedProducts(categoryId)
        }
    }

    private fun fetchRelatedProducts(categoryId: String) {
        lifecycleScope.launch {
            try {
                val products = RetrofitClient.getProductApi(this@ProductDetailActivity)
                    .getProductsByCategory(categoryId)
                adapter.updateProducts(products)
            } catch (e: Exception) {
                Toast.makeText(this@ProductDetailActivity, "Error fetching related products: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
