package edu.hitwh.exception;

public class GroupDuplicateException extends RuntimeException{
    public GroupDuplicateException() {
    }

    public GroupDuplicateException(String message) {
        super(message);
    }
}
