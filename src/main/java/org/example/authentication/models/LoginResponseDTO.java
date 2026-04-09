package org.example.authentication.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class LoginResponseDTO {
    @SerializedName("username")
    private String username;

    @SerializedName("role")
    private String role;

    private transient String token;
}
