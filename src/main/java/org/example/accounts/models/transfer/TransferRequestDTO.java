package org.example.accounts.models.transfer;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.BaseModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequestDTO extends BaseModel {
    @SerializedName("senderAccountId")
    private int senderAccountId;

    @SerializedName("receiverAccountId")
    private int receiverAccountId;

    @SerializedName("amount")
    private Double amount;
}
