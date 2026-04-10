package org.example.requests.skelethon.interfaces;

import org.example.models.BaseModel;

public interface CrudEndpointInterface {
    Object post(BaseModel model);
    Object get(int id);
    Object get();
    Object update(int id, BaseModel model);
    Object update(BaseModel model);
    Object delete(int id);
}
