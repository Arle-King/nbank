package org.example.customer.models.profile;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.BaseModel;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequestDTO extends BaseModel {
    @SerializedName("name")
    private String name;
}
