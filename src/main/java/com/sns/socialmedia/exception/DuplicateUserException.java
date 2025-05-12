package com.sns.socialmedia.exception;

public class DuplicateUserException extends RuntimeException{
    public DuplicateUserException(String msg) {
        super(msg);
    }
}
