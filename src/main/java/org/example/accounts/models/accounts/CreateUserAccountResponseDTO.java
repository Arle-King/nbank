package org.example.accounts.models.accounts;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.example.general_models.TransactionDTO;

import java.util.List;

@Getter
public class CreateUserAccountResponseDTO {
    @SerializedName("id")
    private int id;

    @SerializedName("accountNumber")
    private String accountNumber;

    @SerializedName("balance")
    private Double balance;

    @SerializedName("transactions")
    private List<TransactionDTO> transactions;
}
