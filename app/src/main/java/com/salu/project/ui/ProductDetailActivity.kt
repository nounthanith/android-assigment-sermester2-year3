package com.salu.project.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.salu.project.R
import com.salu.project.adapter.ProductAdapter
import com.salu.project.api.RetrofitClient
import com.salu.project.api.SessionManager
import com.salu.project.model.AddToCartRequest
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var ivProductImage: ImageView
    private lateinit var tvProductName: TextView
    private lateinit var tvProductPrice: TextView
    private lateinit var tvProductDescription: TextView
    private lateinit var rvRelatedProducts: RecyclerView
    private lateinit var btnAddToCart: Button
    private lateinit var btnDecrease: ImageButton
    private lateinit var btnIncrease: ImageButton
    private lateinit var tvQuantity: TextView
    private lateinit var adapter: ProductAdapter
    
    private var quantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        ivProductImage = findViewById(R.id.ivProductImageDetail)
        tvProductName = findViewById(R.id.tvProductNameDetail)
        tvProductPrice = findViewById(R.id.tvProductPriceDetail)
        tvProductDescription = findViewById(R.id.tvProductDescriptionDetail)
        rvRelatedProducts = findViewById(R.id.rvRelatedProducts)
        btnAddToCart = findViewById(R.id.btnAddToCart)
        btnDecrease = findViewById(R.id.btnDecreaseDetail)
        btnIncrease = findViewById(R.id.btnIncreaseDetail)
        tvQuantity = findViewById(R.id.tvQuantityDetail)

        val productId = intent.getStringExtra("PRODUCT_ID")
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
            .error(android.R.drawable.stat_notify_error)
            .centerCrop()
            .into(ivProductImage)

        adapter = ProductAdapter(emptyList()) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra("PRODUCT_ID", product.id)
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

        btnIncrease.setOnClickListener {
            quantity++
            tvQuantity.text = quantity.toString()
        }

        btnDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                tvQuantity.text = quantity.toString()
            }
        }

        btnAddToCart.setOnClickListener {
            if (productId != null) {
                addToCart(productId)
            }
        }
    }

    private fun addToCart(productId: String) {
        val sessionManager = SessionManager(this)
        
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Please login to add items to cart", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            return
        }

        val productPrice = intent.getDoubleExtra("PRODUCT_PRICE", 0.0)
        
        btnAddToCart.isEnabled = false
        btnAddToCart.text = "Adding..."

        lifecycleScope.launch {
            try {
                val request = AddToCartRequest(
                    productId = productId,
                    price = productPrice,
                    quantity = quantity
                )
                RetrofitClient.getCartApi(this@ProductDetailActivity).addToCart(request)
                Toast.makeText(this@ProductDetailActivity, "Added to Cart!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@ProductDetailActivity, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                btnAddToCart.isEnabled = true
                btnAddToCart.text = "Add to Cart"
            }
        }
    }

    private fun fetchRelatedProducts(categoryId: String) {
        lifecycleScope.launch {
            try {
                val products = RetrofitClient.getProductApi(this@ProductDetailActivity)
                    .getProductsByCategory(categoryId)
                adapter.updateProducts(products)
            } catch (e: Exception) {
                Toast.makeText(this@ProductDetailActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
