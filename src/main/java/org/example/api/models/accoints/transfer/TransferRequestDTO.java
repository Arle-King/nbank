package org.example.api.models.accoints.transfer;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.api.models.BaseModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequestDTO extends BaseModel {
    @SerializedName("senderAccountId")
    private int senderAccountId;

    @SerializedName("receiverAccountId")
    private int receiverAccountId;

    @SerializedName("amount")
    private Double amount;
}
