package com.github.manerajona.ss

import groovy.util.logging.Slf4j
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Shared
import spock.lang.Specification

@Slf4j
abstract class UnitTestSpecification extends Specification {

    @Shared
    protected WebClient webClient;

    def setupSpec() {

    }

    def cleanupSpec() {
        log.info "Integration tests finished!"
    }
}
