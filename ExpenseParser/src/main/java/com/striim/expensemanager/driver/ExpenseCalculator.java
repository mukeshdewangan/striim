package com.striim.expensemanager.driver;

import com.striim.expensemanager.currency.CurrencyCode;
import com.striim.expensemanager.currency.CurrencyProvider;
import com.striim.expensemanager.expense.ExpenseEntry;
import com.striim.expensemanager.parser.ExpenseParserBase;
import com.striim.expensemanager.parser.ParserFactory;

import java.util.Iterator;
import java.util.Properties;

public class ExpenseCalculator {
    private final CurrencyProvider currencyProvider;
    ExpenseParserBase parser;

    ExpenseCalculator(CurrencyProvider currencyProvider){
        this.currencyProvider = currencyProvider;
    }

    public double calculateTotalExpense(Properties properties, CurrencyCode targetCurrency) {
        this.parser = ParserFactory.getParser(FileType.valueOf(properties.getProperty("fileType")));

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
