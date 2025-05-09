package com.striim.expensemanager.driver;

import com.striim.expensemanager.currency.CurrencyCode;
import com.striim.expensemanager.currency.CurrencyProvider;
import com.striim.expensemanager.expense.ExpenseEntry;
import com.striim.expensemanager.parser.ExpenseFileParser;

import java.util.List;
import java.util.Properties;

public class ExpenseCalculator {
    CurrencyProvider currencyProvider;
    ExpenseFileParser parser;
    CurrencyCode targetCurrency;

    ExpenseCalculator(CurrencyProvider currencyProvider, CurrencyCode targetCurrency, ExpenseFileParser parser){
        this.currencyProvider = currencyProvider;
        this.targetCurrency = targetCurrency;
        this.parser = parser;
    }

    public double calculateTotalExpense(Properties properties) {
        List<ExpenseEntry> expenses = calculateExpense(properties);

        double totalExpense = 0;
        for (ExpenseEntry expenseEntry : expenses) {
            totalExpense += convertExpenseToTargetCurrency(expenseEntry, targetCurrency);
        }
        return totalExpense;
    }

    //later make it private
    private List<ExpenseEntry> calculateExpense(Properties properties) {
        // parse XML file
        String expenseFile = properties.getProperty("expenseFilePath");
        String xsdFile = properties.getProperty("xsdFilePath");
        return parser.parse(expenseFile,xsdFile);
    }

    private double convertExpenseToTargetCurrency(ExpenseEntry expenseEntry, CurrencyCode targetCurrency) {
        double conversionRate = currencyProvider.getCurrencyValue(expenseEntry.getCurrency(), targetCurrency, expenseEntry.getDate());
        return expenseEntry.getAmount()*(conversionRate);
    }
}
