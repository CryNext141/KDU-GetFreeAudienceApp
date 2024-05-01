package com.example.roomify;

//ErrorHandling contains fields for error message and error code and provides methods for getting and setting these values
public class ErrorHandling {
    private String error_message;
    private String errorcode;

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }
}