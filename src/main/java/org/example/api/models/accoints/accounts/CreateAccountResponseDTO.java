package org.example.api.models.accoints.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.example.api.models.BaseModel;
import org.example.api.models.accoints.transactions.TransactionDTO;

import java.util.List;

@Getter
public class CreateAccountResponseDTO extends BaseModel {
    @JsonProperty("id")
    private int id;

    @JsonProperty("accountNumber")
    private String accountNumber;

    @JsonProperty("balance")
    private Double balance;

    @JsonProperty("transactions")
    private List<TransactionDTO> transactions;
}
