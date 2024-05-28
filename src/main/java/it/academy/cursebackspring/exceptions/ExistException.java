package it.academy.cursebackspring.exceptions;


import it.academy.cursebackspring.utilities.Constants;

public class ExistException extends RuntimeException {

    public ExistException(String e) {
        super(e);
    }

    public ExistException() {
        super(Constants.EXIST_EXCEPTION_MESSAGE);
    }

}
