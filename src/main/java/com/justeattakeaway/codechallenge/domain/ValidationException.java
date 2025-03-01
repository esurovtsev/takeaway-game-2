package com.justeattakeaway.codechallenge.domain;

/***
 * Represents all validation exceptions in the App. For simplicity reasons we do not expand validation exception to
 * many classes, describing every single exception in details, but for real world app it could be a good idea to do
 * this, so we could track and analise what kind of validation failed and what were their conditions.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
