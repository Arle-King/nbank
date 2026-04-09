package org.example.general_models;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.example.BaseModel;

import java.util.List;

@Data
public class UserResponseDTO extends BaseModel {
    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String userName;

    @SerializedName("password")
    private String password;

    @SerializedName("name")
    private String name;

    @SerializedName("role")
    private String role;

    @SerializedName("accounts")
    private List<AccountDTO> accounts;
}
