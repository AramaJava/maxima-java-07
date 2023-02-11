package org.example;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.model.Cat;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.sql.*;
import java.util.function.Function;


/* Задача:
   Описать интерфейс BaseRepository
   для реализации репозитория котов:
   CRUD-операции + выборка данных с методами:

    boolean create(T element);
    T read(I id);
    int update(I id, T element);
    void delete(I id);
    List<T> findAll();  //search(), get.. select

    Описать класс SimpleCatRepository для реализации
    этого интерфейса.

    URL базы данных и имя таблицы задайте
    в конструкторе класса выбраным Вами способом
 */

public class App
{

 /*   public static final String DB_URL="jdbc:h2:mem:test";
    public static final String DB_DRIVER="org.h2.Driver";
*/
    public static void main( String[] args ) {
        try {
            String dbPropsPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("database.properties")).getPath();

            Properties dbProps = new Properties();
            dbProps.load(new FileInputStream(dbPropsPath));


            HikariConfig config = new HikariConfig();
            config.setDriverClassName(dbProps.getProperty("db.driver"));
            config.setJdbcUrl(dbProps.getProperty("db.host"));

            HikariDataSource ds = new HikariDataSource(config);
            Connection connection = ds.getConnection();

            System.out.println( "Соединение с базой данных выполнено!" );

            Function <ResultSet, Cat> catRowMapper = rs -> {
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


            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE cats (Id BIGINT, Name VARCHAR(45), Weight INT, isAngry BIT)");
            statement.executeUpdate("INSERT INTO  cats (Id, Name, Weight, isAngry) VALUES (1L, 'Мурзик', 2, true)");
            statement.executeUpdate("INSERT INTO  cats (Id, Name, Weight, isAngry) VALUES (2L, 'Рамзес', 3, false)");
            statement.executeUpdate("INSERT INTO  cats (Id, Name, Weight, isAngry) VALUES (3L, 'Эдуард', 10, true )");
            statement.executeUpdate("INSERT INTO  cats (Id, Name, Weight, isAngry) VALUES (4L, 'Эдуард', 7, false)");
//            statement.executeUpdate("ALTER TABLE  cats ADD IsAngry BIT");


            int Rows = statement.executeUpdate("UPDATE cats SET Name='Карл' WHERE name ='Эдуард'");
            System.out.println("Обновлено записей: " + Rows);

            Rows = statement.executeUpdate("DELETE FROM cats WHERE Weight=10");
            System.out.println("Удалено записей: " + Rows);

            ResultSet resultSet = statement.executeQuery("SELECT * from cats");
            while (resultSet.next())
            {
                Cat cat = catRowMapper.apply(resultSet);

            //    String name = resultSet.getString("name");
            //    int weight = resultSet.getInt("weight");
                String isAngry = (cat.isAngry()? "Сердитый" : "Добродушный");
                System.out.printf("%s кот %s весом %d кг.%n", cat.isAngry(), cat.getName(), cat.getWeight());

            }

            connection.close();
            System.out.println( "Отключение от базы выполнено успешно!" );



        } catch (SQLException e) {
             e.printStackTrace();
             System.out.println("Ошибка SQL!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
