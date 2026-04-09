package org.example;

import io.restassured.common.mapper.TypeRef;
import org.example.accounts.models.deposit.DepositRequestDTO;
import org.example.accounts.requests.deposit.DepositRequest;
import org.example.general_models.UserResponseDTO;
import org.example.admin.requests.GetAllUserRequest;
import org.example.general_models.AccountDTO;
import org.example.specs.RequestSpecs;
import org.example.specs.ResponceSpecs;

import java.util.List;

public class BankWidget {

    public static UserResponseDTO getUresById(int userId) {
        return new GetAllUserRequest(
                RequestSpecs.getAdminSpec(),
                ResponceSpecs.requestReturnsOk())
                .getAllUsers()
                .extract()
                .as(new TypeRef<List<UserResponseDTO>>() {}).stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElse(null);
    }

    public static AccountDTO getAccountById(int accountId) {
        return new GetAllUserRequest(
                RequestSpecs.getAdminSpec(),
                ResponceSpecs.requestReturnsOk())
                .getAllUsers()
                .extract()
                .as(new TypeRef<List<UserResponseDTO>>() {}).stream()
                .flatMap(user -> user.getAccounts().stream())
                .filter(acc -> acc.getId() == accountId)
                .findFirst()
                .orElse(null);
    }

    public static void dodepInAccount(String name, String password, int accountId, Double amount) {
        do {
            if (amount > 5001) {
                new DepositRequest(
                        RequestSpecs.getUserSpec(name, password),
                        ResponceSpecs.requestReturnsOk())
                        .post(new DepositRequestDTO(accountId, 5000.0));
            } else {
                new DepositRequest(
                        RequestSpecs.getUserSpec(name, password),
                        ResponceSpecs.requestReturnsOk())
                        .post(new DepositRequestDTO(accountId, amount));
            }
            amount = amount - 5000;

        } while (amount > 0);
    }
}
