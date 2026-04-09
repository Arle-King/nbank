package org.example.interfaces;

import io.restassured.response.ValidatableResponse;
import org.example.BaseModel;

public abstract interface Postble<T extends BaseModel> {
    abstract ValidatableResponse post(T model);
}
