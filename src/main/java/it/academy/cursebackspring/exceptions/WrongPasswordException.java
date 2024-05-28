package it.academy.cursebackspring.exceptions;


import it.academy.cursebackspring.utilities.Constants;

public class WrongPasswordException extends RuntimeException {

    public WrongPasswordException() {
        super(Constants.WRONG_PASSWORD);
    }

}
