package net.testproj.auth.DTOs.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRequestDTO {
    private String exchangeCode;
}
