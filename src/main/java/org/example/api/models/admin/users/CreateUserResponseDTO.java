package org.example.api.models.admin.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Getter;
import org.example.api.models.BaseModel;
import org.example.api.models.accoints.accounts.AccountDTO;
import org.example.api.skelethon.enams.Role;

import java.util.List;

@Data
@Getter
public class CreateUserResponseDTO extends BaseModel {

    @JsonProperty("id")
    private int id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("name")
    private String name;

    @JsonProperty("role")
    private Role role;

    @JsonProperty("accounts")
    private List<AccountDTO> accounts;

    private transient String token;

    private transient String unencryptedPassword;
}
