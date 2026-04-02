package com.sanket.financedashboardapi.exception;

public class UnauthorizedActionException extends RuntimeException{
    public UnauthorizedActionException(String m){
        super(m);
    }
}
