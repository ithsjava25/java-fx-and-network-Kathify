package com.example;

public class NtfyMessageDto {
    private String user;
    private String message;

    public NtfyMessageDto(String user, String message) {
        this.user = user;
        this.message = message;
    }

    public String getUser() { return user; }
    public String getMessage() { return message; }
}