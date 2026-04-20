package org.example.api.skelethon.requests;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.api.models.BaseModel;
import org.example.api.skelethon.enams.Endpoint;
import org.example.api.skelethon.interfaces.CrudEndpointInterface;

public class ValidatedCrudRequest<M extends BaseModel> extends HttpRequest implements CrudEndpointInterface {
    private CrudRequest crudRequest;

    public ValidatedCrudRequest(RequestSpecification requestSpecification, Endpoint endpoint, ResponseSpecification responseSpecification) {
        super(requestSpecification, endpoint, responseSpecification);
        this.crudRequest = new CrudRequest(requestSpecification, endpoint, responseSpecification);
    }

    @Override
    public M post(BaseModel model) {
        return (M) crudRequest.post(model).extract().as(endpoint.getResponseModel());
    }

    @Override
    public Object get(int... id) {
        return null;
    }

    @Override
    public M get() {
        return (M) crudRequest.get().extract().as(endpoint.getResponseModel());
    }

    @Override
    public Object update(BaseModel model, int... id) {
        return null;
    }

    @Override
    public M update(BaseModel model) {
        return (M) crudRequest.update(model).extract().as(endpoint.getResponseModel());
    }

    @Override
    public Object delete(int... id) {
        return null;
    }
}
