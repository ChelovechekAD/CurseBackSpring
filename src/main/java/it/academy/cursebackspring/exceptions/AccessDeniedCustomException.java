package it.academy.cursebackspring.exceptions;


import it.academy.cursebackspring.utilities.Constants;

public class AccessDeniedCustomException extends RuntimeException {

    public AccessDeniedCustomException() {
        super(Constants.ACCESS_DENIED);
    }

    public AccessDeniedCustomException(String e) {
        super(e);
    }

}
