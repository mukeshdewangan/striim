package com.striim.expensemanager.parser;

import com.striim.expensemanager.expense.ExpenseEntry;

import java.util.Iterator;
import java.util.Properties;

public class JsonSourceExpenseParser implements InputSourceBase {
    JsonSourceExpenseParser(Properties properties){

    }

    @Override
    public Iterator<ExpenseEntry> getExpenses(Properties properties) {
        throw new UnsupportedOperationException("Currently not supported");
    }
}
