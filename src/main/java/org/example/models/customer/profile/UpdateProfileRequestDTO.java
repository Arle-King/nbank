package org.example.models.customer.profile;

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
public class UpdateProfileRequestDTO extends BaseModel {
    @SerializedName("name")
    private String name;
}
