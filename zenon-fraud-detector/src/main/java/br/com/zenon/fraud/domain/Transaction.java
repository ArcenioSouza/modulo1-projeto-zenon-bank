package br.com.zenon.fraud.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record Transaction(
        @NotNull(message = "Id não pode ser nulo")
        long id,

        @NotNull(message = "Step não pode ser nulo")
        @Min(value = 1, message = "Step não pode ser menor que 1")
        int step,

        @NotNull(message = "Type não pode ser nulo")
        TransactionType type,

        @NotNull(message = "Amount não pode ser nulo")
        @Min(value = 0, message = "Amount não pode ser negativo")
        BigDecimal amount,

        @Valid
        Customer customerOrigem,

        @Valid
        Customer customerDestino,

        @NotNull(message = "IsFraud não pode ser nulo")
        boolean isFraud,

        @NotNull(message = "IsFlaggedFraud não pode ser nulo")
        boolean isFlaggedFraud) {

}
