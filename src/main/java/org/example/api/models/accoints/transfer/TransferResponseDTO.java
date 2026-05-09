package org.example.api.models.accoints.transfer;

import lombok.*;
import org.example.api.models.BaseModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferResponseDTO extends BaseModel {
    private String status;
    private String message;
    private Long transactionId;
    private Long senderAccountId;
    private Long receiverAccountId;
    private double amount;
    private double fraudRiskScore;
    private String fraudReason;
    private boolean requiresVerification;
    private boolean requiresManualReview;
}
