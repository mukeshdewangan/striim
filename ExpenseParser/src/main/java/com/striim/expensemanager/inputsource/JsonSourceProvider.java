package com.striim.expensemanager.inputsource;

import com.striim.expensemanager.parser.JsonSourceExpenseParser;

import java.util.Properties;

public class JsonSourceProvider implements InputSourceProvider{
    @Override
    public boolean supports(String type) {
        return type.equals("JSON");
    }

    @Override
    public InputSourceBase create(Properties props) {
        return new JsonSourceExpenseParser(props);
    }
}
