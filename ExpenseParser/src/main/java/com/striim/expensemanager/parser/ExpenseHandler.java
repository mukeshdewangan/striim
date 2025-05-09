package com.striim.expensemanager.parser;

import com.striim.expensemanager.currency.CurrencyCode;
import com.striim.expensemanager.date_util.DateTimeConverter;
import com.striim.expensemanager.expense.ExpenseEntry;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.striim.expensemanager.expense.Constants.*;
import static com.striim.expensemanager.expense.Constants.EXPENSE;

// Logic to process XML elements without loading everything
public class ExpenseHandler extends DefaultHandler {
    private StringBuilder content = new StringBuilder();
    public List<ExpenseEntry> expenses = new ArrayList<>();

    private String description, currency, date;
    private double amount;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        content.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        content.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case DESCRIPTION:
                description = content.toString().trim();
                break;
            case CURRENCYTYPE:
                currency = content.toString().trim();
                break;
            case AMOUNT:
                amount = Double.parseDouble(content.toString().trim());
                break;
            case DATE:
                date = content.toString().trim();
                break;
            case EXPENSE:
                CurrencyCode currencyC = CurrencyCode.valueOf(currency.toUpperCase());
                LocalDateTime dateTime = DateTimeConverter.convertToDateTime(date);
                ExpenseEntry entry = new ExpenseEntry(description, currencyC, amount, dateTime);
                //System.out.println(entry);
                expenses.add(entry);
                break;
        }
    }

    public List<ExpenseEntry> getExpenses() {
        return expenses;
    }
}
