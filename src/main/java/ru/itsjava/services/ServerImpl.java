package ru.itsjava.services;

import lombok.SneakyThrows;
import ru.itsjava.dao.MessageDaoImpl;
import ru.itsjava.dao.UserDao;
import ru.itsjava.dao.UserDaoImpl;
import ru.itsjava.domain.Message;
import ru.itsjava.dao.MessageDao;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ServerImpl implements Server, Observable{
    private final static int PORT = Integer.parseInt(Properties.getValue("PORT"));
    private final List<Observer> observerList = new ArrayList<>();


    @SneakyThrows
    @Override
    public void start() {
            System.out.println("==SERVER STARTS==");
            ServerSocket serverSocket = new ServerSocket(PORT);
            UserDao userDao = new UserDaoImpl();


            while (true) {
                Socket socket = serverSocket.accept();

                if (!(socket==null)) {
                    System.out.println("Client connected");
                    ClientRunnable clientRunnable = new ClientRunnable(socket, this, userDao);
                    Thread clientThread = new Thread(clientRunnable);
                    clientThread.start();
                }
            }
        }

    @Override
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void deleteObserver(Observer observer) {
        observerList.remove(observer);
    }



    @Override
    public void notifyObserver(String message) {
        for (Observer observer:observerList) {
            observer.notify(message);
        }

    }

    @Override
    public boolean availabilityOfObserver(String message, String recipient, String sender) {
        for (Observer observer:observerList) {
            if (observer.getLogin().equalsIgnoreCase(recipient)){
                observer.notify("Лично от " + sender + ":" + message);
                MessageDao messageDao = new MessageDaoImpl();
                messageDao.saveMyMessage(new Message(message,recipient, sender));
                return true;
            }
        }
        return false;
    }

    }


