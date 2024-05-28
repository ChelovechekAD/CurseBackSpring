package it.academy.cursebackspring.exceptions;


import it.academy.cursebackspring.utilities.Constants;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super(Constants.UNAUTHORIZED);
    }

    public UnauthorizedException(String e) {
        super(e);
    }

}
