package com.example.exceptions;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(String role) {
        super("Role " + role + " not found in DB!");
    }
}
