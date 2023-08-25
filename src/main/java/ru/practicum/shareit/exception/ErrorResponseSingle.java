package ru.practicum.shareit.exception;

public class ErrorResponseSingle {

    private String error;




    public ErrorResponseSingle(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }


}
