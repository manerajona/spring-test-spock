package com.github.manerajona.ss.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@Builder
public final class Account {
    private Long id;
    private String username;
    private double balance;
    private Currency currency;

    public double deposit(double amount) {
        balance += amount;
        return balance;
    }

    // The atm argument is true if the user is performing the transaction at an ATM
    // It's false if the customer is performing the on the app
    public double withdraw(double amount, boolean atm) {
        if ((amount >= 500.00) & !atm) {
            throw new IllegalArgumentException();
        }
        balance -= amount;
        return balance;
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
