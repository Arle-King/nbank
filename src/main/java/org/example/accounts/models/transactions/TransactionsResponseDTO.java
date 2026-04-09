package org.example.accounts.models.transactions;

import lombok.Data;
import org.example.general_models.TransactionDTO;

import java.util.List;

@Data
public class TransactionsResponseDTO {
    private List<TransactionDTO> transactionList;
}
