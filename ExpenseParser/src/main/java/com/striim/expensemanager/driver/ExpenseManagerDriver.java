package com.striim.expensemanager.driver;

import com.striim.expensemanager.currency.CurrencyCode;
import com.striim.expensemanager.currency.CurrencyProvider;
import com.striim.expensemanager.inputsource.InputSourceBase;
import com.striim.expensemanager.inputsource.InputSourceFactory;
import com.striim.expensemanager.inputsource.InputSourceProvider;
import com.striim.expensemanager.inputsource.XmlSourceProvider;
import com.striim.expensemanager.inputsource.JsonSourceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.striim.expensemanager.expense.Constants.*;

public class ExpenseManagerDriver {
    public static void main( String[] args ) {
        Properties props = new Properties();
        props.setProperty(EXPENSE_FILE_PATH, "src/main/resources/sample_expenses.xml");
        props.setProperty(FILETYPE, "XML");

        InputSourceBase inputSource = getInputSource(props);
        CurrencyCode targetCurrency = CurrencyCode.USD;
        CurrencyProvider currencyProvider = CurrencyProvider.getInstance();
        ExpenseCalculator expenseCalculator = new ExpenseCalculator(currencyProvider, inputSource);

        double totalExpense = expenseCalculator.calculateExpense(props, targetCurrency);
        System.out.println("Total Expense " + totalExpense + " " + targetCurrency);

        targetCurrency = CurrencyCode.INR;

        totalExpense = expenseCalculator.calculateExpense(props,targetCurrency );
        System.out.println("Total Expense " + totalExpense + " " + targetCurrency);
    }

    private static InputSourceBase getInputSource(Properties props){
        List<InputSourceProvider> providers = new ArrayList<>();
        providers.add(new XmlSourceProvider());
        providers.add(new JsonSourceProvider());  // Add more as needed

        InputSourceFactory factory = new InputSourceFactory(providers);
        InputSourceBase source = factory.createSource(props);
        return source;
    }
}
