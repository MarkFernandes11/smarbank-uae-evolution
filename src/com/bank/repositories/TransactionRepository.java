package com.bank.repositories;

import com.bank.models.Transaction;
import com.bank.util.DBConnection.PostgresConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    public void saveTransaction(int accountId, Transaction txn) {
        String sql = "INSERT INTO transactions (account_id, amount, transaction_type) VALUES (?, ?, ?)";

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, accountId);
            statement.setDouble(2, txn.amount());
            statement.setString(3, txn.transactionType());

            statement.executeUpdate();
            System.out.println("Transaction Added");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Transaction> getTransactions(int accountId) {
        String sql = "SELECT * FROM transactions WHERE account_id = ?";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, accountId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                double amount = resultSet.getDouble("amount");
                String transactionType = resultSet.getString("transaction_type");
//                double amount = resultSet.getDouble("amount");
                Transaction transaction = new Transaction(amount, transactionType, null);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }
}
