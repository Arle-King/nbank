package org.example.api.skelethon.requests.steps;

import org.example.api.models.accoints.accounts.CreateAccountRequestDTO;
import org.example.api.models.accoints.accounts.CreateAccountResponseDTO;
import org.example.api.skelethon.enams.Endpoint;
import org.example.api.skelethon.requests.ValidatedCrudRequest;
import org.example.api.specs.RequestSpecs;
import org.example.api.specs.ResponceSpecs;

import java.util.ArrayList;
import java.util.List;

public class UserSteps {
    private String username;
    private String password;

    public UserSteps(String username, String password) {
        this.username = username;
        this.password = password;
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

    public List<CreateAccountResponseDTO> createAccount() {
        return createAccount(1);
    }
}
