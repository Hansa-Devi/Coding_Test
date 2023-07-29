package com.smallworld;

import java.util.Optional;

public class Main {

        public static void main(String[] args) {

            String jsonFilePath = "transactions.json"; //  path to the JSON file
            TransactionDataFetcher dataFetcher = new TransactionDataFetcher(jsonFilePath);

            // Test all the methods


//            Method # 1 :Returns the sum of the amounts of all transactions

            double totalTransactionAmount = dataFetcher.getTotalTransactionAmount();
            System.out.println("Total Transaction Amount: " + totalTransactionAmount);

//           Method # 2 :Returns the sum of the amounts of all transactions sent by the specified client

            double totalAmountSentByBillyKimberby = dataFetcher.getTotalTransactionAmountSentBy("Billy Kimber");
            System.out.println("Total Amount Sent by Billy Kimber: " + totalAmountSentByBillyKimberby);

//            Method # 3 :Returns the highest transaction amount
            double maxTransactionAmount = dataFetcher.getMaxTransactionAmount();
            System.out.println("Max Transaction Amount: " + maxTransactionAmount);

//            Method # 4 : Counts the number of unique clients that sent or received a transaction
            long uniqueClientsCount = dataFetcher.countUniqueClients();
            System.out.println("Unique Clients Count: " + uniqueClientsCount);

//            Method # 5 :  Returns whether a client (sender or beneficiary) has at least one transaction
//            with a compliance issue that has not been solved
            boolean hasOpenIssuesForAuntPollyby = dataFetcher.hasOpenComplianceIssues("Tom Aunt Polly");
            System.out.println("Has Open Issues for Aunt Polly: " + hasOpenIssuesForAuntPollyby);

//             Method # 6 : Returns all transactions indexed by beneficiary name
            System.out.println("Transactions Indexed by Beneficiary Name:");
            dataFetcher.getTransactionsByBeneficiaryName().forEach((beneficiary, transactions) -> {
                System.out.println("Beneficiary: " + beneficiary);
                for (Transaction transaction : transactions) {
                    System.out.println(" - MTN: " + transaction.getMtn() + ", Amount: " + transaction.getAmount());
                }
            });

//             Method # 7 : Returns the identifiers of all open compliance issues
            System.out.println("Unsolved Issue IDs: " + dataFetcher.getUnsolvedIssueIds());

//             Method # 8 : Returns a list of all solved issue messages
            System.out.println("Solved Issue Messages: " + dataFetcher.getAllSolvedIssueMessages());

//             Method # 9 : Returns the 3 transactions with the highest amount sorted by amount descending
            System.out.println("Top 3 Transactions by Amount:");
            dataFetcher.getTop3TransactionsByAmount().forEach(transaction -> {
                System.out.println(" - MTN: " + transaction.getMtn() + ", Amount: " + transaction.getAmount());
            });

//             Method # 10 : Returns the sender with the most total sent amount
            Optional<String> topSender = dataFetcher.getTopSender();
            System.out.println("Top Sender: " + topSender);
        }
    }


