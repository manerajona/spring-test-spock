package com.github.manerajona.ss.controller

import com.github.manerajona.ss.domain.model.Currency
import com.github.manerajona.ss.domain.usecase.CurrencyService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification

import static org.hamcrest.Matchers.equalTo

@WebFluxTest(CurrencyController.class)
class CurrencyControllerTest extends Specification {

    @Autowired
    WebTestClient webTestClient

    @SpringBean
    CurrencyService service = Stub()

    def "Create should return created"() {

        given:
        def uri = '/currencies'
        def currency = new Currency(1, 'Bitcoin', 'BTC', 42593.28D, 809545201497L, true)
        def location = new URI('/currencies/1')

        when:
        service.create(_ as Currency) >> Mono.just(1L)

        then:
        webTestClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(currency))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location(location.toString());
    }

    def "GetAll should return ok with 3 elements"() {
        given:
        def uri = '/currencies'
        def currency = new Currency(1, 'Bitcoin', 'BTC', 42593.28D, 809545201497L, true)

        when:
        service.getAll() >> Flux.just(currency, currency, currency)

        then:
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .value(list -> (long) list.size(), equalTo(3L))
    }

    def "GetById should return ok"() {
        given:
        def uri = '/currencies/1'
        def currency = new Currency(1, 'Bitcoin', 'BTC', 42593.28D, 809545201497L, true)

        when:
        service.getById(_ as Long) >> Mono.just(currency)

        then:
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Currency.class)
                .value(c -> c.getId(), equalTo(1L))
                .value(c -> c.getName(), equalTo('Bitcoin'))
                .value(c -> c.getSymbol(), equalTo('BTC'))
                .value(c -> c.getPrice(), equalTo(42593.28D))
                .value(c -> c.getMarketCap(), equalTo(809545201497L))
                .value(c -> c.getIsCrypto(), equalTo(true))
    }

    def "GetBySymbol should return ok"() {
        given:
        def uri = '/currencies/ISO:BTC'
        def currency = new Currency(1, 'Bitcoin', 'BTC', 42593.28D, 809545201497L, true)

        when:
        service.getBySymbol(_ as String) >> Mono.just(currency)

        then:
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Currency.class)
                .value(c -> c.getId(), equalTo(1L))
                .value(c -> c.getName(), equalTo('Bitcoin'))
                .value(c -> c.getSymbol(), equalTo('BTC'))
                .value(c -> c.getPrice(), equalTo(42593.28D))
                .value(c -> c.getMarketCap(), equalTo(809545201497L))
                .value(c -> c.getIsCrypto(), equalTo(true))
    }

    def "Update should return no content"() {
        given:
        def uri = '/currencies/1'
        def currency = new Currency(1, 'Bitcoin', 'BTC', 42593.28D, 809545201497L, true)

        when:
        service.update(_ as Long, _ as Currency) >> Mono.just(currency)

        then:
        webTestClient.patch()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(currency))
                .exchange()
                .expectStatus().isNoContent()
    }

    def "Delete should return no content"() {
        given:
        def uri = '/currencies/1'

        when:
        service.delete(_ as Long) >> Mono.empty()

        then:
        webTestClient.delete()
                .uri(uri)
                .exchange()
                .expectStatus().isNoContent()
    }

}
