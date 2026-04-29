package org.example.api.models.customer.profile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.example.api.models.BaseModel;
import org.example.api.models.admin.users.CreateUserResponseDTO;

@Data
public class UpdateProfileResponseDTO extends BaseModel {

    @SerializedName("customer")
    private CreateUserResponseDTO user;

    @SerializedName("message")
    private String message;

}
