package com.striim.expensemanager.driver;

import com.striim.expensemanager.currency.CurrencyCode;
import com.striim.expensemanager.currency.CurrencyProvider;
import com.striim.expensemanager.expense.ExpenseEntry;
import com.striim.expensemanager.parser.XmlExpenseParser;
import com.striim.expensemanager.validator.XMLValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class ExpenseManagerDriver {
    public static void main( String[] args )
    {
        Properties props = new Properties();
        props.setProperty("expenseFilePath", "src/main/resources/sample_expenses.xml");
        props.setProperty("xsdFilePath", "src/main/resources/expenses.xsd");

        CurrencyProvider currencyProvider = new CurrencyProvider();
        ExpenseCalculator expenseCalculator = new ExpenseCalculator(currencyProvider,
                CurrencyCode.INR, new XmlExpenseParser(new XMLValidator()));
        //List<ExpenseEntry> expenseEntries = expenseCalculator.calculateExpense(props);

        double totalExpense = expenseCalculator.calculateTotalExpense(props);
        System.out.printf("Total Expense " + totalExpense);

    }

    public static List<ExpenseEntry> dummyExpenseEntry(){
        List<ExpenseEntry> expenses = new ArrayList<>();
//
//        expenses.add(new ExpenseEntry("Coffee In Airport", CurrencyCode.USD, 20.00, "August 15, 2020"));
//        expenses.add(new ExpenseEntry("Boating", CurrencyCode.EUR, 150.00, "September 25, 2020"));
//        expenses.add(new ExpenseEntry("Taxi", CurrencyCode.INR, 200.00, "October 1, 2020"));
//        expenses.add(new ExpenseEntry("Lunch", CurrencyCode.USD, 35.50, "October 10, 2020"));
//        expenses.add(new ExpenseEntry("Museum Ticket", CurrencyCode.EUR, 12.00, "October 11, 2020"));

        return expenses;
    }
}
