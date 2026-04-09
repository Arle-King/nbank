package org.example.interfaces;

import io.restassured.response.ValidatableResponse;

public abstract interface Getble {
    abstract ValidatableResponse get(int id);
}
