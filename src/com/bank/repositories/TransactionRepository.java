package com.bank.repositories;

import com.bank.models.Transaction;
import com.bank.util.DBConnection.PostgresConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

}
