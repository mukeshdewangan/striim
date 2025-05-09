package com.striim.expensemanager.driver;

import com.striim.expensemanager.currency.CurrencyCode;
import com.striim.expensemanager.currency.CurrencyProvider;
import com.striim.expensemanager.expense.ExpenseEntry;
import com.striim.expensemanager.parser.XmlExpenseParser;
import com.striim.expensemanager.validator.XMLValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ExpenseManagerDriver {
    public static void main( String[] args )
    {
        Properties props = new Properties();
        //props.setProperty("expenseFilePath", "src/main/resources/sample_expenses.xml");
        props.setProperty("expenseFilePath", "src/main/resources/invalid_expenses.xml");
        props.setProperty("xsdFilePath", "src/main/resources/expenses.xsd");

        CurrencyProvider currencyProvider = new CurrencyProvider();
        ExpenseCalculator expenseCalculator = new ExpenseCalculator(currencyProvider,
                CurrencyCode.INR, new XmlExpenseParser());

        double totalExpense = expenseCalculator.calculateTotalExpense(props);
        System.out.printf("Total Expense " + totalExpense + " INR");
    }
}
