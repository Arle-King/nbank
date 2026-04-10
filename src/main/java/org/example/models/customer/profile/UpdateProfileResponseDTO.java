package org.example.models.customer.profile;

import com.google.gson.annotations.SerializedName;
import org.example.models.BaseModel;
import org.example.models.accoints.accounts.AccountDTO;

import java.util.List;

public class UpdateProfileResponseDTO extends BaseModel {
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
