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
        "time",
        "disclaimer",
        "chartName",
        "bpi"
})
public class CurrentPriceResponse {
    @JsonProperty("time")
    private TimeDto time;
    @JsonProperty("disclaimer")
    private String disclaimer;
    @JsonProperty("chartName")
    private String chartName;
    @JsonProperty("bpi")
    private BpiDto bpi;
}
