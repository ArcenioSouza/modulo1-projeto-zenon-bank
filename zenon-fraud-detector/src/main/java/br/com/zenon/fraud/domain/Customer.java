package br.com.zenon.fraud.domain;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Customer {
    private String name;
    private BigDecimal oldBalance;
    private BigDecimal newBalance;
}
