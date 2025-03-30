package com.example.identityservice.exception;

public enum ErrorCode {
    UNCATEGORIZED( 9999, "Uncategoried Error" ),
    INVALID_KEY( 1000, "Ivalid message key" ),
    USER_EXISTED( 1001, "User already exists" ),
    USERNAME_INVALID( 1003, "Username is invalid" ),
    PASSWORD_INVALID( 1004, "Password is invalid" ),
    USER_NOTEXISTED ( 1005, "User not exists" ),
    UNAUTHENTICATED( 1005, "Unauthenticated" ),
    ;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    private int code;
    private String message;



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
