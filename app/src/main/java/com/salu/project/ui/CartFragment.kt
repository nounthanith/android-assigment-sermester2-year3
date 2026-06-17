package com.salu.project.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.salu.project.R
import com.salu.project.adapter.CartAdapter
import com.salu.project.api.RetrofitClient
import com.salu.project.model.UpdateQuantityRequest
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private lateinit var rvCartItems: RecyclerView
    private lateinit var tvTotalPrice: TextView
    private lateinit var btnClearCart: Button
    private lateinit var btnCheckout: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: CartAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        rvCartItems = view.findViewById(R.id.rvCartItems)
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice)
        btnClearCart = view.findViewById(R.id.btnClearCart)
        btnCheckout = view.findViewById(R.id.btnCheckout)
        progressBar = view.findViewById(R.id.cartProgressBar)

        setupAdapter()
        fetchCart()

        btnClearCart.setOnClickListener { clearCart() }
        btnCheckout.setOnClickListener {
            Toast.makeText(requireContext(), "Order Placed Successfully!", Toast.LENGTH_SHORT).show()
            clearCart()
        }

        return view
    }

    private fun setupAdapter() {
        adapter = CartAdapter(
            emptyList(),
            onUpdate = { item, newQty -> updateQuantity(item.product.id, newQty) },
            onRemove = { item -> removeItem(item.product.id) }
        )
        rvCartItems.adapter = adapter
    }

    private fun fetchCart() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.getCartApi(requireContext()).getCart()
                adapter.updateItems(response.items)
                tvTotalPrice.text = "$${String.format("%.2f", response.totalPrice)}"
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to load cart: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun updateQuantity(productId: String, quantity: Int) {
        lifecycleScope.launch {
            try {
                val request = UpdateQuantityRequest(quantity)
                val response = RetrofitClient.getCartApi(requireContext())
                    .updateQuantity(productId, request)
                adapter.updateItems(response.items)
                tvTotalPrice.text = "$${String.format("%.2f", response.totalPrice)}"
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removeItem(productId: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.getCartApi(requireContext()).removeItem(productId)
                adapter.updateItems(response.items)
                tvTotalPrice.text = "$${String.format("%.2f", response.totalPrice)}"
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Remove failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearCart() {
        lifecycleScope.launch {
            try {
                RetrofitClient.getCartApi(requireContext()).clearCart()
                adapter.updateItems(emptyList())
                tvTotalPrice.text = "$0.00"
                Toast.makeText(requireContext(), "Cart Cleared", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Clear failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
