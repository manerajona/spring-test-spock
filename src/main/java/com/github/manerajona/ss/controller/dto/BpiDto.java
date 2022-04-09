package com.github.manerajona.ss.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "USD",
        "GBP",
        "EUR"
})
public class BpiDto {
    @JsonProperty("USD")
    private CurrencyDto usd;
    @JsonProperty("GBP")
    private CurrencyDto gbp;
    @JsonProperty("EUR")
    private CurrencyDto eur;
}
