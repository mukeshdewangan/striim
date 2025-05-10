package com.striim.expensemanager.parser;

import com.striim.expensemanager.currency.CurrencyCode;
import com.striim.expensemanager.date_util.DateTimeConverter;
import com.striim.expensemanager.driver.ConfigLoader;
import com.striim.expensemanager.driver.ExpenseManagerDriver;
import com.striim.expensemanager.expense.Constants;
import com.striim.expensemanager.expense.ExpenseEntry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import com.striim.expensemanager.inputsource.InputSourceBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.ValidatorHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import static com.striim.expensemanager.expense.Constants.EXPENSE_FILE_PATH;

public class XmlSourceExpenseParser implements InputSourceBase {
    private static final Logger logger = LoggerFactory.getLogger(XmlSourceExpenseParser.class);
    private final String expenseFile;
    ValidatorHandler validatorHandler;

    public XmlSourceExpenseParser(Properties properties){
        expenseFile = properties.getProperty(EXPENSE_FILE_PATH);
    }

    @Override
    public Iterator<ExpenseEntry> getExpenses(Properties properties) {
        // Below is Deprecated - parse the XML file and return the list of ExpenseEntry objects
        //List<ExpenseEntry> expenseEntries = parseInternal(filePath);
        try {
            Iterator<ExpenseEntry> iterator = parseLargeXML(expenseFile);
            return iterator;
        }
        catch (Exception e){
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    private Iterator<ExpenseEntry> parseLargeXML(String expenseFilePath) throws SAXException, ParserConfigurationException, IOException {
        validatorHandler = getValidatorHandler();

        File xmlFile = new File(expenseFilePath);
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        XMLReader reader = saxParser.getXMLReader();
        ExpenseHandler expenseHandler = new ExpenseHandler();

        reader.setContentHandler(new CombinedHandler(validatorHandler, expenseHandler));
        try {
            reader.parse(new InputSource(xmlFile.getAbsolutePath()));
        } finally {
            // Ensure poison pill is always inserted
            expenseHandler.signalEndOfStream();
        }

//        for (ExpenseEntry entry : expenseHandler) {
//            // Process each entry lazily
//            logger.info(entry);
//        }
//        return expenseHandler.getExpenses();
        return expenseHandler.iterator();
    }

    ValidatorHandler getValidatorHandler() throws SAXException {
        String xsdFilePath = ConfigLoader.getInstance().getXMLSchemaPath();

        File xsdFile = new File(xsdFilePath);

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(xsdFile);
        return schema.newValidatorHandler();
    }

    // Deprecated method, which uses SAX to parse the XML file
    @Deprecated
    private List<ExpenseEntry> parseInternal(String filePath, String... schemaFile) {
//        if (!fileValidator.isValidFile(filePath, schemaFile)) {
//            throw new RuntimeException("Invalid file");
//        }
        List<ExpenseEntry> expenses = new ArrayList<>();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new RuntimeException("File not found");
        }
        try (InputStream xmlStream = Files.newInputStream(path)) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlStream);

            NodeList expenseNodes = document.getElementsByTagName("expense");

            for (int i = 0; i < expenseNodes.getLength(); i++) {
                Element expenseElement = (Element) expenseNodes.item(i);

                String description = expenseElement.getElementsByTagName(Constants.DESCRIPTION).item(0).getTextContent().trim();
                String currencyType = expenseElement.getElementsByTagName(Constants.CURRENCYTYPE).item(0).getTextContent().trim();
                double amount = Double.parseDouble(expenseElement.getElementsByTagName(Constants.AMOUNT).item(0).getTextContent().trim());
                String date = expenseElement.getElementsByTagName(Constants.DATE).item(0).getTextContent().trim();
                CurrencyCode currency = CurrencyCode.valueOf(currencyType.toUpperCase());
                LocalDateTime dateTime = DateTimeConverter.convertToDateTime(date);

                ExpenseEntry entry = new ExpenseEntry(description, currency, amount, dateTime);
                expenses.add(entry);
            }

            logger.info("\nParsed Expense Entries:");
            return expenses;

        } catch (Exception e) {
            logger.info("Error: " + e.getMessage());
        }
        return expenses;
    }
}
