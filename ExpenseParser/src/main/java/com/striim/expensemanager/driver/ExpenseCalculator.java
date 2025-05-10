package com.striim.expensemanager.driver;

import com.striim.expensemanager.currency.CurrencyCode;
import com.striim.expensemanager.currency.CurrencyProvider;
import com.striim.expensemanager.expense.ExpenseEntry;
import com.striim.expensemanager.parser.InputSourceBase;
import com.striim.expensemanager.parser.InputSourceFactory;

import java.util.Iterator;
import java.util.Properties;

import static com.striim.expensemanager.expense.Constants.EXPENSE_FILE_PATH;
import static com.striim.expensemanager.expense.Constants.FILETYPE;

public class ExpenseCalculator {
    private final CurrencyProvider currencyProvider;
    InputSourceBase inputSource;

    ExpenseCalculator(CurrencyProvider currencyProvider){
        this.currencyProvider = currencyProvider;
    }

    public double calculateExpense(Properties properties, CurrencyCode targetCurrency) {
        this.inputSource = InputSourceFactory.createSource(FileType.valueOf(properties.getProperty(FILETYPE)), properties);

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
