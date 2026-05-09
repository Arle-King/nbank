package org.example.api.skelethon.interfaces;

import org.example.api.models.BaseModel;

public interface CrudEndpointInterface {
    Object post(BaseModel model);
    Object get(int... id);
    Object get();
    Object update(BaseModel model, int... id);
    Object update(BaseModel model);
    Object delete(int... id);
}
