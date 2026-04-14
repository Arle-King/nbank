package org.example.models.admin.users;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Getter;
import org.example.models.BaseModel;
import org.example.models.accoints.accounts.AccountDTO;
import org.example.requests.skelethon.enams.Role;

import java.util.List;

@Data
@Getter
public class CreateUserResponseDTO extends BaseModel {

    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("name")
    private String name;

    @SerializedName("role")
    private Role role;

    @SerializedName("accounts")
    private List<AccountDTO> accounts;

    private transient String token;

    private transient String unencryptedPassword;
}
