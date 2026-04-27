package org.example.api.models.accoints.transactions;

import lombok.Getter;
import org.example.api.models.BaseModel;

import java.util.List;

@Getter
public class TransactionsResponseDTO extends BaseModel {
    private List<TransactionDTO> allTransaction;
}
