package com.striim.expensemanager.inputsource;

import java.util.Properties;

public interface InputSourceProvider {
    boolean supports(String type);
    InputSourceBase create(Properties prop);
}
