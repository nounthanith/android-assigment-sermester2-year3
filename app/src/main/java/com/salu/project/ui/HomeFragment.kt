package com.salu.project.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.salu.project.R
import com.salu.project.api.RetrofitClient
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var rvProducts: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        
        rvProducts = view.findViewById(R.id.rvProducts)
        progressBar = view.findViewById(R.id.progressBar)
        
        adapter = ProductAdapter(emptyList()) { product ->
            val intent = Intent(requireContext(), ProductDetailActivity::class.java).apply {
                putExtra("PRODUCT_NAME", product.name)
                putExtra("PRODUCT_PRICE", product.price)
                putExtra("PRODUCT_DESCRIPTION", product.description)
                putExtra("PRODUCT_IMAGE", product.image)
                putExtra("CATEGORY_ID", product.category)
            }
            startActivity(intent)
        }
        rvProducts.adapter = adapter

        fetchProducts()

        return view
    }

    private fun fetchProducts() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val products = RetrofitClient.getProductApi(requireContext()).getProducts()
                adapter.updateProducts(products)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error fetching products: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
