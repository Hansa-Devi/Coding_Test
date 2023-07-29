package com.smallworld;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionDataFetcher {


    private List<Transaction> transactions;

    public TransactionDataFetcher(String jsonFilePath) {
        // Step 1: Parse the JSON data
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            transactions = objectMapper.readValue(new File(jsonFilePath), objectMapper.getTypeFactory().constructCollectionType(List.class, Transaction.class));
        } catch (IOException e) {
            e.printStackTrace();
            // For simplicity, we will just print the stack trace here.
            throw new RuntimeException("Error while parsing JSON data: " + e.getMessage());
        }
    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        double totalAmount = 0.0;
        for (Transaction transaction : transactions) {
            totalAmount += transaction.getAmount();
        }
        return totalAmount;

    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        double totalAmount = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getSenderFullName().equals(senderFullName)) {
                totalAmount += transaction.getAmount();
            }
        }
        return totalAmount;
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        double maxAmount = Double.MIN_VALUE;
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > maxAmount) {
                maxAmount = transaction.getAmount();
            }
        }
        return maxAmount;

    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        return transactions.stream()
            .flatMap(transaction -> Stream.of(transaction.getSenderFullName(), transaction.getBeneficiaryFullName()))
            .distinct()
            .count();

    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        for (Transaction transaction : transactions) {
            if ((transaction.getSenderFullName().equals(clientFullName) || transaction.getBeneficiaryFullName().equals(clientFullName))
                    && transaction.getIssueId() != null && !transaction.isIssueSolved()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, List<Transaction>> getTransactionsByBeneficiaryName() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getBeneficiaryFullName));
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        Set<Integer> unsolvedIssueIds = new HashSet<>();
        for (Transaction transaction : transactions) {
            if (transaction.getIssueId() != null && !transaction.isIssueSolved()) {
                unsolvedIssueIds.add(transaction.getIssueId());
            }
        }
        return unsolvedIssueIds;
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        List<String> solvedIssueMessages = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.isIssueSolved() && transaction.getIssueMessage() != null) {
                solvedIssueMessages.add(transaction.getIssueMessage());
            }
        }
        return solvedIssueMessages;

    }

    /**
     * Returns the 3 transactions with highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        return transactions.stream()
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .limit(3)
                .collect(Collectors.toList());

    }

    /**
     * Returns the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getSenderFullName, Collectors.summingDouble(Transaction::getAmount)))
                .entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey);

    }

}
