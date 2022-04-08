package com.github.manerajona.ss.domain.repository;

import com.github.manerajona.ss.domain.model.Currency;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CurrencyRepository extends ReactiveMongoRepository<Currency, Long> {
    Flux<Currency> findBySymbol(String symbol);
}
