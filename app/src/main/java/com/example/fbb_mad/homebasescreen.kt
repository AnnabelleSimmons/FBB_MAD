package com.example.fbb_mad

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button

class HomeBaseScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables edge-to-edge UI
        setContentView(R.layout.activity_homebasescreen)

        // Set up window insets to adjust for system bars
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_homebasescreen)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        // Initialize buttons
        val viewBudgetsButton = findViewById<Button>(R.id.viewbudgetsscreenbutton)
        val addBudgetButton = findViewById<Button>(R.id.registerbutton)
        val addExpenseButton = findViewById<Button>(R.id.loginbutton)

        // Navigate to the view budgets screen when the button is clicked
        viewBudgetsButton.setOnClickListener {
            val intent = Intent(this, BudgetViewScreen::class.java)
            startActivity(intent)
        }

        // Navigate to the add budget screen when the button is clicked
        addBudgetButton.setOnClickListener {
            val intent = Intent(this, BudgetCreationscreen::class.java)
            startActivity(intent)
        }

        // Navigate to the add expense screen when the button is clicked
        addExpenseButton.setOnClickListener {
            val intent = Intent(this, expenseincomescreen::class.java)
            startActivity(intent)
        }
    }
}
