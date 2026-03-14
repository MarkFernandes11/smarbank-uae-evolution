package com.bank.repositories;

import com.bank.exceptions.AccountNotFoundException;
import com.bank.models.Account;
import com.bank.util.IConstant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepository {

    public void saveAccount(Connection connection, Account account) {
        String sql = "INSERT INTO accounts (holder_name, balance) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, account.getAccountHolder());
            statement.setDouble(2, account.getBalance());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new AccountNotFoundException(String.format(IConstant.ACCOUNT_NOT_FOUND, account.getAccountHolder()));
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    account.setId(generatedId);
                    System.out.println("DEBUG: Account saved with ID: " + generatedId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during account creation", e);
        } catch (AccountNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Account> findByName(Connection connection, String name) {
        String sql = "SELECT * from accounts where holder_name = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String accountHolder = resultSet.getString("holder_name");
                double balance = resultSet.getDouble("balance");
                int id = resultSet.getInt("id");
                return Optional.of(new Account(accountHolder, balance, id));
            }
            return Optional.ofNullable(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBalance(Connection connection, int accountId, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, newBalance);
            statement.setInt(2, accountId);

            statement.executeUpdate();
            System.out.println("DEBUG: Account updated successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllUsernames(Connection connection) {
        List<String> userNames = new ArrayList<>();
        String sql = "SELECT holder_name from accounts";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String accountHolder = resultSet.getString("holder_name");
                userNames.add(accountHolder);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userNames;
    }

    public double getBalance(Connection connection, final int id) {
        String sql = "SELECT balance from accounts where id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                return balance;
            }
            return 0.0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
