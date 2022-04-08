package com.github.manerajona.ss.domain.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public final class Account {
    private Long id;
    private String username;
    private double balance;
    private Currency currency;

    public Account(String username, double balance, Currency currency) {
        this.username = username;
        this.balance = balance;
        this.currency = currency;
    }

    public double deposit(double amount) {
        balance += amount;
        return balance;
    }

    // The atm argument is true if the user is performing the transaction at an ATM
    // It's false if the customer is performing the on the app
    public double withdraw(double amount, boolean atm) {
        if ((amount > 500.00) & !atm) {
            throw new IllegalArgumentException();
        }
        balance -= amount;
        return balance;
    }

    public String getUsername() {
        return username;
    }

    public double getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
