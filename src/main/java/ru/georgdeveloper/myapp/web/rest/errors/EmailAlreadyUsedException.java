package ru.georgdeveloper.myapp.web.rest.errors;

@SuppressWarnings("java:S110") // Дерево наследования классов не должно быть слишком глубоким
public class EmailAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Электронная почта уже используется!", "userManagement", "emailexists");
    }
}
