package ru.itsjava.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import ru.itsjava.dao.UserDao;
import ru.itsjava.domain.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class ClientRunnable implements Runnable, Observer{
    private final Socket socket;
    private final Observable server;
    private final UserDao userDao;
    private User user;

    @SneakyThrows
    @Override
    public void run() {
        BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                if (authorization(socketReader)) {
                    server.addObserver(this);
                    String sockedInputMessage;
                    while ((sockedInputMessage = socketReader.readLine()) != null) {
                        System.out.println(sockedInputMessage);
                        server.notifyObserver(user.getName() + ":" + sockedInputMessage);
                    }
                } else {
                    this.notify("Ошибка авторизации");

                }
            }






    @SneakyThrows
    private boolean authorization(BufferedReader socketReader) {
        String socketInputMessage = socketReader.readLine();
        val loginAndPass = socketInputMessage.substring(6).split(":");
        try {
            user = userDao.findByName(loginAndPass[0]);
        } catch (NoSuchElementException e) {
            return false;
        }

        if (user.getPassword().equals(loginAndPass[1])){
            notify("Вы успешно авторизовались");
            return true;
        }
       return false;
    }

    @SneakyThrows
    @Override
    public void notify(String message) {
        PrintWriter socketWriter = new PrintWriter(socket.getOutputStream());
        socketWriter.println(message);
        socketWriter.flush();
        }
    }

