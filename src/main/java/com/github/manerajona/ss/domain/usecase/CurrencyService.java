package com.github.manerajona.ss.domain.usecase;

import com.github.manerajona.ss.domain.model.Currency;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyService {
    Mono<Long> create(Currency currency);
    Mono<Currency> getById(Long id);
    Mono<Currency> getBySymbol(String symbol);
    Flux<Currency> getAll();
    Mono<Currency> update(Long id, Currency currency);
    Mono<Void> delete(Long id);
}
