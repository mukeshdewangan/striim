package com.striim.expensemanager.parser;

import com.striim.expensemanager.expense.ExpenseEntry;

import java.util.Iterator;

public interface ExpenseParserBase {
    void printReport();
    Iterator<ExpenseEntry> parse(String filePath);
}
