package ru.itsjava.dao;

import ru.itsjava.domain.User;

public interface UserDao {
    User findByName (String name);
    User createUser (String name, String password);
    //    void updateUser (String name);
    void deleteUser (String name);

}


