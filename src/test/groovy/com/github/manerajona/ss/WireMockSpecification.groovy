package com.github.manerajona.ss

import com.github.tomakehurst.wiremock.junit.WireMockRule
import groovy.util.logging.Slf4j
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class WireMockSpecification extends Specification {

    static WireMockRule wireMockRule

    protected static final int WIRE_PORT = 8080

    def setupSpec() {
        log.debug "Starting WireMock..."
        wireMockRule = new WireMockRule(WIRE_PORT)
        wireMockRule.start()
    }

    def cleanupSpec() {
        wireMockRule.stop()
        log.debug "WireMock was stopped!"
    }

    static def testStub(String body, String uri, int status) {
        stubFor(post(urlEqualTo(uri))
                .withRequestBody(equalTo(body))
                .willReturn(aResponse().withStatus(status))
        )
    }
}
