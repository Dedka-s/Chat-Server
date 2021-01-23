package ru.itsjava.dao;

import ru.itsjava.domain.Message;

public interface MessageDao {
    void saveMyMessage(Message message);
    void getMessage();
}
