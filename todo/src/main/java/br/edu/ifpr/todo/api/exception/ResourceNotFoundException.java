package br.edu.ifpr.todo.api.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String msg) {
        //precios fazer msg amigavel la no outro la
        super(msg);
    }
    }
