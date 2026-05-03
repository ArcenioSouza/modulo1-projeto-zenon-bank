package br.com.zenon.fraud.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Customer {
    @NotNull(message = "Name não pode ser nulo")
    private String name;

    @NotNull(message = "OldBalance não pode ser nulo")
    @Min(value = 0, message = "OldBalance não pode ser negativo")
    private BigDecimal oldBalance;

    @NotNull(message = "NewBalance não pode ser nulo")
    @Min(value = 0, message = "NewBalance não pode ser negativo")
    private BigDecimal newBalance;
}
