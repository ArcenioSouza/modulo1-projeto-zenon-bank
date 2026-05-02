package br.com.zenon.fraud.domain;

public enum TransactionType {
    CASH_IN,
    CASH_OUT,
    DEBIT,
    PAYMENT,
    TRANSFER;

    public static TransactionType fromString(String value) {
        try {
            return TransactionType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Tipo de transação inválido: " + value);
        }
    }
}
