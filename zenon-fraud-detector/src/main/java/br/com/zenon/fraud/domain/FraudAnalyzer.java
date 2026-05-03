package br.com.zenon.fraud.domain;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FraudAnalyzer {

    public static void fraudReport(List<Transaction> transactionList){

        System.out.println("\n--------------Transações criadas por arquivo csv contendo fraude -----------------");

        //Todas as fraudes
        List<Transaction> fraudList = transactionList.stream()
                .filter(Transaction::isFraud).toList();

        //As 3 fraudes de maior valor
        List<BigDecimal> maxValueFraudTransaction = fraudList.stream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .limit(3)
                .map(transaction -> {
                    return transaction.amount().setScale(2);
                })
                .toList();


        //5 clientes com maior numero de fraudes
        List<Customer> customerFraud = fraudList.stream()
                .collect(Collectors.groupingBy(Transaction::customerOrigem, Collectors.counting()))
                .entrySet().stream().sorted(Map.Entry.<Customer, Long>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();

        List<String> customerNames = customerFraud.stream().map(Customer::getName).toList();

        BigDecimal totalFraudsAmount = fraudList.stream()
                .map(Transaction::amount).reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<TransactionType, Long> fraudsByType = fraudList.stream()
                .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()));


        System.out.println("1. Total de Fraudes: " + fraudList.size());
        System.out.println("2. Top 3 fraudes de maior valor: " + maxValueFraudTransaction);
        System.out.println("3. Top 5 Clientes suspeitos: " + customerNames);
        System.out.println("4. Total de prejuizo: " + totalFraudsAmount);
        System.out.println("5. Fraude por tipo de transação: " + fraudsByType);

    }
}
