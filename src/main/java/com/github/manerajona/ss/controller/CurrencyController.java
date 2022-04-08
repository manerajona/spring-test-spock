package com.github.manerajona.ss.controller;

import com.github.manerajona.ss.domain.model.Currency;
import com.github.manerajona.ss.domain.usecase.CurrencyServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping(CurrencyController.CURRENCY_URI)
public class CurrencyController {

    public static final String CURRENCY_URI = "/currencies";

    private final CurrencyServiceImpl service;

    public CurrencyController(CurrencyServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Mono<Void>> create(@RequestBody Currency currency, UriComponentsBuilder uriComponentsBuilder) {
        AtomicLong atomicLong = new AtomicLong();
        service.create(currency).subscribe(atomicLong::set);

        return ResponseEntity.created(uriComponentsBuilder.path(CURRENCY_URI + "/{id}")
                        .buildAndExpand(atomicLong.get())
                        .toUri())
                .build();
    }

    @GetMapping
    public ResponseEntity<Flux<Currency>> getByCategoryAndAvailability(@RequestParam(required = false) String category) {
        Flux<Currency> currencyFlux = Optional.ofNullable(category)
                .map(service::getBySymbol)
                .orElse(service.getAll());

        return ResponseEntity.ok(currencyFlux);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<Currency>> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getById(id)
                .defaultIfEmpty(new Currency())
                .doOnNext(p -> {
                    if (p.getId() == null)
                        throw new NotFoundException();
                }));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Mono<Void>> update(@PathVariable("id") Long id, @RequestBody Currency currency) {
        service.update(id, currency).subscribe(p -> {
            if (p.getId() == null)
                throw new NotFoundException();
        });
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Mono<Void>> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The resource was not found")
    static class NotFoundException extends RuntimeException {
    }

}
