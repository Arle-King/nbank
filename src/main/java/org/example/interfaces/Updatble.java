package org.example.interfaces;

import io.restassured.response.ValidatableResponse;
import org.example.BaseModel;

public abstract interface Updatble<T extends BaseModel>{
    abstract ValidatableResponse update(T model);
}
