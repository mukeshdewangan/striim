package com.striim.expensemanager.parser;

import com.striim.expensemanager.currency.CurrencyCode;
import com.striim.expensemanager.date_util.DateTimeConverter;
import com.striim.expensemanager.expense.ExpenseEntry;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.time.LocalDateTime;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import static com.striim.expensemanager.expense.Constants.*;
import static com.striim.expensemanager.expense.Constants.EXPENSE;

// Logic to process XML elements without loading everything
public class ExpenseHandler extends DefaultHandler implements Iterable<ExpenseEntry> {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseHandler.class);
    private final BlockingQueue<ExpenseEntry> queue = new LinkedBlockingQueue<>();
    private static final ExpenseEntry POISON_PILL_ENTRY = new ExpenseEntry("EOF", null, 0, null);
    private StringBuilder content = new StringBuilder();
    //public List<ExpenseEntry> expenses = new ArrayList<>();
    private boolean finished = false;
    private String description, currency, date;
    private double amount;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        content.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        content.append(ch, start, length);
    }

    @Override
    public void endDocument() {
        finished = true;
        try {
            queue.put(POISON_PILL_ENTRY);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case DESCRIPTION:
                description = content.toString().trim();
                break;
            case CURRENCYTYPE:
                currency = content.toString().trim();
                break;
            case AMOUNT:
                amount = Double.parseDouble(content.toString().trim());
                break;
            case DATE:
                date = content.toString().trim();
                break;
            case EXPENSE:
                CurrencyCode currencyC = CurrencyCode.valueOf(currency.toUpperCase());
                LocalDateTime dateTime = DateTimeConverter.convertToDateTime(date);
                ExpenseEntry entry = new ExpenseEntry(description, currencyC, amount, dateTime);
                //System.out.println(entry);
                //expenses.add(entry);
                try {
                    if(isValidEntry(entry)){
                        queue.put(entry);
                    }
                    resetValues();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while enqueueing entry", e);
                }
                break;
        }
    }

    private void resetValues(){
        description = null;
        currency = null;
        date = null;
        amount = 0;
    }

    private boolean isValidEntry(ExpenseEntry entry){
        if(entry.getAmount() == 0 ||  date == null || currency == null){
            logger.info("Missing one or more field, skipping");
            return false;
        }
        return true;
    }

    @Override
    public Iterator<ExpenseEntry> iterator() {
        return new Iterator<ExpenseEntry>() {
            private ExpenseEntry nextEntry;

            @Override
            public boolean hasNext() {
                if (nextEntry != null) return true;

                try {
                    nextEntry = queue.take();
                    if (nextEntry == POISON_PILL_ENTRY) return false;
                    return true;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }

            @Override
            public ExpenseEntry next() {
                if (!hasNext()) throw new NoSuchElementException();
                ExpenseEntry entry = nextEntry;
                nextEntry = null;
                return entry;
            }
        };
    }

//    public List<ExpenseEntry> getExpenses() {
//        return expenses;
//    }

    public void signalEndOfStream() {
        try {
            queue.put(POISON_PILL_ENTRY);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
