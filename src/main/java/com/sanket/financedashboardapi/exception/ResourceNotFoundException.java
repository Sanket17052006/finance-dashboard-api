package com.sanket.financedashboardapi.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String m){
        super(m);
    }
}
