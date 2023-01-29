package org.example;


import java.sql.*;


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

    public static final String DB_URL="jdbc:h2:mem:test";
    public static final String DB_DRIVER="org.h2.Driver";

    public static void main( String[] args ) {

        try {


            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL);
            System.out.println( "Соединение с базой данных выполнено!" );

            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE cats (Name VARCHAR(45), Weight INT, isAngry BIT)");
            statement.executeUpdate("INSERT INTO  cats (Name, Weight, isAngry) VALUES ('Мурзик', 2, true)");
            statement.executeUpdate("INSERT INTO  cats (Name, Weight, isAngry) VALUES ('Рамзес', 3, false)");
            statement.executeUpdate("INSERT INTO  cats (Name, Weight, isAngry) VALUES ('Эдуард', 10, true )");
            statement.executeUpdate("INSERT INTO  cats (Name, Weight, isAngry) VALUES ('Эдуард', 7, true)");
//            statement.executeUpdate("ALTER TABLE  cats ADD IsAngry BIT");


            int Rows = statement.executeUpdate("UPDATE cats SET Name='Карл' WHERE name ='Эдуард'");
            System.out.println("Обновлено записей: " + Rows);

            Rows = statement.executeUpdate("DELETE FROM cats WHERE Weight=10");
            System.out.println("Удалено записей: " + Rows);

            ResultSet resultSet = statement.executeQuery("SELECT * from cats");
            while (resultSet.next())
            {
                String name = resultSet.getString("name");
                int weight = resultSet.getInt("weight");
                String isAngry = (resultSet.getBoolean("IsAngry")? "Сердитый" : "Добродушный");
                System.out.printf("%s кот %s весом %d кг.%n", isAngry, name, weight);

            }


            connection.close();
            System.out.println( "Отключение от базы выполнено успешно!" );


        } catch (ClassNotFoundException e) {
             e.printStackTrace();
             System.out.println("Нет драйвера!");
        } catch (SQLException e) {
             e.printStackTrace();
             System.out.println("Ошибка SQL!");
        }
    }
}
