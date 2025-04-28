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
        enableEdgeToEdge()
        setContentView(R.layout.activity_homebasescreen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_homebasescreen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Handle button clicks
        val viewBudgetsButton = findViewById<Button>(R.id.viewbudgetsscreenbutton)
        val addBudgetButton = findViewById<Button>(R.id.registerbutton)
        val addExpenseButton = findViewById<Button>(R.id.loginbutton)

        viewBudgetsButton.setOnClickListener {
            // Navigate to the view budgets screen
            val intent = Intent(this, BudgetViewScreen::class.java)
            startActivity(intent)
        }

        addBudgetButton.setOnClickListener {
            // Navigate to the add budget screen
            val intent = Intent(this, BudgetCreationscreen::class.java)
            startActivity(intent)
        }

        addExpenseButton.setOnClickListener {
            // Navigate to the add expense screen
            val intent = Intent(this, expenseincomescreen::class.java)
            startActivity(intent)
        }
    }
}
