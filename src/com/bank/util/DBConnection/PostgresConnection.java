package com.bank.util.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/smartbank_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres@123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
