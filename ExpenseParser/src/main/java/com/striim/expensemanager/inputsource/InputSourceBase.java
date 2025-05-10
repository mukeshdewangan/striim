package com.striim.expensemanager.inputsource;

import com.striim.expensemanager.expense.ExpenseEntry;

import java.util.Iterator;
import java.util.Properties;

public interface InputSourceBase {
    Iterator<ExpenseEntry> getExpenses(Properties properties);
}
