package org.example;

import io.restassured.common.mapper.TypeRef;
import org.example.models.accoints.accounts.AccountDTO;
import org.example.models.accoints.deposit.DepositRequestDTO;
import org.example.models.admin.users.CreateUserResponseDTO;
import org.example.requests.skelethon.requests.CrudRequest;
import org.example.requests.skelethon.enams.Endpoint;
import org.example.specs.RequestSpecs;
import org.example.specs.ResponceSpecs;

import java.util.List;

public class BankWidget {
    public static CreateUserResponseDTO getUresById(int userId) {
        return new CrudRequest(
                RequestSpecs.getAdminSpec(),
                Endpoint.GET_ALL_USERS,
                ResponceSpecs.requestReturnsOk())
                .get()
                .extract()
                .as(new TypeRef<List<CreateUserResponseDTO>>() {}).stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElse(null);
    }

    public static AccountDTO getAccountById(int accountId) {
        return new CrudRequest(
                RequestSpecs.getAdminSpec(),
                Endpoint.GET_ALL_USERS,
                ResponceSpecs.requestReturnsOk())
                .get()
                .extract()
                .as(new TypeRef<List<CreateUserResponseDTO>>() {}).stream()
                .flatMap(user -> user.getAccounts().stream())
                .filter(acc -> acc.getId() == accountId)
                .findFirst()
                .orElse(null);
    }

    public static void dodepInAccount(String name, String password, int accountId, Double amount) {
        do {
            if (amount > 5001) {
                new CrudRequest(
                        RequestSpecs.getUserSpec(name, password),
                        Endpoint.DEPOSIT,
                        ResponceSpecs.requestReturnsOk())
                        .post(new DepositRequestDTO(accountId, 5000.0));
            } else {
                new CrudRequest(
                        RequestSpecs.getUserSpec(name, password),
                        Endpoint.DEPOSIT,
                        ResponceSpecs.requestReturnsOk())
                        .post(new DepositRequestDTO(accountId, amount));
            }
            amount = amount - 5000;

        } while (amount > 0);
    }
}
