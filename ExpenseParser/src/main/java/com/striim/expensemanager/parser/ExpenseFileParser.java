package com.striim.expensemanager.parser;

import com.striim.expensemanager.expense.ExpenseEntry;

import java.util.Iterator;
import java.util.List;

public interface ExpenseFileParser {
    void printReport();
    Iterator<ExpenseEntry> parse(String filePath, String... schemaFile);
}
