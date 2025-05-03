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

    // Represents each budget with a name and amount left
    data class Budget(val name: String, val amountLeft: Double)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Enables edge-to-edge UI
        setContentView(R.layout.activity_budget_view_screen)

//        // Handling window insets to adjust for system bars on edge-to-edge UI devices
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_budget_view_screen)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())  // Get system bars insets
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)  // Apply padding to layout
//            insets  // Return insets to be applied
//        }

        // Retrieve budgets from SharedPreferences, sorted alphabetically by name
        val budgets = getBudgetsFromSharedPreferences().sortedBy { it.name.lowercase() }

        // Get reference to the LinearLayout where the budget data will be displayed
        val linearLayout = findViewById<LinearLayout>(R.id.BudgetData)

        // Check if the list of budgets is empty and display a message if true
        if (budgets.isEmpty()) {
            val noDataText = TextView(this)  // Create a new TextView for the "No budgets found" message
            noDataText.text = "No budgets found."  // Set the text of the message
            linearLayout.addView(noDataText)  // Add the message to the LinearLayout
        } else {
            // Loop through each budget and create a TextView to display the budget information
            for (budget in budgets) {
                val budgetText = TextView(this)  // Create a new TextView for each budget

                // Format the budget amount to two decimal places for proper display
                val formattedAmount = String.format("$%.2f", budget.amountLeft)

                // Set the text of the TextView to include the budget name and formatted amount
                budgetText.text = "${budget.name}: $formattedAmount"

                // Add the TextView to the LinearLayout to display it on screen
                linearLayout.addView(budgetText)
            }
        }
    }

    // Function to retrieve budgets from SharedPreferences with error handling
    private fun getBudgetsFromSharedPreferences(): List<Budget> {
        val sharedPreferences = getSharedPreferences("BudgetPreferences", Context.MODE_PRIVATE)  // Access SharedPreferences
        val gson = Gson()  // Initialize Gson for JSON parsing

        return try {
            // Retrieve the JSON string of saved budgets from SharedPreferences
            val json = sharedPreferences.getString("budgets", "[]")  // Default to empty array if no data is found

            // If json is null or empty, return an empty list
            if (json.isNullOrEmpty()) {
                emptyList()  // Return an empty list if no data is found
            } else {
                // Convert the JSON string into a list of Budget objects
                val type = object : TypeToken<List<Budget>>() {}.type
                gson.fromJson(json, type)  // Parse the JSON into a list of Budget objects
            }
        } catch (e: JsonSyntaxException) {
            // Handle invalid JSON format (corrupted data)
            showToast("Error parsing budgets data.")  // Show a Toast message in case of an error
            emptyList()  // Return an empty list if there's an error in parsing
        } catch (e: Exception) {
            // Catch any other exceptions (e.g., file I/O errors, etc.)
            showToast("An unexpected error occurred while loading budgets.")  // Show an error message
            emptyList()  // Return an empty list in case of any unexpected errors
        }
    }

    // Function to show toast messages (for error feedback)
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()  // Show a short duration toast with the message
    }

    // Function to store budgets in SharedPreferences
    fun saveBudgetsToSharedPreferences(budgets: List<Budget>) {
        val sharedPreferences = getSharedPreferences("BudgetPreferences", Context.MODE_PRIVATE)  // Access SharedPreferences
        val editor = sharedPreferences.edit()  // Start editing the SharedPreferences
        val gson = Gson()  // Initialize Gson for JSON parsing
        val json = gson.toJson(budgets)  // Convert the list of budgets into a JSON string
        editor.putString("budgets", json)  // Save the JSON string in SharedPreferences
        editor.apply()  // Apply the changes to SharedPreferences
    }
}
