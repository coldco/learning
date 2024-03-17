package edu.hitwh.exception;

public class UsernameDuplicateException extends RuntimeException{
    public UsernameDuplicateException() {
    }

    public UsernameDuplicateException(String message) {
        super(message);
    }
}
