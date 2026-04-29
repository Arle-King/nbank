package org.example.api.models.accoints.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("id")
    private int id;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("type")
    private String type;

    @JsonProperty("timestamp")
    private String time;

    @JsonProperty("relatedAccountId")
    private int relatedAccountId;
}
