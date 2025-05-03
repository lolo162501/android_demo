package com.example.myapplication.product

class ShoppingCart {
    private val items = mutableMapOf<Product, Int>()
    fun addItem(item: Product, quantity: Int = 1) {
        val currentQuantity = items[item] ?: 0
        items[item] = currentQuantity + quantity
    }
    fun removeItem(item: Product) {
        items.remove(item)
    }
    fun updateQuantity(item: Product, quantity: Int) {
        if (quantity <= 0) {
            removeItem(item)
        } else {
            items[item] = quantity
        }
    }
    fun getItems(): Map<Product, Int> {
        return items.toMap()
    }
    fun getItemCount(): Int {
        return items.values.sum()
    }
    fun getTotalPrice(): Int {
        return items.entries.sumOf { (product, quantity) -> 
            product.price * quantity 
        }
    }
    fun clearCart() {
        items.clear()
    }
}