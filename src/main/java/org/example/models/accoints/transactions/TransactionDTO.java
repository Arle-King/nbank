package org.example.models.accoints.transactions;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {
    @SerializedName("id")
    private int id;

    @SerializedName("amount")
    private Double amount;

    @SerializedName("type")
    private String type;

    @SerializedName("timestamp")
    private String time;

    @SerializedName("relatedAccountId")
    private int relatedAccountId;
}
