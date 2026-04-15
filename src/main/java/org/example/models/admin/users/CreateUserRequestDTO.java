package org.example.models.admin.users;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.generators.GeneratingRule;
import org.example.models.BaseModel;
import org.example.requests.skelethon.enams.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequestDTO extends BaseModel {
    @SerializedName("username")
    @GeneratingRule(regex = "^Username[a-zA-Z0-9._-]{2,7}$")
    private String username;

    @SerializedName("password")
    @GeneratingRule(regex = "^Password#1[a-zA-Z0-9!@#$%^&*()_+]{3,5}$")
    private String password;

    @SerializedName("role")
    @GeneratingRule(regex = "USER")
    private Role role;
}
