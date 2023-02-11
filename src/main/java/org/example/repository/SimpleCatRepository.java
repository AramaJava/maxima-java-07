package org.example.repository;

import org.example.model.Cat;

import java.io.*;

import java.net.URL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class SimpleCatRepository implements CatRepository {

    static String READ_CAT = "SELECT * FROM cats WHERE id=?;";
    static String INSERT_CAT = "INSERT INTO cats (id, name, weight, isAngry) VALUES (?,?,?,?);";
    static String UPDATE_CAT = "UPDATE cats SET name=?, weight=?, isAngry=? WHERE id=?;";
    static String DELETE_CAT = "DELETE FROM cats WHERE id=?;";
    static String CREATE_CATS = "CREATE TABLE cats (id BIGINT PRIMARY KEY, name VARCHAR(45) NOT NULL, weight SMALLINT NOT NULL, isAngry BIT);";
    static String FINDALL_CATS = "SELECT * FROM cats;";

    private Connection getConnection() throws IOException {
        String dbURL;
        String dbUsername = "sa";
        String dbPassword = "";

        FileInputStream fis;
        Properties properties = new Properties();

        try {
            URL url = this.getClass()
                    .getClassLoader()
                    .getResource("database.properties");
            assert url != null;
            fis = new FileInputStream(url.getFile());
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

    /*Описать класс SimpleCatRepository для реализации этого интерфейса.
    URL базы данных и имя таблицы задайте в конструкторе класса выбраным Вами способом*/


    public SimpleCatRepository() {
        try {
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            stmt.execute(CREATE_CATS);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean create(Cat element) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CAT)) {
            preparedStatement.setLong(1, element.getId());
            preparedStatement.setString(2, element.getName());
            preparedStatement.setInt(3, element.getWeight());
            preparedStatement.setBoolean(4, element.isAngry());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public Cat read(Long id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(READ_CAT)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            return new Cat(id, rs.getString("Name"), rs.getInt("Weight"), rs.getBoolean("IsAngry"));

        } catch (SQLException e) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public int update(Long id, Cat element) {

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CAT)) {
            preparedStatement.setString(1, element.getName());
            preparedStatement.setInt(2, element.getWeight());
            preparedStatement.setBoolean(3, element.isAngry());
            preparedStatement.setLong(4, id);
            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            return 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Long id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CAT)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Cat> findAll() {

        List<Cat> cats = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FINDALL_CATS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                int weight = rs.getInt("weight");
                boolean isAngry = rs.getBoolean("isAngry");
                cats.add(new Cat(id, name, weight, isAngry));
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        return cats;
    }


}

