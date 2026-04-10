package org.example.models.authentication.login;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.models.BaseModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO extends BaseModel {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;
}
