package org.example.api.models.customer.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.example.api.models.BaseModel;
import org.example.api.models.admin.users.CreateUserResponseDTO;

@Data
public class UpdateProfileResponseDTO extends BaseModel {

    @JsonProperty("customer")
    private CreateUserResponseDTO user;

    @JsonProperty("message")
    private String message;

}
