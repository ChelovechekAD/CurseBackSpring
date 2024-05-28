package it.academy.cursebackspring.exceptions;


import it.academy.cursebackspring.utilities.Constants;

public class OrderItemNotFoundException extends NotFoundException {

    public OrderItemNotFoundException() {
        super(Constants.ORDER_ITEM_NOT_FOUND);
    }

}
