package com.striim.expensemanager.inputsource;

import com.striim.expensemanager.driver.FileType;
import com.striim.expensemanager.expense.Constants;

import java.util.List;
import java.util.Properties;

public class InputSourceFactory {
    private List<InputSourceProvider> providers;

    public InputSourceFactory(List<InputSourceProvider> providers) {
        this.providers = providers;
    }

    public InputSourceBase createSource(Properties prop) {
        String type = prop.getProperty(Constants.FILETYPE);
        for (InputSourceProvider provider : providers) {
            if (provider.supports(type)) {
                return provider.create(prop);
            }
        }
        throw new IllegalArgumentException("Unsupported source type: " + type);
    }


//    public static InputSourceBase createSource(FileType fileType, Properties properties) {
//        switch (fileType){
//        case XML:
//            return new XmlSourceExpenseParser(properties);
//        case JSON:
//            return new JsonSourceExpenseParser(properties);
//        default:
//            throw new IllegalArgumentException("Invalid parser type");
//        }
//    }
}
