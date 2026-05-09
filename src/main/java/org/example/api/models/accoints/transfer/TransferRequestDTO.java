package org.example.api.models.accoints.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("senderAccountId")
    private Long senderAccountId;

    @JsonProperty("receiverAccountId")
    private Long receiverAccountId;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("description")
    private String description;
}
