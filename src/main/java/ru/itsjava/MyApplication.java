package ru.itsjava;
import ru.itsjava.services.ServerImpl;

public class MyApplication {

    public static void main(String[] args) {
       new ServerImpl().start();
    }
}
