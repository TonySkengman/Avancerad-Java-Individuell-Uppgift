package me.code.repositories;

import me.code.models.Transaction;
import me.code.models.TransactionType;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileTransactionRepository implements ITransactionRepository {

    private static final String EXTENSION = ".txt";

    @Override
    public Transaction findById(UUID transactionId) throws Exception {
        String fileName = getFileName(transactionId);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine();
            String amountString = reader.readLine();
            String typeString = reader.readLine();
            String dateString = reader.readLine();

            TransactionType type = TransactionType.valueOf(typeString);
            LocalDate date = LocalDate.parse(dateString);
            double amount = Double.parseDouble(amountString);

            return new Transaction(transactionId, amount, date, type);
        }
    }

    @Override
    public List<Transaction> findAll() throws Exception {
        ArrayList<Transaction> transactions = new ArrayList<>();

        File directory = new File("./");
        File[] transactionFiles = directory.listFiles();
        if (transactionFiles == null) {
            return transactions;
        }

        for (File transactionFile : transactionFiles) {
            String name = transactionFile.getName();

            if (!name.endsWith(EXTENSION)) {
                continue;
            }

            String fileName = name.substring(0, name.length() - EXTENSION.length());

            UUID transactionId;
            try {
                transactionId = UUID.fromString(fileName);
            } catch (IllegalArgumentException ignored) {
                continue;
            }

            Transaction transaction = findById(transactionId);
            transactions.add(transaction);
        }

        return transactions;
    }

    @Override
    public void save(Transaction transaction) throws Exception {
        String fileName = getFileName(transaction.getId());

        try (BufferedWriter stream = new BufferedWriter(new FileWriter(fileName))) {
            String date = transaction.getDate().toString();

            stream
                    .append(transaction.getId().toString())
                    .append("\n")
                    .append(String.valueOf(transaction.getAmount()))
                    .append("\n")
                    .append(transaction.getType().toString())
                    .append("\n")
                    .append(date);
        }
    }

    @Override
    public void delete(UUID transactionId) throws Exception {
        String fileName = getFileName(transactionId);

        File file = new File(fileName);
        boolean ignored = file.delete();
    }

    private static String getFileName(UUID transactionId) {
        return transactionId.toString() + EXTENSION;
    }
}