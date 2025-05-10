package com.striim.expensemanager.parser;

import com.striim.expensemanager.driver.FileType;

public class ParserFactory {
    public static ExpenseParserBase getParser(FileType parserType) {
        switch (parserType){
        case XML:
            return new XmlExpenseParser();
        case JSON:
            return new JsonExpenseParser();
        default:
            throw new IllegalArgumentException("Invalid parser type");
        }
    }
}
