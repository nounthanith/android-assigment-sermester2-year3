package com.salu.project.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.salu.project.R
import com.salu.project.model.CartItem

class CartAdapter(
    private var items: List<CartItem>,
    private val onUpdate: (CartItem, Int) -> Unit,
    private val onRemove: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProductImage: ImageView = view.findViewById(R.id.ivCartProductImage)
        val tvProductName: TextView = view.findViewById(R.id.tvCartProductName)
        val tvProductPrice: TextView = view.findViewById(R.id.tvCartProductPrice)
        val tvQuantity: TextView = view.findViewById(R.id.tvQuantity)
        val btnDecrease: ImageButton = view.findViewById(R.id.btnDecrease)
        val btnIncrease: ImageButton = view.findViewById(R.id.btnIncrease)
        val btnRemove: ImageButton = view.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        holder.tvProductName.text = item.product.name
        holder.tvProductPrice.text = "$${item.product.price}"
        holder.tvQuantity.text = item.quantity.toString()

        Glide.with(holder.itemView.context)
            .load(item.product.image)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(holder.ivProductImage)

        holder.btnDecrease.setOnClickListener {
            if (item.quantity > 1) onUpdate(item, item.quantity - 1)
        }

        holder.btnIncrease.setOnClickListener {
            onUpdate(item, item.quantity + 1)
        }

        holder.btnRemove.setOnClickListener {
            onRemove(item)
        }
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}