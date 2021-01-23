package ru.itsjava.services;

public interface Observable {
    void addObserver (Observer observer);

    void deleteObserver(Observer observer);

    void notifyObserver(String message);

    boolean availabilityOfObserver (String message, String login, String sender);


}
