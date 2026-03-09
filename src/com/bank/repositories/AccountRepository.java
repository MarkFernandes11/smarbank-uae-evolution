package com.bank.repositories;

import com.bank.models.Account;
import com.bank.util.DBConnection.PostgresConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AccountRepository {

    public void saveAccount(Account account) {
        String sql = "INSERT INTO accounts (holder_name, balance) VALUES (?, ?)";

        try (Connection connection  = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, account.getAccountHolder());
            statement.setDouble(2, account.getBalance());

            statement.executeUpdate();
            System.out.println("DEBUG: Account saved to database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Account> findByName(String name) {
        String sql = "SELECT * from accounts where holder_name = ?";

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String accountHolder = resultSet.getString("holder_name");
                double balance = resultSet.getDouble("balance");
                return Optional.of(new Account(accountHolder, balance));
            }
            return Optional.of(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBalance(String name, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE holder_name = ?";

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, newBalance);
            statement.setString(2, name);

            statement.executeUpdate();
            System.out.println("DEBUG: Account updated successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
