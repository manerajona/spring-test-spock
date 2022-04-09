package com.github.manerajona.ss.domain.model

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import spock.lang.Specification

class AccountTest extends Specification {

    EasyRandom easyRandom

    void setup() {
        def seed = new Random().nextInt()
        EasyRandomParameters parameters = new EasyRandomParameters().stringLengthRange(5, 10)seed(seed)
        easyRandom = new EasyRandom(parameters)
    }

    def 'Dummy Test'() {
        def i = 1 + 1

        expect:
        i == 2
    }

    def 'getBalance should remain the same'() {
        final def initialBalance = easyRandom.nextDouble()
        final def testAccount =
                Account.builder()
                        .username(easyRandom.nextObject(String.class))
                        .balance(initialBalance)
                        .currency(easyRandom.nextObject(Currency.class))
                        .build()

        expect: 'the balance remains the same'
        testAccount.getBalance() == initialBalance
    }

    def 'deposit should update balance to 1200.00'() {

        given: 'initial balance'
        final def initialBalance = 1000.00

        and: 'amount to deposit'
        final def amount = 200.00

        and: 'random account'
        final def testAccount =
                Account.builder()
                        .username(easyRandom.nextObject(String.class))
                        .balance(initialBalance)
                        .currency(easyRandom.nextObject(Currency.class))
                        .build()

        when: 'amount is deposited'
        def balance = testAccount.deposit(amount)

        then: 'Balance should be 1200.00'
        balance == (double) 1200.00
        testAccount.getBalance() == (double) 1200.00
    }

    def 'withdraw from the app should update balance to 800.00'() {

        given: 'initial balance'
        final def initialBalance = 1000.00

        and: 'amount to withdraw'
        final def amount = 200.00

        and: 'random account'
        final def testAccount =
                Account.builder()
                        .username(easyRandom.nextObject(String.class))
                        .balance(initialBalance)
                        .currency(easyRandom.nextObject(Currency.class))
                        .build()

        when: 'amount is withdrew using a device distinct from an atm'
        def balance = testAccount.withdraw(amount, false)

        then: 'balance should be 800.00'
        balance == (double) 800.00
        testAccount.getBalance() == (double) 800.00
    }

    def 'withdraw from atm should update balance to 800.00'() {

        given: 'initial balance'
        final def initialBalance = 1000.00

        and: 'amount to withdraw'
        final def amount = 200.00

        and: 'random account'
        final def testAccount =
                Account.builder()
                        .username(easyRandom.nextObject(String.class))
                        .balance(initialBalance)
                        .currency(easyRandom.nextObject(Currency.class))
                        .build()

        when: 'amount is withdrew from atm'
        def balance = testAccount.withdraw(amount, true)

        then: 'balance should be 800.00'
        balance == (double) 800.00
        testAccount.getBalance() == (double) 800.00
    }

    def 'withdraw from atm with amount of 500.00 or more should throw IllegalArgumentException'() {

        given: 'initial balance'
        final def initialBalance = 1000.00

        and: 'amount to withdraw'
        final def amount = 600.00

        and: 'random account'
        final def testAccount =
                Account.builder()
                        .username(easyRandom.nextObject(String.class))
                        .balance(initialBalance)
                        .currency(easyRandom.nextObject(Currency.class))
                        .build()

        when: 'amount is withdrew from atm'
        testAccount.withdraw(amount, false)

        then: 'an exception should be thrown'
        thrown(IllegalArgumentException)
        testAccount.getBalance() == initialBalance.doubleValue()
    }

}
