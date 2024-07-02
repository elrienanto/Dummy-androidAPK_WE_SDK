package com.webengage.demo.shopping.view.productDetail

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.webengage.demo.shopping.R
import com.webengage.demo.shopping.view.home.Product
import com.webengage.sdk.android.WebEngage

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val product = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("product", Product::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("product") as Product
        }

        // Assuming the product category is also passed via intent
        val productCategory = intent.getStringExtra("category") ?: "Unknown Category"

        product?.let {
            populateDetails(it)
            trackProductViewedEvent(it, productCategory)
        }

        val addToCartButton = findViewById<Button>(R.id.addToCartButton)
        addToCartButton.setOnClickListener {
            trackAddToCartEvent(product)
            sendAddToCartResult(product)
        }
    }

    private fun populateDetails(product: Product) {
        val imageView = findViewById<ImageView>(R.id.productImageView)
        val productTitleTextView = findViewById<TextView>(R.id.titleTextView)
        val productPriceTextView = findViewById<TextView>(R.id.priceTextView)
        val imageResId = resources.getIdentifier(product.image, "drawable", packageName)
        imageView.setImageResource(imageResId)
        productTitleTextView.text = product.title
        productPriceTextView.text = product.price
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = product.title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun sendAddToCartResult(product: Product?) {
        val resultIntent = Intent()
        resultIntent.putExtra("added", true)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun trackProductViewedEvent(product: Product, category: String) {
        val attributes = mapOf(
            "product_image" to product.image,
            "product_title" to product.title,
            "product_price" to product.price,
            "product_category" to category
        )
        WebEngage.get().analytics().track("Product Viewed", attributes)
    }

    private fun trackAddToCartEvent(product: Product?) {
        product?.let {
            val attributes = mapOf(
                "product_image" to it.image,
                "product_title" to it.title,
                "product_price" to it.price
            )
            WebEngage.get().analytics().track("Added to Cart", attributes)
        }
    }
}
