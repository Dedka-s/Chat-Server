package ru.itsjava.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
@Getter
@ToString
@RequiredArgsConstructor
public class User {
    private final String name;
    private final String password;
}
