package org.example.api.models.customer.profile;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.api.generators.GeneratingRule;
import org.example.api.models.BaseModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileRequestDTO extends BaseModel {
    @SerializedName("name")
    @GeneratingRule(regex = "^[a-zA-Z]{3,4} [a-zA-Z]{3,4}")
    private String name;
}
