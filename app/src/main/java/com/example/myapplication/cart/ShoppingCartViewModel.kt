package com.example.myapplication.cart

class ShoppingCartViewModel(private val repository: ShoppingCartRepository) {
    fun addProductToCart(product: Product) = repository.addProduct(product)
    fun removeProductFromCart(productId: String) = repository.removeProduct(productId)
    fun clearCart() = repository.clearCart()
    fun getCartItems(): List<CartItem> = repository.getCartItems()
    fun getTotalPrice(): Int = repository.getTotalPrice()
}