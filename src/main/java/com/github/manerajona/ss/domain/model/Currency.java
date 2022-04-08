package com.github.manerajona.ss.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public final class Currency {
    private Long id;
    private String name;
    private String symbol;
    @JsonProperty("buy_price")
    private Double price;
    @JsonProperty("market_cap")
    private Double marketCap;
    private Boolean isCrypto;

    public Currency() {
    }

    public Currency(String name, String symbol, Double price, Double marketCap, Boolean isCrypto) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.marketCap = marketCap;
        this.isCrypto = isCrypto;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getPrice() {
        return price;
    }

    public Double getMarketCap() {
        return marketCap;
    }

    public Boolean getIsCrypto() {
        return isCrypto;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setMarketCap(Double marketCap) {
        this.marketCap = marketCap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(id, currency.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
