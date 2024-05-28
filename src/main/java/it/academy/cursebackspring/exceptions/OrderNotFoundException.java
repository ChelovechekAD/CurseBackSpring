package it.academy.cursebackspring.exceptions;


import it.academy.cursebackspring.utilities.Constants;

public class OrderNotFoundException extends NotFoundException {

    public OrderNotFoundException() {
        super(Constants.ORDER_NOT_FOUND);
    }
}
