package com.striim.expensemanager.driver;

import com.striim.expensemanager.currency.CurrencyCode;
import com.striim.expensemanager.currency.CurrencyProvider;
import com.striim.expensemanager.expense.ExpenseEntry;
import com.striim.expensemanager.parser.ParserFactory;
import com.striim.expensemanager.driver.FileType;
import com.striim.expensemanager.parser.XmlExpenseParser;
import com.striim.expensemanager.validator.XMLValidator;

import java.util.ArrayList;
import java.util.List;
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

        double totalExpense = expenseCalculator.calculateTotalExpense(props, targetCurrency);
        System.out.println("Total Expense " + totalExpense + " " + targetCurrency);

        targetCurrency = CurrencyCode.INR;

        totalExpense = expenseCalculator.calculateTotalExpense(props,targetCurrency );
        System.out.println("Total Expense " + totalExpense + " " + targetCurrency);
    }
}
