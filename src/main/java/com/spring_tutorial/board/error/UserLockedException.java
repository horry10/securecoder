package com.spring_tutorial.board.error;

public class UserLockedException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private String message;

    public UserLockedException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
