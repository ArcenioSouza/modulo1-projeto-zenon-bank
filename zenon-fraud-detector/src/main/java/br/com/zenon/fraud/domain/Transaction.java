package br.com.zenon.fraud.domain;

import java.math.BigDecimal;

public record Transaction(long id, int step, TransactionType type, BigDecimal amount, Customer customerOrigem, Customer customerDestino,
        boolean isFraud, boolean isFlaggedFraud) {

}
