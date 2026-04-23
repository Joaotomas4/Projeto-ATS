package org.spotifumtp37.exceptions;

public class SubscriptionDoesNotAllowException extends Exception {
    public SubscriptionDoesNotAllowException(String message) {
        super(message);
    }
}
