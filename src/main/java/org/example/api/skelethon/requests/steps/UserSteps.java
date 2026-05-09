package org.example.api.skelethon.requests.steps;

import org.example.api.models.accoints.accounts.CreateAccountRequestDTO;
import org.example.api.models.accoints.accounts.CreateAccountResponseDTO;
import org.example.api.models.accoints.deposit.DepositRequest;
import org.example.api.models.accoints.deposit.DepositResponse;
import org.example.api.models.accoints.transfer.TransferRequestDTO;
import org.example.api.models.accoints.transfer.TransferResponseDTO;
import org.example.api.models.admin.users.CreateUserResponseDTO;
import org.example.api.skelethon.enams.Endpoint;
import org.example.api.skelethon.requests.ValidatedCrudRequest;
import org.example.api.specs.RequestSpecs;
import org.example.api.specs.ResponceSpecs;
import org.example.common.helpers.StepLogger;

import java.util.ArrayList;
import java.util.List;

public class UserSteps {
    private String username;
    private String password;

    public UserSteps(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserSteps(CreateUserResponseDTO user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    public List<CreateAccountResponseDTO> createAccount(int count) {
        List<CreateAccountResponseDTO> accounts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            accounts.add(new ValidatedCrudRequest<CreateAccountResponseDTO>(
                    RequestSpecs.getUserSpec(username, password),
                    Endpoint.CREATE_ACCOUNT,
                    ResponceSpecs.entityWasCreated())
                    .post(new CreateAccountRequestDTO()));
        }

        return accounts;
    }

    public DepositResponse depositToAccount(Long accountId, double amount) {
        return StepLogger.log("User " + username + " deposits " + amount + " to account " + accountId, () -> {
            DepositRequest depositRequest = DepositRequest.builder()
                    .accountId(accountId)
                    .amount(amount)
                    .description("Test deposit")
                    .build();

            return new ValidatedCrudRequest<DepositResponse>(
                    RequestSpecs.getUserSpec(username, password),
                    Endpoint.ACCOUNT_DEPOSIT,
                    ResponceSpecs.requestReturnsOk())
                    .post(depositRequest);
        });
    }

    public TransferResponseDTO transferWithFraudCheck(Long senderAccountId, Long receiverAccountId, double amount) {
        return StepLogger.log("User " + username + " transfers " + amount + " to " + receiverAccountId + " with fraud check", () -> {
            TransferRequestDTO transferRequest = TransferRequestDTO.builder()
                    .senderAccountId(senderAccountId)
                    .receiverAccountId(receiverAccountId)
                    .amount(amount)
                    .description("Test transfer with fraud check")
                    .build();

            return new ValidatedCrudRequest<TransferResponseDTO>(
                    RequestSpecs.getUserSpec(username, password),
                    Endpoint.TRANSFER_WITH_FRAUD_CHECK,
                    ResponceSpecs.requestReturnsOk())
                    .post(transferRequest);
        });
    }

    public List<CreateAccountResponseDTO> createAccount() {
        return createAccount(1);
    }
}
