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
        "updated",
        "updatedISO",
        "updateduk"
})
public class TimeDto {
    @JsonProperty("updated")
    private String updated;
    @JsonProperty("updatedISO")
    private String updatedISO;
    @JsonProperty("updateduk")
    private String updateduk;
}
