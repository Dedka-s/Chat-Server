package ru.itsjava.dao;

import ru.itsjava.domain.User;
import ru.itsjava.services.Properties;

import java.sql.*;
import java.util.NoSuchElementException;

public class UserDaoImpl implements UserDao {
    private final static String URL = Properties.getValue("URL");
    private final static String DB_LOGIN = Properties.getValue("DB_LOGIN");
    private final static String DB_PASSWORD = Properties.getValue("DB_PASSWORD");

    @Override
    public User findByName(String name) {
        try (Connection connection = DriverManager.getConnection(URL, DB_LOGIN, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT name, password FROM my_schema_dec_2020.users where name = '" + name + "';");
            resultSet.next();

            return new User(resultSet.getString("name"), resultSet.getString("password"));

//            while (resultSet.next()){
//                System.out.println(resultSet.getString("name") + ":" + resultSet.getString("password"));
//            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        throw new NoSuchElementException("User not found");
    }
}
