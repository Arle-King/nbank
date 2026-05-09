package org.example.api.models.accoints.deposit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.api.models.BaseModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepositResponse extends BaseModel {
    private Long id;
    private String accountNumber;
    private double balance;
    private Double depositAmount;
    private Long transactionId;
}
