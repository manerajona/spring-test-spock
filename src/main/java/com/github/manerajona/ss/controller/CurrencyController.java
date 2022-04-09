package com.github.manerajona.ss.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.manerajona.ss.controller.dto.CurrentPriceResponse;
import com.github.manerajona.ss.domain.model.Currency;
import com.github.manerajona.ss.domain.usecase.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping(CurrencyController.CURRENCY_URI)
@RequiredArgsConstructor
public class CurrencyController {

    public static final String CURRENCY_URI = "/currencies";

    private final CurrencyService service;
    private final WebClient webClient;
    private final ObjectMapper mapper;

    @Value("${coindesk.url}")
    private String url;

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The resource was not found")
    static class NotFoundException extends RuntimeException {
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
    public ResponseEntity<Flux<Currency>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<Currency>> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getById(id)
                .defaultIfEmpty(new Currency())
                .doOnNext(c -> {
                    if (c.getId() == null)
                        throw new NotFoundException();
                }));
    }

    @GetMapping("/ISO:{symbol}")
    public ResponseEntity<Mono<Currency>> getBySymbol(@PathVariable("symbol") String symbol) {
        return ResponseEntity.ok(service.getBySymbol(symbol)
                .defaultIfEmpty(new Currency())
                .doOnNext(c -> {
                    if (c.getId() == null)
                        throw new NotFoundException();
                }));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Mono<Void>> update(@PathVariable("id") Long id, @RequestBody Currency currency) {
        service.update(id, currency).subscribe(c -> {
            if (c.getId() == null)
                throw new NotFoundException();
        });
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Mono<Void>> delete(@PathVariable("id") Long id) {
        service.delete(id).subscribe();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/btc-currentprice")
    public ResponseEntity<Mono<CurrentPriceResponse>> getBtcCurrentPrice() {

        Mono<CurrentPriceResponse> responseMono = webClient.get()
                .uri(url.concat("/v1/bpi/currentprice.json"))
                .retrieve()
                .bodyToMono(String.class)
                .map(json -> {
                    CurrentPriceResponse response;
                    try {
                        response = mapper.readValue(json, CurrentPriceResponse.class);
                    } catch (JsonProcessingException e) {
                        response = new CurrentPriceResponse();
                    }
                    return response;
                });

        return ResponseEntity.ok(responseMono);
    }

}
