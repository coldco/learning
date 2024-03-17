package edu.hitwh.exception;

public class TitleNullException extends RuntimeException{
    public TitleNullException() {
    }

    public TitleNullException(String message) {
        super(message);
    }
}
