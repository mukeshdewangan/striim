package com.striim.expensemanager.parser;

import com.striim.expensemanager.driver.FileType;

import java.util.Properties;

public class InputSourceFactory {
    private InputSourceFactory(){}

    public static InputSourceBase createSource(FileType fileType, Properties properties) {
        switch (fileType){
        case XML:
            return new XmlSourceExpenseParser(properties);
        case JSON:
            return new JsonSourceExpenseParser(properties);
        default:
            throw new IllegalArgumentException("Invalid parser type");
        }
    }
}
