package org.example.models.authentication.login;

import com.google.gson.annotations.SerializedName;
import org.example.models.BaseModel;

public class LoginResponseDTO extends BaseModel {
    @SerializedName("username")
    private String username;

    @SerializedName("role")
    private String role;
}
