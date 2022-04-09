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
        "code",
        "symbol",
        "rate",
        "description",
        "rate_float"
})
public class CurrencyDto {
    @JsonProperty("code")
    private String code;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("rate")
    private String rate;
    @JsonProperty("description")
    private String description;
    @JsonProperty("rate_float")
    private Double rateFloat;
}
