package it.academy.cursebackspring.exceptions;


import it.academy.cursebackspring.utilities.Constants;

public class OrderItemExistException extends ExistException {

    public OrderItemExistException() {
        super(Constants.ORDER_ITEM_EXIST);
    }

}
