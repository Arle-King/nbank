package org.example.models.accoints.deposit;

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
public class DepositRequestDTO extends BaseModel {
    @SerializedName("id")
    private int id;

    @SerializedName("balance")
    private Double balance;
}
