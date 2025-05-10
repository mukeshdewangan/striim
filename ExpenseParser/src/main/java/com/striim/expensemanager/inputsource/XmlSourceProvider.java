package com.striim.expensemanager.inputsource;

import com.striim.expensemanager.parser.XmlSourceExpenseParser;

import java.util.Properties;

public class XmlSourceProvider implements InputSourceProvider{
    public XmlSourceProvider(){}
    @Override
    public boolean supports(String type) {
        return type.equals("XML");
    }

    @Override
    public InputSourceBase create(Properties props) {
        return new XmlSourceExpenseParser(props);
    }
}
