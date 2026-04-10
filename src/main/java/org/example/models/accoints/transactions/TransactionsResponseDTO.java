package org.example.models.accoints.transactions;

import lombok.Getter;
import org.example.models.BaseModel;

import java.util.List;

@Getter
public class TransactionsResponseDTO extends BaseModel {
    private List<TransactionDTO> allTransaction;
}
