package it.academy.cursebackspring.exceptions;


import it.academy.cursebackspring.utilities.Constants;

public class ProductUsedInOrdersException extends RuntimeException {
    public ProductUsedInOrdersException() {
        super(Constants.THIS_PRODUCT_ALREADY_USED_IN_ORDER_S);
    }

}
