package com.example.myapplication.cart


class ShoppingCartRepository {
    private val cart = mutableMapOf<String, CartItem>()
    /** 新增商品：如果已存在則增加數量，否則新增一筆。*/
    fun addProduct(product: Product) {
        val item = cart[product.id]
        if (item != null) {
            cart[product.id] = item.copy(quantity = item.quantity + 1)
        } else {
            cart[product.id] = CartItem(product, 1)
        }
    }
    /** 移除商品：數量減 1，如果數量為 0 則刪除項目。*/
    fun removeProduct(productId: String) {
        val item = cart[productId]
        if (item != null) {
            if (item.quantity > 1) {
                cart[productId] = item.copy(quantity = item.quantity - 1)
            } else {
                cart.remove(productId)
            }
        }
    }
    /** 清空購物車 */
    fun clearCart() {
        cart.clear()
    }
    /** 取得目前所有購物車項目 */
    fun getCurrentCartItems(): Map<String, CartItem> = cart.toMap()
    /** 取得排序後的購物車清單（依商品名稱排序） */
    fun getCartItems(): List<CartItem> = cart.values.toList().sortedBy { it.product.name }
    /** 計算購物車的總金額 */
    fun getTotalPrice(): Int = cart.values.sumOf { it.product.price * it.quantity }
}