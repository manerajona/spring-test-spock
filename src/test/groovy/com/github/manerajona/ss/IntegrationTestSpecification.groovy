package com.github.manerajona.ss


import groovy.util.logging.Slf4j
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import spock.lang.Shared
import spock.lang.Specification

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
abstract class IntegrationTestSpecification extends Specification {

    protected static final int TIMEOUT_MILLIS = 1500

    @Shared
    protected WebClient webClient;

    def setupSpec() {

        def baseUrl = 'http://localhost:8080'
        def wiretap = HttpClient.create().wiretap(true)

        log.info "Init WebClient with base url=$baseUrl"

        webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(wiretap))
                .build();
    }

    def cleanupSpec() {
        log.info "Integration tests finished!"
    }
}
