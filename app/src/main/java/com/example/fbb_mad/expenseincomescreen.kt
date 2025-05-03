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
        enableEdgeToEdge()
        setContentView(R.layout.activity_expenseincomescreen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_expenseincomescreen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val checkExpense = findViewById<CheckBox>(R.id.checkexpense)
        val checkIncome = findViewById<CheckBox>(R.id.checkexpense2)
        val amountInput = findViewById<EditText>(R.id.amountinput)
        val budgetInput = findViewById<EditText>(R.id.budgetinput)
        val submitButton = findViewById<Button>(R.id.submitButton)

        checkExpense.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) checkIncome.isChecked = false
        }
        checkIncome.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) checkExpense.isChecked = false
        }

        submitButton.setOnClickListener {
            val amountText = amountInput.text.toString().replace(',', '.').trim()
            val budgetName = budgetInput.text.toString().trim()

            if (amountText.isEmpty() || budgetName.isEmpty()) {
                showToast("Please fill in all fields.")
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                showToast("Please enter a valid positive number.")
                return@setOnClickListener
            }

            val isExpense = checkExpense.isChecked
            val isIncome = checkIncome.isChecked

            if (!isExpense && !isIncome) {
                showToast("Please select whether it's an expense or income.")
                return@setOnClickListener
            }

            val budgets = getBudgetsFromSharedPreferences()
            val selectedBudget = budgets.find { it.name == budgetName }

            if (selectedBudget == null) {
                showToast("Budget not found.")
                return@setOnClickListener
            }

            val updatedBudget = if (isExpense) {
                selectedBudget.copy(amountLeft = selectedBudget.amountLeft - amount)
            } else {
                selectedBudget.copy(amountLeft = selectedBudget.amountLeft + amount)
            }

            val updatedBudgets = budgets.map {
                if (it.name == budgetName) updatedBudget else it
            }
            saveBudgetsToSharedPreferences(updatedBudgets)

            showToast("${if (isExpense) "Expense" else "Income"} of $${"%.2f".format(amount)} added to $budgetName.")

            amountInput.text.clear()
            budgetInput.text.clear()
            checkExpense.isChecked = false
            checkIncome.isChecked = false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getBudgetsFromSharedPreferences(): List<Budget> {
        val sharedPreferences = getSharedPreferences("BudgetPreferences", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("budgets", "[]")
        return gson.fromJson(json, Array<Budget>::class.java).toList()
    }

    private fun saveBudgetsToSharedPreferences(budgets: List<Budget>) {
        val sharedPreferences = getSharedPreferences("BudgetPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(budgets)
        editor.putString("budgets", json)
        editor.apply()
    }
}

data class Budget(val name: String, val amountLeft: Double)
