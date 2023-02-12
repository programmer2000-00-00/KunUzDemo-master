package com.company.controller;

import com.company.exp.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler({
        EmailAlreadyExistsException.class,
            AppBadRequestException.class,
            ItemNotFoundException.class,
            RegionAlreadyExsistException.class,
            TagAlreadyExsistException.class,
            CategoryAlreadyExsistException.class,
            PasswordOrEmailWrongException.class,
            ArticleTypeAlreadyExsistsException.class,
            ItemAlreadyExistsException.class
    })
    public ResponseEntity<?> handlerExc(RuntimeException runtimeException) {
        return ResponseEntity.badRequest().body(runtimeException.getMessage());
    }

    @ExceptionHandler({AppForbiddenException.class})
    public ResponseEntity<?> handleForbidden(RuntimeException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler({TokenNotValidException.class})
    public ResponseEntity<?> handler(TokenNotValidException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
