package org.example.api.models.authentication.login;

import com.google.gson.annotations.SerializedName;
import org.example.api.models.BaseModel;

public class LoginResponseDTO extends BaseModel {
    @SerializedName("username")
    private String username;

    @SerializedName("role")
    private String role;
}
