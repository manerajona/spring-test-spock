package com.github.manerajona.ss.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public final class Currency {
    private Long id;
    private String name;
    private String symbol;
    @JsonProperty("buy_price")
    private Double price;
    @JsonProperty("market_cap")
    private Long marketCap;
    private Boolean isCrypto;

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setMarketCap(Long marketCap) {
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
