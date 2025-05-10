package com.striim.expensemanager.parser;

import com.striim.expensemanager.expense.ExpenseEntry;

import java.util.Iterator;

public class JsonSourceExpenseParser implements InputSourceBase {
    @Override
    public Iterator<ExpenseEntry> getExpenses(String filePath) {
        throw new UnsupportedOperationException("Currently not supported");
    }
}
