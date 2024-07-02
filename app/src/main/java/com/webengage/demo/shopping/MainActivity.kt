package com.webengage.demo.shopping

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.webengage.demo.shopping.Constants.cartTAG
import com.webengage.demo.shopping.Constants.homeTAG
import com.webengage.demo.shopping.Constants.userTAG
import com.webengage.demo.shopping.view.cart.CartFragment
import com.webengage.demo.shopping.view.home.HomeProductsFragment
import com.webengage.demo.shopping.view.user.UserFragment



class MainActivity : AppCompatActivity(), FragmentListener {

    private val homeFragment = HomeProductsFragment()
    private val userFragment = UserFragment()
    private val cartFragment = CartFragment()
    private lateinit var bottomNavigationView: BottomNavigationView

    private val bottomNavigationSelectedListener =
        NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    loadFragment(homeTAG, "HomeScreen")
                }

                R.id.action_cart -> {
                    loadFragment(cartTAG, "CartScreen")
                }

                R.id.action_profile -> {
                    loadFragment(userTAG, "UserProfile")
                }
            }
            true
        }

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && windowInsetsController != null) {
            windowInsetsController.hide(WindowInsets.Type.statusBars())
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            actionBar?.hide()
        }
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener(bottomNavigationSelectedListener)
        loadFragment(homeTAG, "HomeScreen")
    }

    private fun loadFragment(fragmentTag: String, screenName: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        for (fragment in fragmentManager.fragments) {
            fragmentTransaction.hide(fragment)
        }
        val existingFragment = fragmentManager.findFragmentByTag(fragmentTag)

        if (existingFragment != null) {
            fragmentTransaction.show(existingFragment)
        } else {
            val newFragment = when (fragmentTag) {
                homeTAG -> homeFragment
                userTAG -> userFragment
                cartTAG -> cartFragment
                else -> throw IllegalArgumentException("Unknown tag: $fragmentTag")
            }
            fragmentTransaction.add(R.id.fragment_container, newFragment, fragmentTag)
        }

        fragmentTransaction.commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the selected item index in the BottomNavigationView
        outState.putInt("selectedItemId", bottomNavigationView.selectedItemId)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restore the selected item index in the BottomNavigationView
        val selectedItemId = savedInstanceState.getInt("selectedItemId")
        bottomNavigationView.selectedItemId = selectedItemId
    }

    override fun onFragmentAction(actionType: String) {
        if (actionType == (homeTAG)) {
            loadFragment(actionType, "HomeScreen")
        }
    }
}
