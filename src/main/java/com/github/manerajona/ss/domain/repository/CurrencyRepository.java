package com.github.manerajona.ss.domain.repository;

import com.github.manerajona.ss.domain.model.Currency;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CurrencyRepository extends ReactiveMongoRepository<Currency, Long> {
    Mono<Currency> findBySymbol(String symbol);
}
