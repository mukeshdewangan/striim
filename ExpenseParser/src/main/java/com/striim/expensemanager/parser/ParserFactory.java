package com.striim.expensemanager.parser;

public class ParserFactory {
    public static ExpenseParserBase getParser(String parserType) {
        if(parserType.equals("XML")) {
            return new XmlExpenseParser();
        } else if(parserType.equals("JSON")) {
            return new JsonExpenseParser();
        } else {
            throw new IllegalArgumentException("Invalid parser type");
        }
    }
}
