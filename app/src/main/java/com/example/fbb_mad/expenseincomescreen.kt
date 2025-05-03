package com.example.fbb_mad

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson

class expenseincomescreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables edge-to-edge UI
        setContentView(R.layout.activity_expenseincomescreen)

//        // Set up window insets to adjust for system bars (e.g., status bar, navigation bar)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_expenseincomescreen)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        // Initialize UI elements
        val checkExpense = findViewById<CheckBox>(R.id.checkexpense)//expense
        val checkIncome = findViewById<CheckBox>(R.id.checkexpense2)//income
        val amountInput = findViewById<EditText>(R.id.amountinput)
        val budgetInput = findViewById<EditText>(R.id.budgetinput)
        val submitButton = findViewById<Button>(R.id.submitButton)

        // Ensure only one checkbox (expense or income) can be selected at a time
        checkExpense.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) checkIncome.isChecked = false
        }
        checkIncome.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) checkExpense.isChecked = false
        }

        // Handle the Submit button click
        submitButton.setOnClickListener {
            // Get input values
            val amountText = amountInput.text.toString().replace(',', '.').trim() // Format amount
            val budgetName = budgetInput.text.toString().trim()

            // Validation checks for amount and budget name
            if (amountText.isEmpty() || budgetName.isEmpty()) {
                showToast("Please fill in all fields.")
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                showToast("Please enter a valid positive number.")
                return@setOnClickListener
            }

            // Check if an expense or income is selected
            val isExpense = checkExpense.isChecked
            val isIncome = checkIncome.isChecked

            if (!isExpense && !isIncome) {
                showToast("Please select whether it's an expense or income.")
                return@setOnClickListener
            }

            // Retrieve the list of budgets and find the selected budget
            val budgets = getBudgetsFromSharedPreferences()
            val selectedBudget = budgets.find { it.name == budgetName }

            if (selectedBudget == null) {
                showToast("Budget not found.")
                return@setOnClickListener
            }

            // Update the selected budget with the expense or income
            val updatedBudget = if (isExpense) {
                selectedBudget.copy(amountLeft = selectedBudget.amountLeft - amount)
            } else {
                selectedBudget.copy(amountLeft = selectedBudget.amountLeft + amount)
            }

            // Update the budgets list with the modified budget
            val updatedBudgets = budgets.map {
                if (it.name == budgetName) updatedBudget else it
            }

            // Save the updated budgets list back to SharedPreferences
            saveBudgetsToSharedPreferences(updatedBudgets)

            // Show success message
            showToast("${if (isExpense) "Expense" else "Income"} of $${"%.2f".format(amount)} added to $budgetName.")

            // Clear inputs and reset checkboxes
            amountInput.text.clear()
            budgetInput.text.clear()
            checkExpense.isChecked = false
            checkIncome.isChecked = false
        }
    }

    // Utility function to show Toast messages
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Function to get saved budgets from SharedPreferences
    private fun getBudgetsFromSharedPreferences(): List<Budget> {
        val sharedPreferences = getSharedPreferences("BudgetPreferences", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("budgets", "[]")
        return gson.fromJson(json, Array<Budget>::class.java).toList()
    }

    // Function to save the updated budgets list to SharedPreferences
    private fun saveBudgetsToSharedPreferences(budgets: List<Budget>) {
        val sharedPreferences = getSharedPreferences("BudgetPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(budgets)
        editor.putString("budgets", json)
        editor.apply()
    }
}

// Data class representing a Budget
data class Budget(val name: String, val amountLeft: Double)
