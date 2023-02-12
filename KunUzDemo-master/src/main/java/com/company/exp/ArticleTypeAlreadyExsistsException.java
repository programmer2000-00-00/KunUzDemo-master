package com.company.exp;

public class ArticleTypeAlreadyExsistsException extends RuntimeException{
    public ArticleTypeAlreadyExsistsException(String message) {
        super(message);
    }
}
