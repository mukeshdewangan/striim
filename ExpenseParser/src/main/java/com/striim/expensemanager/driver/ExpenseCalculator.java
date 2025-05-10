package com.striim.expensemanager.driver;

import com.striim.expensemanager.currency.CurrencyCode;
import com.striim.expensemanager.currency.CurrencyProvider;
import com.striim.expensemanager.expense.ExpenseEntry;
import com.striim.expensemanager.inputsource.InputSourceBase;

import java.util.Iterator;
import java.util.Properties;

public class ExpenseCalculator {
    private final CurrencyProvider currencyProvider;
    InputSourceBase inputSource;

    ExpenseCalculator(CurrencyProvider currencyProvider, InputSourceBase inputSource){
        this.currencyProvider = currencyProvider;
        this.inputSource = inputSource;
    }

    public double calculateExpense(Properties properties, CurrencyCode targetCurrency) {
        Iterator<ExpenseEntry> expensesIter = inputSource.getExpenses(properties);

        double totalExpense = 0;
        while(expensesIter.hasNext()){
            totalExpense += convertExpenseToTargetCurrency(expensesIter.next(), targetCurrency);
        }

        return totalExpense;
    }

    private double convertExpenseToTargetCurrency(ExpenseEntry expenseEntry, CurrencyCode targetCurrency) {
        double conversionRate = currencyProvider.getCurrencyValue(expenseEntry.getCurrency(), targetCurrency, expenseEntry.getDate());
        return expenseEntry.getAmount()*(conversionRate);
    }
}
