package com.striim.expensemanager.parser;

import com.striim.expensemanager.currency.CurrencyCode;
import com.striim.expensemanager.date_util.DateTimeConverter;
import com.striim.expensemanager.expense.ExpenseEntry;
import com.striim.expensemanager.validator.FileValidator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import com.striim.expensemanager.validator.XMLValidator;
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

public class XmlExpenseParser implements ExpenseFileParser {
    FileValidator fileValidator;

    public XmlExpenseParser(){
        this.fileValidator = new XMLValidator();
    }
    @Override
    public void printReport() {}

    @Override
    public List<ExpenseEntry> parse(String filePath, String... schemaFile) {
        if (!fileValidator.isValidFile(filePath, schemaFile)) {
            throw new RuntimeException("Invalid file");
        }
        // parse the XML file and return the list of ExpenseEntry objects
        //List<ExpenseEntry> expenseEntries = parseInternal(filePath);
        List<ExpenseEntry> expenseEntries = new ArrayList<>();
        try {
            expenseEntries = parseLargeXML(filePath, schemaFile);
            return expenseEntries;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    private List<ExpenseEntry> parseInternal(String xmlPath) {
        List<ExpenseEntry> expenses = new ArrayList<>();
        Path path = Paths.get(xmlPath);
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

                String description = expenseElement.getElementsByTagName("description").item(0).getTextContent().trim();
                String currencyType = expenseElement.getElementsByTagName("currencytype").item(0).getTextContent().trim();
                double amount = Double.parseDouble(expenseElement.getElementsByTagName("amount").item(0).getTextContent().trim());
                String date = expenseElement.getElementsByTagName("date").item(0).getTextContent().trim();
                CurrencyCode currency = CurrencyCode.valueOf(currencyType.toUpperCase());
                LocalDateTime dateTime = DateTimeConverter.convertToDateTime(date);

                ExpenseEntry entry = new ExpenseEntry(description, currency, amount, dateTime);
                expenses.add(entry);
            }

            System.out.println("\nParsed Expense Entries:");
            return expenses;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return expenses;
    }

    private List<ExpenseEntry> parseLargeXML(String expenseFilePath, String... xsdFilePath) throws SAXException, ParserConfigurationException, IOException {
        File xmlFile = new File(expenseFilePath);
        File xsdFile = new File(xsdFilePath[0]);

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(xsdFile);
        ValidatorHandler validatorHandler = schema.newValidatorHandler();

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        XMLReader reader = saxParser.getXMLReader();
        ExpenseHandler expenseHandler = new ExpenseHandler();

        reader.setContentHandler(new CombinedHandler(validatorHandler, expenseHandler));
        reader.parse(new InputSource(xmlFile.getAbsolutePath()));
        return expenseHandler.getExpenses();
    }

}
