package mocktest;

import api.BaseTest;
import org.example.BankWidget;
import org.example.api.models.accoints.accounts.CreateAccountResponseDTO;
import org.example.api.models.accoints.transfer.TransferResponseDTO;
import org.example.api.models.admin.users.CreateUserResponseDTO;
import org.example.api.models.comparison.ModelAssertions;
import org.example.api.skelethon.requests.steps.UserSteps;
import org.example.common.annotations.FraudCheckMock;
import org.example.common.extensions.FraudCheckWireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FraudCheckWireMockExtension.class)
public class TransferWithFraudCheckTest extends BaseTest {

    CreateUserResponseDTO user1;
    CreateUserResponseDTO user2;
    CreateAccountResponseDTO user1Account;
    CreateAccountResponseDTO user2Account;
    TransferResponseDTO transferResponse;
    UserSteps userSteps;

    double transferAmount;

    final static String errorKey = "transfer";
    final static String errorValue = "Invalid transfer: insufficient funds or invalid accounts";

    @BeforeEach
    public void precondition() {
        super.precondition();
        user1 = BankWidget.createUser();
        user2 = BankWidget.createUser();

        userSteps = new UserSteps(user1);

        user1Account = BankWidget.createAccount(user1);
        user2Account = BankWidget.createAccount(user2);

        double depositAmount = 4000.0; //Math.random() * 4999.9 + 0.1;

        userSteps.depositToAccount((long) user1Account.getId(), depositAmount);

        transferAmount = Math.random() * (depositAmount - 0.1) + 0.1;
    }

    @Test
    @FraudCheckMock(
            status = "SUCCESS",
            decision = "APPROVED",
            riskScore = 0.2,
            reason = "Low risk transaction",
            requiresManualReview = false,
            additionalVerificationRequired = false
    )
    public void testTransferWithFraudCheck() {

        transferResponse = userSteps.transferWithFraudCheck(
                (long) user1Account.getId(),
                (long) user2Account.getId(),
                transferAmount
        );

        softAssertions.assertThat(transferResponse).isNotNull();


        TransferResponseDTO expectedResponse = TransferResponseDTO.builder()
                .status("APPROVED")
                .message("Transfer approved and processed immediately")
                .amount(transferAmount)
                .senderAccountId((long) user1Account.getId())
                .receiverAccountId((long) user2Account.getId())
                .fraudRiskScore(0.2)
                .fraudReason("Low risk transaction")
                .requiresManualReview(false)
                .requiresVerification(false)
                .build();

        ModelAssertions.assertThatModels(expectedResponse, transferResponse).match();
    }
}
