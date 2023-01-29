package org.example.repository;

import org.example.model.Cat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SimpleCatRepository implements CatRepository {
    static String READ_CAT = "SELECT * FROM cats WHERE id=?;";
    static String INSERT_CAT = "INSERT INTO cats (id, name, weight, isAngry) VALUES (?,?,?,?);";
    static String UPDATE_CAT = "UPDATE cats SET name=?, weight=?, isAngry=? WHERE id=?;";
    static String DELETE_CAT = "DELETE FROM cats WHERE id=?;";
    static String CREATE_CATS = "CREATE TABLE cats (id BIGINT PRIMARY KEY, name VARCHAR(45) NOT NULL, weight SMALLINT NOT NULL, isAngry BIT);";
    static String FINDALL_CATS = "SELECT * FROM cats;";


    public SimpleCatRepository() {
        try {
            Connection connection = DBUtils.getConnection();
            Statement stmt = connection.createStatement();
            stmt.execute(CREATE_CATS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean create(Cat element) {
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CAT)) {
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
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(READ_CAT)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
             return new Cat(id, rs.getString("Name"), rs.getInt("Weight"), rs.getBoolean("IsAngry"));

        } catch (SQLException e) {
            return null;
        }
    }


    @Override
    public int update(Long id, Cat element) {

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CAT)) {
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
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CAT)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Cat> findAll() {

        List<Cat> cats = new ArrayList<>();

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FINDALL_CATS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                int weight = rs.getInt("weight");
                boolean isAngry = rs.getBoolean("isAngry");
                cats.add(new Cat(id, name, weight, isAngry));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cats;
    }


}

