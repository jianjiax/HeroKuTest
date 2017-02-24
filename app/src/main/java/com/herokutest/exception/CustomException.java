package com.herokutest.exception;

/**
 * Created by wzg on 15/12/8.
 */
public class CustomException extends Exception {
    public CustomException(String detailMessage) {
        super(detailMessage);
    }
}
