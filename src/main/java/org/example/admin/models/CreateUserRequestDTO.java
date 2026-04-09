package org.example.admin.models;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.example.BaseModel;
import org.example.UserRole;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequestDTO extends BaseModel {
    @SerializedName("username")
    private String userName;

    @SerializedName("password")
    private String password;

    @SerializedName("role")
    private UserRole role;
}
