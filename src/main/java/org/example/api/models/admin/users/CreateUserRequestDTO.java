package org.example.api.models.admin.users;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.api.generators.GeneratingRule;
import org.example.api.models.BaseModel;
import org.example.api.skelethon.enams.Role;

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
