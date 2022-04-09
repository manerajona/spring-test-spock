package com.github.manerajona.ss.controller

import com.github.manerajona.ss.IntegrationTestSpecification
import com.github.manerajona.ss.controller.dto.CurrentPriceResponse
import com.github.manerajona.ss.domain.model.Currency
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.scheduler.Schedulers
import spock.lang.Unroll

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import static org.assertj.core.api.Assertions.assertThat


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CurrencyControllerIntegrationTest extends IntegrationTestSpecification {

    @Unroll
    @Order(1)
    def "Create currency #symbol (#name)"() {
        def uri = '/currencies'
        def countDownLatch = new CountDownLatch(1)
        def currency = new Currency(id, name, symbol, buyPrice, marketCap, isCrypto)

        expect: 'a new currency will be created and the location of the resource in the header'

        webClient.post().uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(currency))
                .retrieve()
                .toBodilessEntity()
                .publishOn(Schedulers.parallel())
                .subscribe(responseEntity -> {
                    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED)

                    def location = responseEntity.getHeaders() get(HttpHeaders.LOCATION) stream() findAny() orElse('')
                    assertThat(location).containsOnlyOnce(uri)

                    countDownLatch.countDown()
                })

        countDownLatch.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        assertThat(countDownLatch.getCount()).isEqualTo(0)

        where: 'Use cases'

        id | name             | symbol | buyPrice  | marketCap     | isCrypto
        1  | 'US Dollar'      | 'US'   | 1D        | 0L            | false
        2  | 'Euro'           | 'EUR'  | 1.09D     | 0L            | false
        3  | 'Argentine Peso' | 'ARS'  | 0.0089D   | 0L            | false
        4  | 'Bitcoin'        | 'BTC'  | 42593.28D | 809545201497L | true
        5  | 'Ethereum'       | 'ETH'  | 3219.62D  | 387318531344L | true
        6  | 'Tether'         | 'USDT' | 1D        | 82514561751L  | true
        7  | 'Cardano'        | 'ADA'  | 1.04D     | 35165954031L  | true
    }

    @Unroll
    @Order(2)
    def "Get currency by id=#id"() {
        def uri = "/currencies/$id"
        def countDownLatch = new CountDownLatch(1)

        expect: 'currency will be returned from the service'

        webClient.get().uri(uri)
                .retrieve()
                .bodyToMono(Currency.class)
                .publishOn(Schedulers.single())
                .subscribe(response -> {
                    assertThat(response.getId()).isEqualTo(id)
                    assertThat(response.getName()).isEqualTo(name)
                    assertThat(response.getSymbol()).isEqualTo(symbol)
                    assertThat(response.getPrice()).isEqualTo(buyPrice)
                    assertThat(response.getMarketCap()).isEqualTo(marketCap)
                    assertThat(response.getIsCrypto()).isEqualTo(isCrypto)

                    countDownLatch.countDown()
                })

        countDownLatch.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        assertThat(countDownLatch.getCount()).isEqualTo(0)

        where: 'Use cases'

        id | name             | symbol | buyPrice  | marketCap     | isCrypto
        1  | 'US Dollar'      | 'US'   | 1D        | 0L            | false
        2  | 'Euro'           | 'EUR'  | 1.09D     | 0L            | false
        3  | 'Argentine Peso' | 'ARS'  | 0.0089D   | 0L            | false
        4  | 'Bitcoin'        | 'BTC'  | 42593.28D | 809545201497L | true
        5  | 'Ethereum'       | 'ETH'  | 3219.62D  | 387318531344L | true
        6  | 'Tether'         | 'USDT' | 1D        | 82514561751L  | true
        7  | 'Cardano'        | 'ADA'  | 1.04D     | 35165954031L  | true
    }

    @Order(3)
    def 'List all currencies'() {
        def uri = '/currencies'
        def countDownLatch = new CountDownLatch(1)

        expect: 'a list with all currencies will be returned'

        webClient.get().uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Currency.class)
                .publishOn(Schedulers.parallel())
                .count()
                .subscribe(count -> {
                    assertThat(count) isEqualTo(7)
                    countDownLatch.countDown()
                })

        countDownLatch.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        assertThat(countDownLatch.getCount()).isEqualTo(0)
    }

    @Unroll
    @Order(4)
    def "Update #symbol price to #buyPrice"() {
        def uri = "/currencies/$id"
        def countDownLatch = new CountDownLatch(1)
        def currency = new Currency()
        currency.setPrice(buyPrice)

        expect: 'the price of the existing currency will be updated'

        webClient.patch().uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(currency))
                .retrieve()
                .toBodilessEntity()
                .publishOn(Schedulers.single())
                .subscribe(responseEntity -> {
                    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT)

                    countDownLatch.countDown()
                })

        countDownLatch.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        assertThat(countDownLatch.getCount()).isEqualTo(0)

        where: 'Use cases'

        id | symbol | buyPrice
        4  | 'BTC'  | 43000D
        5  | 'ETH'  | 3300D
        7  | 'ADA'  | 1.1D
    }

    @Unroll
    @Order(5)
    def "Get #symbol by symbol"() {
        def uri = "/currencies/ISO:$symbol"
        def countDownLatch = new CountDownLatch(1)

        expect: 'the currency will be found by its symbol'

        webClient.get().uri(uri)
                .retrieve()
                .bodyToMono(Currency.class)
                .publishOn(Schedulers.single())
                .subscribe(response -> {
                    assertThat(response.getId()).isEqualTo(id)
                    assertThat(response.getName()).isEqualTo(name)
                    assertThat(response.getSymbol()).isEqualTo(symbol)
                    assertThat(response.getPrice()).isEqualTo(buyPrice)
                    assertThat(response.getMarketCap()).isEqualTo(marketCap)
                    assertThat(response.getIsCrypto()).isEqualTo(isCrypto)

                    countDownLatch.countDown()
                })

        countDownLatch.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        assertThat(countDownLatch.getCount()).isEqualTo(0)

        where: 'Use cases'

        id | name       | symbol | buyPrice | marketCap     | isCrypto
        4  | 'Bitcoin'  | 'BTC'  | 43000D   | 809545201497L | true
        5  | 'Ethereum' | 'ETH'  | 3300D    | 387318531344L | true
        6  | 'Tether'   | 'USDT' | 1D       | 82514561751L  | true
        7  | 'Cardano'  | 'ADA'  | 1.1D     | 35165954031L  | true
    }

    @Unroll
    @Order(6)
    def "Delete #symbol by id"() {
        def uri = "/currencies/$id"
        def countDownLatch = new CountDownLatch(1)

        expect: 'the currency will be removed'

        webClient.delete().uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity()
                .publishOn(Schedulers.single())
                .subscribe(responseEntity -> {
                    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT)

                    countDownLatch.countDown()
                })

        countDownLatch.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        assertThat(countDownLatch.getCount()).isEqualTo(0)

        where: 'Use cases'

        id | symbol
        4  | 'BTC'
        5  | 'ETH'
        7  | 'ADA'
    }

    @Unroll
    @Order(7)
    def "Get currency by id #id should respond with Not Found"() {
        def uri = "/currencies/$id"
        def countDownLatch = new CountDownLatch(1)

        expect: 'the service will return currency not found'

        webClient.get().uri(uri)
                .retrieve()
                .bodyToMono(Currency.class)
                .publishOn(Schedulers.single())
                .subscribe(responseEntity -> {
                    println responseEntity
                }, throwable -> countDownLatch.countDown())

        countDownLatch.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        assertThat(countDownLatch.getCount()).isEqualTo(0)

        where: 'Use cases'

        id | symbol
        4  | 'BTC'
        5  | 'ETH'
        7  | 'ADA'
    }

    @Order(8)
    def 'Get Btc Current Price should return the latest price of Bitcoin'() {
        def uri = '/currencies/btc-currentprice'
        def countDownLatch = new CountDownLatch(1)

        expect: 'the service will return current price of BTC in EUR, GBP and USD'

        webClient.get().uri(uri)
                .retrieve()
                .bodyToMono(CurrentPriceResponse.class)
                .publishOn(Schedulers.single())
                .subscribe(response -> {
                    assertThat(response).isNotNull()
                    assertThat(response.getChartName()).isEqualTo('Bitcoin')
                    assertThat(response.getBpi().getEur().getCode()).isEqualTo('EUR')
                    assertThat(response.getBpi().getGbp().getCode()).isEqualTo('GBP')
                    assertThat(response.getBpi().getUsd().getCode()).isEqualTo('USD')
                    countDownLatch.countDown()
                })

        countDownLatch.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        countDownLatch.getCount() == 0L
    }
}
