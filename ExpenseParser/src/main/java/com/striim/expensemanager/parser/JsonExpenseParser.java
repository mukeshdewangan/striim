package com.striim.expensemanager.parser;

import com.striim.expensemanager.expense.ExpenseEntry;

import java.util.Iterator;

public class JsonExpenseParser implements ExpenseParserBase {
    @Override
    public void printReport() {
        throw new UnsupportedOperationException("Currently not supported");
    }

    @Override
    public Iterator<ExpenseEntry> parse(String filePath) {
        throw new UnsupportedOperationException("Currently not supported");
    }
}
