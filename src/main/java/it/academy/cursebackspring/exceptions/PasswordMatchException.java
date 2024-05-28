package it.academy.cursebackspring.exceptions;


import it.academy.cursebackspring.utilities.Constants;

public class PasswordMatchException extends RuntimeException {

    public PasswordMatchException() {
        super(Constants.PASSWORD_MATCH_EXCEPTION);
    }

}
