package ru.georgdeveloper.myapp.service;

public class EmailAlreadyUsedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        super("Электронная почта уже используется!");
    }
}
