package com.striim.expensemanager.expense;

import com.striim.expensemanager.currency.CurrencyCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExpenseEntry {
    private double amount;
    private String description;
    private LocalDateTime date;
    private CurrencyCode currency;

    public ExpenseEntry( String description,   CurrencyCode currency, double amount, LocalDateTime date) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public CurrencyCode getCurrency() {
        return currency;
    }


    @Override
    public String toString() {
        return "ExpenseEntry{" +
                "description='" + description + '\'' +
                ", currencyType='" + currency.toString() + '\'' +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                '}';
    }
}
