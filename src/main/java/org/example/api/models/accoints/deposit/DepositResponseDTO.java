package org.example.api.models.accoints.deposit;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.example.api.models.BaseModel;
import org.example.api.models.accoints.transactions.TransactionDTO;

import java.util.List;

@Getter
public class DepositResponseDTO extends BaseModel {
    @SerializedName("id")
    private int id;

    @SerializedName("accountNumber")
    private String accountNumber;

    @SerializedName("balance")
    private Double balance;

    @SerializedName("transactions")
    private List<TransactionDTO> transactions;
}
