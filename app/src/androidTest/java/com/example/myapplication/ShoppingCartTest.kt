package com.example.myapplication

import com.example.myapplication.cart.Product
import com.example.myapplication.cart.ShoppingCartRepository
import com.example.myapplication.cart.ShoppingCartViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ShoppingCartTest {
    private lateinit var repository: ShoppingCartRepository
    private lateinit var viewModel: ShoppingCartViewModel

    // 測試用商品資料
    private val apple = Product("p1", "Apple", 30)
    private val banana = Product("p2", "Banana", 20)
    private val orange = Product("p3", "Orange", 25)

    /**
     * 在每個測試之前執行，初始化 Repository 與 ViewModel，並清空購物車。
     */
    @Before
    fun setup() {
        repository = ShoppingCartRepository()
        viewModel = ShoppingCartViewModel(repository)
        repository.clearCart()
    }

    /**
     * 測試：新增商品後，ViewModel 的購物車狀態應正確更新。
     */
    @Test
    fun `新增單一商品`() {
        viewModel.addProductToCart(apple)
        val cartItems = viewModel.getCartItems()
        val totalPrice = viewModel.getTotalPrice()

        assertEquals("購物車應有 1 個項目", 1, cartItems.size)
        assertEquals("項目應為蘋果", apple.id, cartItems[0].product.id)
        assertEquals("蘋果數量應為 1", 1, cartItems[0].quantity)
        assertEquals("總金額應為蘋果價格", apple.price, totalPrice)

        val repoState = repository.getCurrentCartItems()
        assertTrue("Repository 應包含蘋果", repoState.containsKey(apple.id))
        assertEquals("Repository 蘋果數量應為 1", 1, repoState[apple.id]?.quantity)
    }

    /**
     * 測試：多次新增相同商品，數量應累加。
     */
    @Test
    fun `新增相同商品`() {
        viewModel.addProductToCart(apple)
        viewModel.addProductToCart(apple)
        viewModel.addProductToCart(apple)

        val cartItems = viewModel.getCartItems()
        val totalPrice = viewModel.getTotalPrice()

        assertEquals("購物車仍應只有 1 種項目", 1, cartItems.size)
        assertEquals("項目應為蘋果", apple.id, cartItems[0].product.id)
        assertEquals("蘋果數量應為 3", 3, cartItems[0].quantity)
        assertEquals("總金額應為 3 顆蘋果價格", apple.price * 3, totalPrice)
    }

    /**
     * 測試：新增不同商品，總金額應正確計算。
     */
    @Test
    fun `新增不同商品`() {
        viewModel.addProductToCart(apple)  // 1.50
        viewModel.addProductToCart(banana) // 0.75
        viewModel.addProductToCart(orange) // 1.25

        val cartItems = viewModel.getCartItems()
        val totalPrice = viewModel.getTotalPrice()

        val expectedTotalPrice = apple.price + banana.price + orange.price
        val expectedSize = 3

        assertEquals("購物車應有 $expectedSize 個不同的項目", expectedSize, cartItems.size)

        // 驗證順序與數量
        assertEquals("第 1 項應為蘋果", apple.id, cartItems[0].product.id)
        assertEquals("蘋果數量應為 1", 1, cartItems[0].quantity)
        assertEquals("第 2 項應為香蕉", banana.id, cartItems[1].product.id)
        assertEquals("香蕉數量應為 1", 1, cartItems[1].quantity)
        assertEquals("第 3 項應為橘子", orange.id, cartItems[2].product.id)
        assertEquals("橘子數量應為 1", 1, cartItems[2].quantity)

        assertEquals("總金額應為所有商品價格加總", expectedTotalPrice, totalPrice)
    }

    /**
     * 測試：移除商品會減少數量並更新金額。
     */
    @Test
    fun `移除商品`() {
        viewModel.addProductToCart(apple)
        viewModel.addProductToCart(apple)
        viewModel.addProductToCart(banana)

        viewModel.removeProductFromCart(apple.id)

        val cartItems = viewModel.getCartItems()
        val totalPrice = viewModel.getTotalPrice()
        val expectedTotalPrice = apple.price + banana.price

        assertEquals("購物車應剩 2 個項目 (1蘋果, 1香蕉)", 2, cartItems.size)

        val appleItem = cartItems.find { it.product.id == apple.id }
        assertNotNull("應還能找到蘋果", appleItem)
        assertEquals("蘋果數量應剩 1", 1, appleItem?.quantity)
        assertEquals("總金額應更新", expectedTotalPrice, totalPrice)
    }

    /**
     * 測試：清空購物車會移除所有項目並歸零總金額。
     */
    @Test
    fun `清空購物車`() {
        viewModel.addProductToCart(apple)
        viewModel.addProductToCart(banana)
        viewModel.addProductToCart(orange)

        viewModel.clearCart()

        val cartItems = viewModel.getCartItems()
        val totalPrice = viewModel.getTotalPrice()

        assertTrue("購物車列表應為空", cartItems.isEmpty())
        assertEquals("總金額應為 0", 0, totalPrice)
        assertTrue("Repository 應為空", repository.getCurrentCartItems().isEmpty())
    }
}
