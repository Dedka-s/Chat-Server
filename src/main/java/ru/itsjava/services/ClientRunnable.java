package ru.itsjava.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import ru.itsjava.dao.MessageDao;
import ru.itsjava.dao.MessageDaoImpl;
import ru.itsjava.dao.UserDao;
import ru.itsjava.domain.Message;
import ru.itsjava.domain.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class ClientRunnable implements Runnable, Observer {
    private final Socket socket;
    private final Observable server;
    private final UserDao userDao;
    private User user;


    @SneakyThrows
    @Override
    public void run() {
        BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        MessageDao messageDao = new MessageDaoImpl();
        String socketInputMessage = socketReader.readLine();

        if (socketInputMessage.equals("REG")) {
            while (!registration(socketReader)) ;

        } else {
            while (!authorization(socketReader)) ;


            server.addObserver(this);
            String sockedInputMessage;
            try {
                while ((sockedInputMessage = socketReader.readLine()) != null) {
                    System.out.println(sockedInputMessage);

                    if (sockedInputMessage.charAt(0) == '/'){
                        instructions(sockedInputMessage);
                    } else {
                        val login = sockedInputMessage.split(":");
                        if (login.length > 1) {
                            if (!server.availabilityOfObserver(login[1], login[0], user.getName())) {
                                notify("нет такого пользователя");
                            }
                        } else {

                            messageDao.saveMyMessage(new Message(sockedInputMessage, "Общий чат", user.getName()));
                            server.notifyObserver(user.getName() + ":" + sockedInputMessage);
                        }
                    }



                }
            } catch (SocketException e) {
                System.out.println(user.getName() + ":" + "Client disconnection");
                server.deleteObserver(this);
            }
        }

    }

    private void instructions(String instruction) {
        if (instruction.equals("/delete profile")){
            userDao.deleteUser(user.getName());
        }
//        else if (instruction.equals("/edit profile")){
//
//        } else  if (instruction.equals("/get message")){
//
//        }
    }

    @SneakyThrows
    private boolean authorization(BufferedReader socketReader) {
        String socketInputMessage = socketReader.readLine();
        val loginAndPass = socketInputMessage.substring(6).split(":");
        try {
            user = userDao.findByName(loginAndPass[0]);
        } catch (NoSuchElementException e) {
            notify("Ошибка авторизации");
            return authorization(socketReader);
        }

        if (user.getPassword().equals(loginAndPass[1])) {
            notify("Вы успешно авторизовались");
            return true;
        }
        notify("Ошибка авторизации");
        return authorization(socketReader);
    }


    @SneakyThrows
    @Override
    public void notify(String message) {
        PrintWriter socketWriter = new PrintWriter(socket.getOutputStream());
        socketWriter.println(message);
        socketWriter.flush();
    }

    public String getLogin(){
        return user.getName();
    }

    @SneakyThrows
    private boolean registration(BufferedReader socketReader) {
        String socketInputMessage = socketReader.readLine();
        val loginAndPass = socketInputMessage.substring(6).split(":");
        try {
            user = userDao.findByName(loginAndPass[0]);
            notify("Такой пользователь уже есть");
            return registration(socketReader);
        } catch (NoSuchElementException e) {
            user = userDao.createUser(loginAndPass[0],loginAndPass[1]);
            notify("Вы успешно авторизовались");
            return true;
        }
    }
    }
