package org.example.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.model.Cat;


import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;


public class AdvancedCatRepository implements CatRepository {

    private static String READ_CAT = "SELECT * FROM cats WHERE id=?;";
    private static String INSERT_CAT = "INSERT INTO cats (id, name, weight, isAngry) VALUES (?,?,?,?);";
    private static String UPDATE_CAT = "UPDATE cats SET name=?, weight=?, isAngry=? WHERE id=?;";
    private static String DELETE_CAT = "DELETE FROM cats WHERE id=?;";
    private static String CREATE_CATS = "CREATE TABLE cats (id BIGINT PRIMARY KEY, name VARCHAR(45) NOT NULL, weight SMALLINT NOT NULL, isAngry BIT);";
    private static String FINDALL_CATS = "SELECT * FROM cats;";

    private static Connection connection;


  /*  Задача:
    Описать класс AdvancedCatRepository, реализующий интерфейс CatRepository
    Использовать RowMapper, Connection Pool и PreparedStatement для решения предыдущей задачи
    Все настройки подключения описать в файле application.properties в корне проекта или в папке resources:
    db.url — URL базы данных
    db.driver — имя драйвера */


    public AdvancedCatRepository() {

        String dbPropsPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("database.properties")).getPath();
        Properties dbProps = new Properties();
        try {
            dbProps.load(new FileInputStream(dbPropsPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(dbProps.getProperty("db.driver"));
        config.setJdbcUrl(dbProps.getProperty("db.host"));
        HikariDataSource ds = new HikariDataSource(config);


        try {
            connection = ds.getConnection();
            Statement stmt = connection.createStatement();
            stmt.execute(CREATE_CATS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Function<ResultSet, Cat> catRowMapper = rs -> {
        try {
            return new Cat(
                    rs.getLong("Id"),
                    rs.getString("Name"),
                    rs.getInt("Weight"),
                    rs.getBoolean("isAngry")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    @Override
    public boolean create(Cat element) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CAT)) {
            preparedStatement.setLong(1, element.getId());
            preparedStatement.setString(2, element.getName());
            preparedStatement.setInt(3, element.getWeight());
            preparedStatement.setBoolean(4, element.isAngry());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }

    }


    @Override
    public Cat read(Long id) {
        Cat cat = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(READ_CAT)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                cat = catRowMapper.apply(rs);
            }
        } catch (SQLException e) {
            return null;
        }
        return cat;
    }


    @Override
    public int update(Long id, Cat element) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CAT)) {
            preparedStatement.setString(1, element.getName());
            preparedStatement.setInt(2, element.getWeight());
            preparedStatement.setBoolean(3, element.isAngry());
            preparedStatement.setLong(4, id);
            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            return 0;
        }
    }


    @Override
    public void delete(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CAT)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Cat> findAll() {

        List<Cat> cats = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(FINDALL_CATS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Cat cat = catRowMapper.apply(rs);
                cats.add(cat);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cats;
    }


}

