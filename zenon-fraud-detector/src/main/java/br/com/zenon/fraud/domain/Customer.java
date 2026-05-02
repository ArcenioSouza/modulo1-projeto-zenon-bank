package br.com.zenon.fraud.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Customer {
    private String name;
    private BigDecimal oldBalance;
    private BigDecimal newBalance;
}
