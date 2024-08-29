package ru.nand.eurekaclientauth.util;

public class MyUserErrorResponse {
    private String message; // Сообщение ошибки
    private long timestamp; // Отметка времени когда произошла ошибка

    public MyUserErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
