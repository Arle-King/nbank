package org.example.models.customer.profile;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.example.models.BaseModel;
import org.example.models.admin.users.CreateUserResponseDTO;

@Data
public class UpdateProfileResponseDTO extends BaseModel {

    @SerializedName("customer")
    private CreateUserResponseDTO user;

    @SerializedName("message")
    private String message;

}
