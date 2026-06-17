package com.salu.project.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.salu.project.R
import com.salu.project.model.Product

class ProductAdapter(
    private var products: List<Product>,
    private val onItemClick: ((Product) -> Unit)? = null
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProductImage: ImageView = view.findViewById(R.id.ivProductImage)
        val tvProductName: TextView = view.findViewById(R.id.tvProductName)
        val tvProductDescription: TextView = view.findViewById(R.id.tvProductDescription)
        val tvProductPrice: TextView = view.findViewById(R.id.tvProductPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.tvProductName.text = product.name
        holder.tvProductDescription.text = product.description
        holder.tvProductPrice.text = "$${product.price}"

        Glide.with(holder.itemView.context)
            .load(product.image)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(holder.ivProductImage)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(product)
        }
    }

    override fun getItemCount() = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}