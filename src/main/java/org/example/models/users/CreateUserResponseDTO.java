package org.example.models.users;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.models.BaseModel;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserResponseDTO extends BaseModel {
    @SerializedName("id")
    private String id;

    @SerializedName("username")
    private String userName;

    @SerializedName("password")
    private String password;

    @SerializedName("name")
    private String name;

    @SerializedName("role")
    private String role;

    @SerializedName("accounts")
    private List<String> accounts;
}
