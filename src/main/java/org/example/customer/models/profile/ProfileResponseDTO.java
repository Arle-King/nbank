package org.example.customer.models.profile;

import com.google.gson.annotations.SerializedName;
import org.example.general_models.AccountDTO;

import java.util.List;

public class ProfileResponseDTO {
    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("name")
    private String name;

    @SerializedName("role")
    private String role;

    @SerializedName("accounts")
    private List<AccountDTO> accounts;
}
