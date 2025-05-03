package com.example.fbb_mad

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BudgetCreationscreen : AppCompatActivity() {

    // Data class to represent a Budget with a name and amount left
    data class Budget(val name: String, val amountLeft: Double)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Enables edge-to-edge UI
        setContentView(R.layout.activity_budget_creationscreen)

//        // Handling window insets for edge-to-edge UI
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_budget_creationscreen)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())  // Get system bar insets
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)  // Apply padding for UI adjustments
//            insets  // Return the insets to be applied
//        }

        //inputs and buttons to create & delete budgets
        val budgetNameInput = findViewById<EditText>(R.id.budgetinput)
        val budgetAmountInput = findViewById<EditText>(R.id.amountinput)
        val createButton = findViewById<Button>(R.id.createbudgetbutton)
        val deleteButton = findViewById<Button>(R.id.deleteBudgetButton)

        // Set click listener for the create button
        createButton.setOnClickListener {
            // Get the entered values for budget name and amount
            val budgetName = budgetNameInput.text.toString().trim()
            val budgetAmount = budgetAmountInput.text.toString().trim()

            // Validate that both fields have values entered
            if (budgetName.isEmpty() || budgetAmount.isEmpty()) {
                Toast.makeText(this, "Please enter both a budget name and amount!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Safely parse the entered amount to a Double and validate it
            val amountDouble = budgetAmount.toDoubleOrNull()
            if (amountDouble == null || amountDouble < 0) {
                Toast.makeText(this, "Invalid or negative amount entered!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Format the amount to ensure it has exactly two decimal places
            val formattedAmount = String.format("%.2f", amountDouble)

            // Create the new budget and save it
            val newBudget = Budget(budgetName, formattedAmount.toDouble())
            saveBudgetToSharedPreferences(newBudget)

            // Show confirmation toast
            Toast.makeText(this, "Budget \"$budgetName\" with $formattedAmount created!", Toast.LENGTH_LONG).show()

            // Clear the input fields after saving the budget
            budgetNameInput.text.clear()
            budgetAmountInput.text.clear()
        }

        // Set click listener for the delete button
        deleteButton.setOnClickListener {
            val budgetName = budgetNameInput.text.toString().trim()  // Get the budget name to delete

            // Validate that the name field is not empty
            if (budgetName.isEmpty()) {
                Toast.makeText(this, "Please enter a budget name to delete.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Retrieve the list of existing budgets
            val budgets = getBudgetsFromSharedPreferences()
            val selectedBudget = budgets.find { it.name == budgetName }  // Search for the budget to delete

            // If the budget is not found, show a message
            if (selectedBudget == null) {
                Toast.makeText(this, "Budget not found.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show confirmation dialog before proceeding with deletion
            showDeleteConfirmationDialog(budgetName, budgets)
        }
    }

    // Function to show a confirmation dialog for deleting a budget
    private fun showDeleteConfirmationDialog(budgetName: String, budgets: List<Budget>) {
        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle("Confirm Deletion")  // Set the title of the dialog
            .setMessage("Are you sure you want to delete the budget: $budgetName?")  // Set the message
            .setPositiveButton("Yes") { _, _ ->  // On confirmation, delete the budget
                val updatedBudgets = budgets.filterNot { it.name == budgetName }  // Filter out the budget to delete
                saveBudgetsToSharedPreferences(updatedBudgets)  // Save the updated budget list
                Toast.makeText(this, "Budget '$budgetName' deleted successfully.", Toast.LENGTH_SHORT).show()  // Show success message
            }
            .setNegativeButton("No") { dialog, _ ->  // On cancel, dismiss the dialog
                dialog.dismiss()
            }
            .create()

        dialog.show()  // Show the dialog to the user
    }

    // Function to retrieve the list of budgets from SharedPreferences
    private fun getBudgetsFromSharedPreferences(): List<Budget> {
        val sharedPreferences = getSharedPreferences("BudgetPreferences", Context.MODE_PRIVATE)
        val gson = Gson()  // Initialize Gson for JSON parsing
        val json = sharedPreferences.getString("budgets", "[]")  // Retrieve the saved budgets as a JSON string
        return gson.fromJson(json, Array<Budget>::class.java).toList()  // Convert JSON to a list of Budget objects
    }

    // Function to save a single budget to SharedPreferences
    private fun saveBudgetToSharedPreferences(budget: Budget) {
        val sharedPreferences = getSharedPreferences("BudgetPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()  // Start editing SharedPreferences
        val gson = Gson()  // Initialize Gson for JSON serialization

        // Load existing budgets safely
        val existingJson = sharedPreferences.getString("budgets", "[]")  // Retrieve the existing budgets JSON string
        val listType = object : TypeToken<MutableList<Budget>>() {}.type  // Define the type for the list of Budget objects
        val existingBudgets: MutableList<Budget> = gson.fromJson(existingJson, listType) ?: mutableListOf()  // Parse the existing budgets or initialize an empty list

        existingBudgets.add(budget)  // Add the new budget to the list

        // Save the updated list of budgets back to SharedPreferences
        editor.putString("budgets", gson.toJson(existingBudgets))
        editor.apply()  // Apply changes
    }

    // Function to save the updated list of budgets to SharedPreferences
    private fun saveBudgetsToSharedPreferences(budgets: List<Budget>) {
        val sharedPreferences = getSharedPreferences("BudgetPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()  // Start editing SharedPreferences
        val gson = Gson()  // Initialize Gson for JSON serialization
        val json = gson.toJson(budgets)  // Convert the list of budgets to a JSON string
        editor.putString("budgets", json)  // Save the JSON string in SharedPreferences
        editor.apply()  // Apply the changes to SharedPreferences
    }
}
