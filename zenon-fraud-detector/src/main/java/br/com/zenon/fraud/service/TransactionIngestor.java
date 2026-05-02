package br.com.zenon.fraud.service;

import br.com.zenon.fraud.domain.Customer;
import br.com.zenon.fraud.domain.Transaction;
import br.com.zenon.fraud.domain.TransactionType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;


import java.util.Set;

public class TransactionIngestor {

    private static final Validator VALIDATOR = Validation
            .byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    private TransactionIngestor() {
    }

    public static List<Transaction> ingestor(String nameFile) throws IOException {
        List<Transaction> transactionsList = new ArrayList<>();
        Path path = Paths.get(nameFile);

        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Channels.newInputStream(channel), StandardCharsets.UTF_8))) {

            reader.readLine();

            String line;
            long id = 1L;
            int readLines = 0;
            while ((line = reader.readLine()) != null && readLines < 1000) {
                readLines++;

                try {
                    transactionsList.add(toTransaction(line, id));
                } catch (RuntimeException e) {
                    System.err.println("Erro: " + line + " - " + e.getMessage());
                }

                id++;
            }
        }

        return transactionsList;
    }

    private static Transaction toTransaction(String line, long id) {
        String[] columns = line.split(",");

        if (columns.length != 11) {
            throw new IllegalArgumentException("Quantidade de colunas invalida: " + columns.length);
        }

        Customer customerOrigem = Customer.builder()
                .name(columns[3])
                .oldBalance(new BigDecimal(columns[4]))
                .newBalance(new BigDecimal(columns[5]))
                .build();

        Customer customerDestino = Customer.builder()
                .name(columns[6])
                .oldBalance(new BigDecimal(columns[7]))
                .newBalance(new BigDecimal(columns[8]))
                .build();

        Transaction transaction = new Transaction(
                id,
                Integer.parseInt(columns[0]),
                TransactionType.fromString(columns[1]),
                new BigDecimal(columns[2]),
                customerOrigem,
                customerDestino,
                parseBoolean(columns[9]),
                parseBoolean(columns[10])
            );

        Set<ConstraintViolation<Transaction>> violations = VALIDATOR.validate(transaction);

        if (!violations.isEmpty()) {
            String message = violations.iterator().next().getMessage();
            throw new IllegalArgumentException(message);
        }

        return transaction;
    }

    private static boolean parseBoolean(String value) {
        if ("0".equals(value)) {
            return false;
        }
        if ("1".equals(value)) {
            return true;
        }
        throw new IllegalArgumentException("Valor booleano invalido: " + value);
    }
}
