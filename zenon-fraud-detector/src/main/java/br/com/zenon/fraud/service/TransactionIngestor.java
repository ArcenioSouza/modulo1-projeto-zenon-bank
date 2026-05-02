package br.com.zenon.fraud.service;

import br.com.zenon.fraud.domain.Customer;
import br.com.zenon.fraud.domain.Transaction;
import br.com.zenon.fraud.domain.TransactionType;
import lombok.NoArgsConstructor;

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

public class TransactionIngestor {

    private TransactionIngestor() {
        /* This utility class should not be instantiated */
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
            while ((line = reader.readLine()) != null && transactionsList.size() < 1000) {
                transactionsList.add(toTransaction(line, id));
                id++;
            }
        }

        return transactionsList;
    }

    private static Transaction toTransaction(String line, long id) {
        String[] columns = line.split(",");

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

        return new Transaction(
                id,
                Integer.parseInt(columns[0]),
                TransactionType.fromString(columns[1]),
                new BigDecimal(columns[2]),
                customerOrigem,
                customerDestino,
                "1".equals(columns[9]),
                "1".equals(columns[10])
        );
    }
}
