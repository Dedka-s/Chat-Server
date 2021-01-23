package ru.itsjava.services;

public interface Observer {
    void notify(String message);
    String getLogin();
}
