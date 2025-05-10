package com.striim.expensemanager.parser;

import com.striim.expensemanager.driver.FileType;

public class InputSourceFactory {
    public static InputSourceBase createSource(FileType fileType) {
        switch (fileType){
        case XML:
            return new XmlSourceExpenseParser();
        case JSON:
            return new JsonSourceExpenseParser();
        default:
            throw new IllegalArgumentException("Invalid parser type");
        }
    }
}
