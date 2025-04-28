package com.example.fbb_mad

import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

class BudgetViewScreen : AppCompatActivity() {

    // Sample Budget data class
    data class Budget(val name: String, val amountLeft: Double)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_budget_view_screen)

        // Handling window insets for edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_budget_view_screen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve budgets from SharedPreferences with error handling
        val budgets = getBudgetsFromSharedPreferences()

        // Get the LinearLayout to populate
        val linearLayout = findViewById<LinearLayout>(R.id.BudgetData)

        // If the budgets list is empty, display a message
        if (budgets.isEmpty()) {
            val noDataText = TextView(this)
            noDataText.text = "No budgets found."
            linearLayout.addView(noDataText)
        } else {
            // Loop through each budget and create TextViews
            for (budget in budgets) {
                // Create a new TextView for each budget
                val budgetText = TextView(this)
                budgetText.text = "${budget.name}: \$${budget.amountLeft}"

                // Add the created TextView to the LinearLayout
                linearLayout.addView(budgetText)
            }
        }
    }

    // Function to retrieve budgets from SharedPreferences with error handling
    private fun getBudgetsFromSharedPreferences(): List<Budget> {
        val sharedPreferences = getSharedPreferences("BudgetPreferences", Context.MODE_PRIVATE)
        val gson = Gson()

        return try {
            // Get the JSON string from SharedPreferences
            val json = sharedPreferences.getString("budgets", "[]")

            // Check if json is null or empty and handle gracefully
            if (json.isNullOrEmpty()) {
                emptyList()  // No budgets found, return empty list
            } else {
                // Try to convert the JSON string back into a list of Budget objects
                val type = object : TypeToken<List<Budget>>() {}.type
                gson.fromJson(json, type)
            }
        } catch (e: JsonSyntaxException) {
            // Handle invalid JSON format (corrupted data)
            showToast("Error parsing budgets data.")
            emptyList()  // Return an empty list if there's an error in parsing
        } catch (e: Exception) {
            // Catch any other exception (e.g., file I/O errors, etc.)
            showToast("An unexpected error occurred while loading budgets.")
            emptyList()  // Return an empty list if there's an unknown error
        }
    }

    // Function to show toast messages (for error feedback)
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Function to store budgets in SharedPreferences
    fun saveBudgetsToSharedPreferences(budgets: List<Budget>) {
        val sharedPreferences = getSharedPreferences("BudgetPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        // Convert the list of budgets to JSON
        val json = gson.toJson(budgets)
        // Save the JSON string in SharedPreferences
        editor.putString("budgets", json)
        editor.apply()
    }
}
