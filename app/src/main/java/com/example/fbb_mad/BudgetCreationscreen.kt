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

    data class Budget(val name: String, val amountLeft: Double)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_budget_creationscreen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_budget_creationscreen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val budgetNameInput = findViewById<EditText>(R.id.budgetinput)
        val budgetAmountInput = findViewById<EditText>(R.id.amountinput)
        val createButton = findViewById<Button>(R.id.createbudgetbutton)
        val deleteButton = findViewById<Button>(R.id.deleteBudgetButton)

        createButton.setOnClickListener {
            val budgetName = budgetNameInput.text.toString().trim()
            val budgetAmount = budgetAmountInput.text.toString().trim()

            if (budgetName.isEmpty() || budgetAmount.isEmpty()) {
                Toast.makeText(this, "Please enter both a budget name and amount!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Parse amount safely
            val amountDouble = budgetAmount.toDoubleOrNull()
            if (amountDouble == null || amountDouble < 0) {
                Toast.makeText(this, "Invalid or negative amount entered!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ensure the amount has exactly two decimal places
            val formattedAmount = String.format("%.2f", amountDouble)

            // Save the new budget
            val newBudget = Budget(budgetName, formattedAmount.toDouble())
            saveBudgetToSharedPreferences(newBudget)

            Toast.makeText(this, "Budget \"$budgetName\" with $formattedAmount created!", Toast.LENGTH_LONG).show()

            // Clear the input fields
            budgetNameInput.text.clear()
            budgetAmountInput.text.clear()
        }

        deleteButton.setOnClickListener {
            val budgetName = budgetNameInput.text.toString().trim()

            if (budgetName.isEmpty()) {
                Toast.makeText(this, "Please enter a budget name to delete.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val budgets = getBudgetsFromSharedPreferences()
            val selectedBudget = budgets.find { it.name == budgetName }

            if (selectedBudget == null) {
                Toast.makeText(this, "Budget not found.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show confirmation dialog for deleting the budget
            showDeleteConfirmationDialog(budgetName, budgets)
        }
    }

    private fun showDeleteConfirmationDialog(budgetName: String, budgets: List<Budget>) {
        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete the budget: $budgetName?")
            .setPositiveButton("Yes") { _, _ ->
                val updatedBudgets = budgets.filterNot { it.name == budgetName }
                saveBudgetsToSharedPreferences(updatedBudgets)
                Toast.makeText(this, "Budget '$budgetName' deleted successfully.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun getBudgetsFromSharedPreferences(): List<Budget> {
        val sharedPreferences = getSharedPreferences("BudgetPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("budgets", "[]")
        return gson.fromJson(json, Array<Budget>::class.java).toList()
    }

    private fun saveBudgetToSharedPreferences(budget: Budget) {
        val sharedPreferences = getSharedPreferences("BudgetPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        // Load existing budgets safely
        val existingJson = sharedPreferences.getString("budgets", "[]")
        val listType = object : TypeToken<MutableList<Budget>>() {}.type
        val existingBudgets: MutableList<Budget> = gson.fromJson(existingJson, listType) ?: mutableListOf()

        existingBudgets.add(budget)

        // Save the updated list back
        editor.putString("budgets", gson.toJson(existingBudgets))
        editor.apply()
    }

    private fun saveBudgetsToSharedPreferences(budgets: List<Budget>) {
        val sharedPreferences = getSharedPreferences("BudgetPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(budgets)
        editor.putString("budgets", json)
        editor.apply()
    }
}
