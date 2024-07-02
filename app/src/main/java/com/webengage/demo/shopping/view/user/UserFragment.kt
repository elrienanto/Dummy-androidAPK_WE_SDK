package com.webengage.demo.shopping.view.user

import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.webengage.demo.shopping.R
import com.webengage.demo.shopping.SharedPrefsManager
import com.webengage.sdk.android.User
import com.webengage.sdk.android.WebEngage
//import com.webengage.sdk.android.actions.render.
import com.webengage.sdk.android.UserProfile

class UserFragment : Fragment() {

    private lateinit var weUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weUser = WebEngage.get().user()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        SharedPrefsManager.get(activity?.applicationContext!!)
        val storedUserName = SharedPrefsManager.getString(SharedPrefsManager.USERNAME, "")

        val usernameEditText = view.findViewById<TextInputEditText>(R.id.usernameEditText)
        val passwordEditText = view.findViewById<TextInputEditText>(R.id.passwordEditText)
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        val usernameTextView = view.findViewById<TextView>(R.id.usernameTextView)

        // Set password input type to textPassword to censor it
        passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        if (TextUtils.isEmpty(storedUserName)) {
            showLoginElements(view)
            usernameTextView.visibility = View.GONE
        } else {
            welcomeUser(storedUserName, usernameTextView)
            hideLoginElements(view)
        }

        loginButton.setOnClickListener {
            val userName = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (isValidCredentials(userName, password)) {
                hideLoginElements(view)
                welcomeUser(userName, usernameTextView)
//                updateUserAttributes()
                SharedPrefsManager.putString(SharedPrefsManager.USERNAME, userName)
                weUser.login(userName)
                setUserProfile()
            } else {
                Toast.makeText(
                    activity?.applicationContext,
                    R.string.error_invalid_username,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        logoutButton.setOnClickListener {
            SharedPrefsManager.putString(SharedPrefsManager.USERNAME, "")
            weUser.logout()
            showLoginElements(view)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val storedUserName = SharedPrefsManager.getString(SharedPrefsManager.USERNAME, "")
        val usernameTextView = view.findViewById<TextView>(R.id.usernameTextView)
        if (TextUtils.isEmpty(storedUserName)) {
            showLoginElements(view)
            usernameTextView.visibility = View.GONE
        } else {
            welcomeUser(storedUserName, usernameTextView)
            hideLoginElements(view)
        }
    }

    private fun isValidCredentials(username: String, password: String): Boolean {
        return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)
    }

    private fun welcomeUser(userName: String, usernameTextView: TextView) {
        usernameTextView.visibility = View.VISIBLE
        usernameTextView.text = getString(R.string.welcome_message, userName)
    }

    private fun showLoginElements(view: View) {
        val usernameEditText = view.findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        usernameEditText?.visibility = View.VISIBLE
        usernameEditText?.setText("")
        passwordEditText?.setText("")
        passwordEditText?.visibility = View.VISIBLE
        view.findViewById<LinearLayoutCompat>(R.id.ll_logIn)?.visibility = View.VISIBLE
        view.findViewById<LinearLayoutCompat>(R.id.ll_logout)?.visibility = View.GONE
    }

    private fun hideLoginElements(view: View) {
        val usernameEditText = view.findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        usernameEditText?.visibility = View.GONE
        passwordEditText?.visibility = View.GONE
        view.findViewById<LinearLayoutCompat>(R.id.ll_logIn)?.visibility = View.GONE
        view.findViewById<LinearLayoutCompat>(R.id.ll_logout)?.visibility = View.VISIBLE
    }

//    private fun updateUserAttributes() {
//        val latitude = 19.0822
//        val longitude = 72.8417
////        weUser.setAttribute("latitude", latitude)
////        weUser.setAttribute("longitude", longitude)
//    }

    private fun setUserProfile() {
        weUser.setUserProfile(
            UserProfile.Builder()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john@doe.com")
                .setBirthDate("1996-08-19")
                .setPhoneNumber("+628155256325")
//                .setGender(Gender.MALE)
                .setCompany("Alphabet Inc.")
                .setLocation(19.0822, 72.8417)
                .build()
        )
    }
}
