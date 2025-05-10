package com.striim.expensemanager.driver;

import com.striim.expensemanager.currency.CurrencyCode;
import com.striim.expensemanager.currency.CurrencyProvider;
import com.striim.expensemanager.expense.ExpenseEntry;
import com.striim.expensemanager.parser.ExpenseParserBase;

import java.util.Iterator;
import java.util.Properties;

public class ExpenseCalculator {
    CurrencyProvider currencyProvider;
    ExpenseParserBase parser;
    CurrencyCode targetCurrency;

    ExpenseCalculator(CurrencyProvider currencyProvider, CurrencyCode targetCurrency, ExpenseParserBase parser){
        this.currencyProvider = currencyProvider;
        this.targetCurrency = targetCurrency;
        this.parser = parser;
    }

    public void setTargetCurrency(CurrencyCode target){
        this.targetCurrency = target;
    }

    public double calculateTotalExpense(Properties properties) {
        Iterator<ExpenseEntry> expensesIter = calculateExpense(properties);

        double totalExpense = 0;
        while(expensesIter.hasNext()){
            //totalExpense += convertExpenseToTargetCurrency(expenseEntry, targetCurrency);
            totalExpense += convertExpenseToTargetCurrency(expensesIter.next(), targetCurrency);
        }

//        while (true) {
//            ExpenseEntry entry = queue.take();
//            if (entry == POISON_PILL) break;
//            process(entry);
//        }

        return totalExpense;
    }

    private Iterator<ExpenseEntry> calculateExpense(Properties properties) {
        String expenseFile = properties.getProperty("expenseFilePath");
        return parser.parse(expenseFile);
    }

    private double convertExpenseToTargetCurrency(ExpenseEntry expenseEntry, CurrencyCode targetCurrency) {
        double conversionRate = currencyProvider.getCurrencyValue(expenseEntry.getCurrency(), targetCurrency, expenseEntry.getDate());
        return expenseEntry.getAmount()*(conversionRate);
    }
}
