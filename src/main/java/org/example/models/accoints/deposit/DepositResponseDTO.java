package org.example.models.accoints.deposit;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.example.models.BaseModel;
import org.example.models.accoints.transactions.TransactionDTO;

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
