package com.example.myapplication

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.myapplication.product.ProductAdapter
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ShoppingCartEspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun addProductToCart_updatesCartSummary() {
        // Check initial cart state is empty
        onView(withId(R.id.cart_item_count))
            .check(matches(withText("Items: 0")))
        onView(withId(R.id.cart_total_price))
            .check(matches(withText("Total: $0.00")))

        // Add the first product to cart
        onView(withId(R.id.products_recyclerview))
            .perform(RecyclerViewActions.actionOnItemAtPosition<ProductAdapter.ProductViewHolder>(0,
                clickOnViewWithId(R.id.add_to_cart_button)))

        // Verify cart is updated to show 1 item
        onView(withId(R.id.cart_item_count))
            .check(matches(withText("Items: 1")))
        
        // Add the second product to cart
        onView(withId(R.id.products_recyclerview))
            .perform(RecyclerViewActions.actionOnItemAtPosition<ProductAdapter.ProductViewHolder>(3,
                clickOnViewWithId(R.id.add_to_cart_button)))

        // Verify cart is updated to show 2 items
        onView(withId(R.id.cart_item_count))
            .check(matches(withText("Items: 2")))

        // Check that total price is updated (we don't check the exact amount because it depends on prices)
        onView(withId(R.id.cart_total_price))
            .check(matches(withText(containsString("Total: $210.00"))))

        // Click "View Cart" button
        onView(withId(R.id.view_cart_button))
            .perform(click())
        
        // Clear cart
        onView(withId(R.id.clear_cart_button))
            .perform(click())

        // Verify cart is empty again
        onView(withId(R.id.cart_item_count))
            .check(matches(withText("Items: 0")))
        onView(withId(R.id.cart_total_price))
            .check(matches(withText("Total: $0.00")))
    }
    
    /**
     * Custom ViewAction to click on a child view with specified ID.
     */
    private fun clickOnViewWithId(viewId: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return allOf(isDisplayed(), isAssignableFrom(View::class.java))
            }

            override fun getDescription(): String {
                return "Click on a child view with specified ID $viewId"
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(viewId)
                v?.performClick()
            }
        }
    }
}