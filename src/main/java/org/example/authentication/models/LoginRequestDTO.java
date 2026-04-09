package org.example.authentication.models;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.example.BaseModel;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDTO extends BaseModel {
    @SerializedName("username")
    private String userName;

    @SerializedName("password")
    private String password;
}
