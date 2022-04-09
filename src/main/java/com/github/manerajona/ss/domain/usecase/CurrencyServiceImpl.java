package com.github.manerajona.ss.domain.usecase;

import com.github.manerajona.ss.domain.model.Currency;
import com.github.manerajona.ss.domain.repository.CurrencyRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository repository;

    @Override
    public Mono<Long> create(Currency currency) {
       return repository.save(currency).map(Currency::getId);
    }

    @Override
    public Mono<Currency> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Currency> getBySymbol(String symbol) {
        return repository.findBySymbol(symbol);
    }

    @Override
    public Flux<Currency> getAll() {
        return repository.findAll()
                .sort(Comparator.comparing(Currency::getIsCrypto).reversed()
                        .thenComparing(Currency::getMarketCap)
                        .thenComparing(Currency::getId));
    }

    @Override
    public Mono<Currency> update(Long id, Currency currency) {
        return repository.findById(id)
                .defaultIfEmpty(new Currency())
                .map(p -> {
                    Optional.ofNullable(currency.getPrice()).ifPresent(p::setPrice);
                    Optional.ofNullable(currency.getMarketCap()).ifPresent(p::setMarketCap);
                    return p;
                }).flatMap(updated -> Optional.ofNullable(updated.getId())
                        .map(i -> repository.save(updated))
                        .orElse(Mono.just(updated)));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }

}
