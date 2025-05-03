package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.product.Product
import com.example.myapplication.product.ProductAdapter
import com.example.myapplication.product.ProductRepository
import com.example.myapplication.product.ShoppingCart
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var cartItemCount: TextView
    private lateinit var cartTotalPrice: TextView
    private lateinit var viewCartButton: Button
    private lateinit var clearCartButton: Button
    private val productRepository = ProductRepository.getInstance()
    private val shoppingCart = ShoppingCart()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViews()
        setupRecyclerView()
        updateCartSummary()
    }

    private fun setupViews() {
        productsRecyclerView = findViewById(R.id.products_recyclerview)
        cartItemCount = findViewById(R.id.cart_item_count)
        cartTotalPrice = findViewById(R.id.cart_total_price)
        viewCartButton = findViewById(R.id.view_cart_button)
        clearCartButton = findViewById(R.id.clear_cart_button)
        viewCartButton.setOnClickListener {
            val cartItems = shoppingCart.getItems()
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show()
            } else {
                val cartContents = StringBuilder("Cart Items:\n")
                cartItems.forEach { (product, quantity) ->
                    val format = NumberFormat.getCurrencyInstance(Locale.US)
                    cartContents.append("${product.name} x $quantity (${format.format(product.price * quantity)})\n")
                }
                Toast.makeText(this, cartContents.toString(), Toast.LENGTH_LONG).show()
            }
        }
        clearCartButton.setOnClickListener {
            shoppingCart.clearCart()
            updateCartSummary()
            Toast.makeText(this, "Cart cleared", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(productRepository.getProducts()) { product ->
            addToCart(product)
        }
        productsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productAdapter
        }
    }

    private fun addToCart(product: Product) {
        shoppingCart.addItem(product)
        updateCartSummary()
        Toast.makeText(this, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
    }

    private fun updateCartSummary() {
        val itemCount = shoppingCart.getItemCount()
        val totalPrice = shoppingCart.getTotalPrice()
        cartItemCount.text = "Items: $itemCount"
        val format = NumberFormat.getCurrencyInstance(Locale.US)
        cartTotalPrice.text = "Total: ${format.format(totalPrice)}"
    }
}