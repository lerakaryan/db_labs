package com.github.lerakaryan.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ClickHouseConfig {
    private static final String URL = "jdbc:clickhouse://localhost:8123/default";

    public static Connection getConnection() throws SQLException {
        Properties properties = new Properties();

        properties.setProperty("user", "user");
        properties.setProperty("password", "admin_admin");

        properties.setProperty("compress", "0");
        properties.setProperty("decompress", "0");

        properties.setProperty("connection_timeout", "10000");

        return DriverManager.getConnection(URL, properties);
    }
}
