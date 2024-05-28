package it.academy.cursebackspring.exceptions;


import it.academy.cursebackspring.utilities.Constants;

public class RefreshTokenInvalidException extends RuntimeException {

    public RefreshTokenInvalidException() {
        super(Constants.TOKEN_INVALID);
    }

}
