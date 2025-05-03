package com.example.myapplication.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private var products: List<Product>,
    private val onAddToCartClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.product_name)
        val priceTextView: TextView = view.findViewById(R.id.product_price)
        val descriptionTextView: TextView = view.findViewById(R.id.product_description)
        val addToCartButton: Button = view.findViewById(R.id.add_to_cart_button)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.nameTextView.text = product.name
        
        // Format price with currency symbol
        val format = NumberFormat.getCurrencyInstance(Locale.US)
        holder.priceTextView.text = format.format(product.price)
        
        holder.descriptionTextView.text = product.description
        
        holder.addToCartButton.setOnClickListener {
            onAddToCartClick(product)
        }
    }
    override fun getItemCount() = products.size
}