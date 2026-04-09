package org.example.accounts.models.deposit;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.BaseModel;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositRequestDTO extends BaseModel {
    @SerializedName("id")
    private int id;

    @SerializedName("balance")
    private Double balance;
}
