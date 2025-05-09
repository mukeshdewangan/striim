package com.striim.expensemanager.parser;

import com.striim.expensemanager.currency.CurrencyCode;
import com.striim.expensemanager.date_util.DateTimeConverter;
import com.striim.expensemanager.expense.ExpenseEntry;
import com.striim.expensemanager.validator.FileValidator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.InputStream;
import java.util.ArrayList;

public class XmlExpenseParser implements ExpenseFileParser {
    FileValidator fileValidator;

    public XmlExpenseParser(FileValidator validator){
        this.fileValidator = validator;
    }
    @Override
    public void printReport() {

    }

    @Override
    public List<ExpenseEntry> parse(String filePath, String... schemaFile) {
        if (!fileValidator.isValidFile(filePath, schemaFile)) {
            throw new RuntimeException("Invalid file");
        }
        // parse the XML file and return the list of ExpenseEntry objects
        List<ExpenseEntry> expenseEntries = parseInternal(filePath);
        return expenseEntries;
    }

    private List<ExpenseEntry> parseInternal( String xmlPath) {
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

            // 3. Print the entries
            System.out.println("\nParsed Expense Entries:");
            expenses.forEach(System.out::println);
            return expenses;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return expenses;
    }

}
