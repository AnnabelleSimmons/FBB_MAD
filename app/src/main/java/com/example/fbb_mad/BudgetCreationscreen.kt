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

        createButton.setOnClickListener {
            val budgetName = budgetNameInput.text.toString().trim()
            val budgetAmount = budgetAmountInput.text.toString().trim()

            if (budgetName.isEmpty() || budgetAmount.isEmpty()) {
                Toast.makeText(this, "Please enter both a budget name and amount!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Parse amount safely
            val amountDouble = budgetAmount.toDoubleOrNull()
            if (amountDouble == null) {
                Toast.makeText(this, "Invalid amount entered!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Format as currency
            val formattedAmount = String.format("$%.2f", amountDouble)
            val newBudget = Budget(budgetName, amountDouble)

            // Save the new budget
            saveBudgetToSharedPreferences(newBudget)

            Toast.makeText(this, "Budget \"$budgetName\" with $formattedAmount created!", Toast.LENGTH_LONG).show()

            // Clear the input fields
            budgetNameInput.text.clear()
            budgetAmountInput.text.clear()
        }
    }

    private fun saveBudgetToSharedPreferences(budget: Budget) {
        val sharedPreferences = getSharedPreferences("BudgetPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()

//        // MIGRATION: wipe bad old data if needed
//        val allPrefs = sharedPreferences.all
//        val rawBudgets = allPrefs["budgets"]
//        if (rawBudgets is Set<*>) {
//            // Old bad data found, nuke it
//            sharedPreferences.edit().remove("budgets").apply()
//        }

        // Load existing budgets safely
        val existingJson = sharedPreferences.getString("budgets", "[]")
        val listType = object : TypeToken<MutableList<Budget>>() {}.type
        val existingBudgets: MutableList<Budget> = gson.fromJson(existingJson, listType) ?: mutableListOf()

        existingBudgets.add(budget)

        // Save the updated list back
        editor.putString("budgets", gson.toJson(existingBudgets))
        editor.apply()
    }
}
