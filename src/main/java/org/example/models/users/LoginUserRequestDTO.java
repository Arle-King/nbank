package org.example.models.users;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.models.BaseModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserRequestDTO extends BaseModel {
    @SerializedName("username")
    private String userName;

    @SerializedName("password")
    private String password;
}
