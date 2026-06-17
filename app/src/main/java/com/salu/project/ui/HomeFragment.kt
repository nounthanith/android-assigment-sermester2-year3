package com.salu.project.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.salu.project.R
import com.salu.project.adapter.CategoryAdapter
import com.salu.project.adapter.ProductAdapter
import com.salu.project.api.RetrofitClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var rvCategories: RecyclerView
    private lateinit var rvProducts: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private var searchJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        
        rvCategories = view.findViewById(R.id.rvCategories)
        rvProducts = view.findViewById(R.id.rvProducts)
        etSearch = view.findViewById(R.id.etSearch)
        progressBar = view.findViewById(R.id.progressBar)
        
        setupCategoryAdapter()
        setupProductAdapter()
        setupSearch()

        fetchData()

        return view
    }

    private fun setupCategoryAdapter() {
        categoryAdapter = CategoryAdapter(emptyList()) { category ->
            fetchProductsByCategory(category.id)
        }
        rvCategories.adapter = categoryAdapter
    }

    private fun setupProductAdapter() {
        productAdapter = ProductAdapter(emptyList()) { product ->
            val intent = Intent(requireContext(), ProductDetailActivity::class.java).apply {
                putExtra("PRODUCT_ID", product.id)
                putExtra("PRODUCT_NAME", product.name)
                putExtra("PRODUCT_PRICE", product.price)
                putExtra("PRODUCT_DESCRIPTION", product.description)
                putExtra("PRODUCT_IMAGE", product.image)
                putExtra("CATEGORY_ID", product.category)
            }
            startActivity(intent)
        }
        rvProducts.adapter = productAdapter
    }

    private fun setupSearch() {
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(etSearch.text.toString())
                true
            } else {
                false
            }
        }

        // Optional: Real-time search with debounce
        etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(500) // Wait 500ms after user stops typing
                    performSearch(s.toString())
                }
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun performSearch(query: String) {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val products = RetrofitClient.getProductApi(requireContext()).getProducts(query)
                productAdapter.updateProducts(products)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Search failed: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun fetchData() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val api = RetrofitClient.getProductApi(requireContext())
                val categories = api.getCategories()
                val products = api.getProducts()
                
                categoryAdapter.updateCategories(categories)
                productAdapter.updateProducts(products)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun fetchProductsByCategory(categoryId: String) {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val products = RetrofitClient.getProductApi(requireContext()).getProductsByCategory(categoryId)
                productAdapter.updateProducts(products)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
