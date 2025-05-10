package com.striim.expensemanager.driver;

import com.striim.expensemanager.currency.CurrencyCode;
import com.striim.expensemanager.currency.CurrencyProvider;
import java.util.Properties;

public class ExpenseManagerDriver {
    public static void main( String[] args )
    {
        Properties props = new Properties();
        props.setProperty("expenseFilePath", "src/main/resources/sample_expenses.xml");
        props.setProperty("fileType", "XML");

        CurrencyCode targetCurrency = CurrencyCode.USD;
        CurrencyProvider currencyProvider = CurrencyProvider.getInstance();
        ExpenseCalculator expenseCalculator = new ExpenseCalculator(currencyProvider);

        double totalExpense = expenseCalculator.calculateExpense(props, targetCurrency);
        System.out.println("Total Expense " + totalExpense + " " + targetCurrency);

        targetCurrency = CurrencyCode.INR;

        totalExpense = expenseCalculator.calculateExpense(props,targetCurrency );
        System.out.println("Total Expense " + totalExpense + " " + targetCurrency);
    }
}
