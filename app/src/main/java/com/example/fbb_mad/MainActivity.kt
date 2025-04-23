package com.example.fbb_mad

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Reference the root layout by the correct ID (activity_main)
        val rootView = findViewById<android.view.View>(R.id.activity_main)

        // Set onApplyWindowInsetsListener to adjust padding based on system bars
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            // Apply padding based on system bars insets
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)

            // Return the insets
            insets
        }

        // Set onClickListener for the login button
        val loginButton = findViewById<Button>(R.id.loginbutton)
        loginButton.setOnClickListener {
            navigateToLoginScreen()
        }

        val registerButton = findViewById<Button>(R.id.registerbutton)
        registerButton.setOnClickListener {
            navigateToRegisterScreen()
        }
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
    }

    private fun navigateToRegisterScreen()
    {
        val intent = Intent(this, registerscreen::class.java)
        startActivity(intent)
    }
}
