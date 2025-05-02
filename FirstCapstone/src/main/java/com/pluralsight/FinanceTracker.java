package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

    public class FinanceTracker {

        private static final String TRANSACTIONS_FILE = "src/main/resources/transactions.csv";

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            ArrayList<Transaction> transactions = loadTransactions();

            while (true) {
                System.out.println("\n--- Finance Tracker ---");
                System.out.println("D) Add Deposit");
                System.out.println("P) Make Payment (Debit)");
                System.out.println("L) Ledger");
                System.out.println("X) Exit");
                System.out.print("Enter selection: ");
                String choice = scanner.nextLine().toUpperCase();

                switch (choice) {
                    case "D":
                        addTransaction(scanner, transactions, true);
                        break;
                    case "P":
                        addTransaction(scanner, transactions, false);
                        break;
                    case "L":
                        ledgerMenu(scanner, transactions);
                        break;
                    case "X":
                        saveTransactions(transactions);
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid selection. Please try again.");
                }
            }
        }

        private static ArrayList<Transaction> loadTransactions() {
            ArrayList<Transaction> transactions = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 5) {
                        transactions.add(new Transaction(
                                parts[0],
                                parts[1],
                                parts[2],
                                parts[3],
                                Double.parseDouble(parts[4])
                        ));
                    }
                }
            } catch (IOException e) {
                System.out.println("No previous transactions found. Starting fresh...");
            }
            return transactions;
        }

        private static void saveTransactions(ArrayList<Transaction> transactions) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE))) {
                for (Transaction t : transactions) {
                    bw.write(t.toCsv());
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error saving transactions.");
            }
        }

        private static void addTransaction(Scanner scanner, ArrayList<Transaction> transactions, boolean isDeposit) {
            System.out.print("Enter description: ");
            String description = scanner.nextLine();
            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine();
            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            if (!isDeposit) {
                amount = -Math.abs(amount); // Payments are negative
            }

            Transaction newTransaction = new Transaction(
                    LocalDate.now().toString(),
                    LocalTime.now().toString(),
                    description,
                    vendor,
                    amount
            );
            transactions.add(newTransaction);
            saveTransactions(transactions);
            System.out.println("Transaction recorded!");
        }

        private static void ledgerMenu(Scanner scanner, ArrayList<Transaction> transactions) {
            while (true) {
                System.out.println("\n--- Ledger ---");
                System.out.println("A) All Entries");
                System.out.println("D) Deposits Only");
                System.out.println("P) Payments Only");
                System.out.println("R) Reports");
                System.out.println("H) Home");
                System.out.print("Enter selection: ");
                String choice = scanner.nextLine().toUpperCase();

                switch (choice) {
                    case "A":
                        displayTransactions(transactions);
                        break;
                    case "D":
                        displayTransactions(transactions, true);
                        break;
                    case "P":
                        displayTransactions(transactions, false);
                        break;
                    case "R":
                        reportsMenu(scanner, transactions);
                        break;
                    case "H":
                        return;
                    default:
                        System.out.println("Invalid selection.");
                }
            }
        }

        private static void displayTransactions(ArrayList<Transaction> transactions) {
            transactions.stream()
                    .sorted(Comparator.comparing(Transaction::getDateTime).reversed())
                    .forEach(System.out::println);
        }

        private static void displayTransactions(ArrayList<Transaction> transactions, boolean depositsOnly) {
            transactions.stream()
                    .filter(t -> depositsOnly ? t.getAmount() > 0 : t.getAmount() < 0)
                    .sorted(Comparator.comparing(Transaction::getDateTime).reversed())
                    .forEach(System.out::println);
        }

        private static void reportsMenu(Scanner scanner, ArrayList<Transaction> transactions) {
            System.out.println("\n--- Reports ---");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
            System.out.print("Enter selection: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "5":
                    System.out.print("Enter vendor name to search: ");
                    String vendor = scanner.nextLine();
                    transactions.stream()
                            .filter(t -> t.getVendor().equalsIgnoreCase(vendor))
                            .forEach(System.out::println);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Report option not implemented yet.");
            }
        }
    }


