package ru.itsjava.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Message {
    private final String message;
    private final String recipient;
    private final String sender;
}
