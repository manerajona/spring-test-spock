package com.github.manerajona.ss.domain.model

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import spock.lang.Specification

class AccountTest extends Specification {

    EasyRandom easyRandom

    void setup() {
        EasyRandomParameters parameters =
                new EasyRandomParameters()
                        .stringLengthRange(5, 10)
                        .seed(new Random().nextInt())

        easyRandom = new EasyRandom(parameters)
    }

    def 'Dummy Test'() {
        def i = 1 + 1

        expect:
        i == 2
    }

    def 'GetBalance should be the same'() {
        final def initialBalance = easyRandom.nextDouble()
        final def testAccount =
                Account.builder()
                        .username(easyRandom.nextObject(String.class))
                        .balance(initialBalance)
                        .currency(easyRandom.nextObject(Currency.class))
                        .build()

        expect:
        testAccount.getBalance() == initialBalance
    }

    def 'When Deposit balance should be 1200.00'() {
        given:
        final def initialBalance = 1000.00
        final def amount = 200.00
        final def testAccount =
                Account.builder()
                        .username(easyRandom.nextObject(String.class))
                        .balance(initialBalance)
                        .currency(easyRandom.nextObject(Currency.class))
                        .build()

        when:
        def balance = testAccount.deposit(amount)

        then:
        balance == (double) 1200.00
        testAccount.getBalance() == (double) 1200.00
    }

    def 'When Withdraw balance should be 800.00'() {
        given:
        final def initialBalance = 1000.00
        final def amount = 200.00
        final def testAccount =
                Account.builder()
                        .username(easyRandom.nextObject(String.class))
                        .balance(initialBalance)
                        .currency(easyRandom.nextObject(Currency.class))
                        .build()

        when:
        def balance = testAccount.withdraw(amount, true)

        then:
        balance == (double) 800.00
        testAccount.getBalance() == (double) 800.00
    }

    def 'When Withdraw from an ATM 500.00 or more should throw IllegalArgumentException'() {
        given:
        final def initialBalance = 1000.00
        final def amount = 600.00
        final def testAccount =
                Account.builder()
                        .username(easyRandom.nextObject(String.class))
                        .balance(initialBalance)
                        .currency(easyRandom.nextObject(Currency.class))
                        .build()

        when:
        testAccount.withdraw(amount, false)

        then:
        thrown(IllegalArgumentException)
        testAccount.getBalance() == initialBalance.doubleValue()
    }

}
