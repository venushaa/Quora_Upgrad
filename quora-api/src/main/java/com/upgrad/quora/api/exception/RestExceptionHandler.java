package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.AnswerDeleteResponse;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(InvalidQuestionException .class)
    public ResponseEntity<AnswerResponse> invalidQuestionException (InvalidQuestionException exc, WebRequest request) {
        return new ResponseEntity<AnswerResponse>(
                new AnswerResponse().id(exc.getCode()).status(exc.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AnswerNotFoundException.class)
    public ResponseEntity<AnswerResponse> answerNotFoundException ( AnswerNotFoundException exc, WebRequest request) {
        return new ResponseEntity<AnswerResponse>(
                new AnswerResponse().id(exc.getCode()).status(exc.getErrorMessage()),HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<AnswerResponse> authorizationFailedException ( AuthorizationFailedException exc, WebRequest request) {
        return new ResponseEntity<AnswerResponse>(
                new AnswerResponse().id(exc.getCode()).status(exc.getErrorMessage()), HttpStatus.UNAUTHORIZED);
    }


}
