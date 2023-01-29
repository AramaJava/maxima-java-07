package org.example.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtils {

    public static Connection getConnection() {
        String dbURL;
        String dbUsername = "sa";
        String dbPassword = "";

        FileInputStream fis;

        Properties properties = new Properties();
        try {
            fis = new FileInputStream("src/main/resources/database.properties");
            properties.load(fis);
            dbURL = properties.getProperty("db.host");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Connection connection;

        try {
            connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
