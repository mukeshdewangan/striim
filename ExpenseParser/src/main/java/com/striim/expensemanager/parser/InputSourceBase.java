package com.striim.expensemanager.parser;

import com.striim.expensemanager.expense.ExpenseEntry;

import java.util.Iterator;

public interface InputSourceBase {
    Iterator<ExpenseEntry> getExpenses(String filePath);
}
