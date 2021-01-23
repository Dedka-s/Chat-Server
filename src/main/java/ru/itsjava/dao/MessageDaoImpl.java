package ru.itsjava.dao;

import ru.itsjava.domain.Message;
import ru.itsjava.services.Properties;

import java.sql.*;

public class MessageDaoImpl implements MessageDao{
    private final static String URL = Properties.getValue("URL");
    private final static String DB_LOGIN = Properties.getValue("DB_LOGIN");
    private final static String DB_PASSWORD = Properties.getValue("DB_PASSWORD");

    @Override
    public void saveMyMessage(Message message) {
        try (Connection connection = DriverManager.getConnection(URL, DB_LOGIN, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT id FROM my_schema_dec_2020.users where name = '" + message.getSender() + "';");
            resultSet.next();
            int idSender = resultSet.getInt("id");

            resultSet = statement.executeQuery("SELECT id FROM my_schema_dec_2020.users where name = '" + message.getRecipient() + "';");
            resultSet.next();
            int idLogin = resultSet.getInt("id");

            statement.executeUpdate("insert into message (textMessage, idSender, idRecipient) values " +
                    "('"+message.getMessage()+"','"+idSender+"','"+idLogin+"');");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void getMessage() {

    }
}
