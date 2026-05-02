package br.com.zenon.fraud;

import br.com.zenon.fraud.domain.Customer;
import br.com.zenon.fraud.domain.Transaction;
import br.com.zenon.fraud.domain.TransactionType;
import br.com.zenon.fraud.service.TransactionIngestor;
import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class MainClass {

    static void main() throws IOException {
        Customer clienteOrigem1 = new Customer(
                "C1231006815",
                new BigDecimal("170136.0"),
                new BigDecimal("160296.36")
        );

        Customer clienteDestino1 = new Customer(
                "M1979787155",
                new BigDecimal("0.0"),
                new BigDecimal("0.0")
        );

        Transaction transacao1 = new Transaction(
                1,
                1,
                TransactionType.PAYMENT,
                new BigDecimal("9839.64"),
                clienteOrigem1,
                clienteDestino1,
                false,
                false
        );
        Customer clienteOrigem2 = new Customer(
                "C1280323807",
                new BigDecimal("850002.52"),
                new BigDecimal("0.0")
        );

        Customer clienteDestino2 = new Customer(
                "C873221189",
                new BigDecimal("6510099.11"),
                new BigDecimal("7360101.63")
        );

        Transaction transacao2 = new Transaction(
                2,
                1,
                TransactionType.CASH_OUT,
                new BigDecimal("850002.52"),
                clienteOrigem2,
                clienteDestino2,
                true,
                false
        );

        Gson gson = new Gson();
        String transaction1 = gson.toJson(transacao1);
        String transaction2 = gson.toJson(transacao2);

        System.out.println("\n------------- Transações criadas por builder ------------------\n");

        System.out.println("Transaction 1: " + transaction1);
        System.out.println("Transaction 2: " + transaction2);

        System.out.println("\n--------------Transações criadas por arquivo csv -----------------\n");

        List<Transaction> list = TransactionIngestor.ingestor("data/TransactionList.csv");

        list.forEach(transaction -> {
            if(transaction.id() <= 10){
                System.out.println(transaction);
            }
        });

        System.out.println("\n--------------Transações criadas por arquivo csv contendo erros tratados -----------------");

        List<Transaction> listWithError = TransactionIngestor.ingestor("data/TransactionErrorList.csv");

        listWithError.forEach(transaction -> {
            System.out.println(transaction);
        });


    }



}
