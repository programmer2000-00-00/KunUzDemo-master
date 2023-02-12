package com.company.exp;

public class CategoryAlreadyExsistException extends RuntimeException {
    public CategoryAlreadyExsistException(String message) {
        super(message);
    }
}
