package org.example;

import io.restassured.common.mapper.TypeRef;
import org.example.generators.RandomModelGenerator;
import org.example.models.accoints.accounts.AccountDTO;
import org.example.models.accoints.accounts.CreateAccountRequestDTO;
import org.example.models.accoints.accounts.CreateAccountResponseDTO;
import org.example.models.accoints.deposit.DepositRequestDTO;
import org.example.models.admin.users.CreateUserRequestDTO;
import org.example.models.admin.users.CreateUserResponseDTO;
import org.example.requests.skelethon.enams.Endpoint;
import org.example.requests.skelethon.requests.CrudRequest;
import org.example.requests.skelethon.requests.ValidatedCrudRequest;
import org.example.specs.RequestSpecs;
import org.example.specs.ResponceSpecs;

import java.util.List;

public class BankWidget {

    public static List<CreateUserResponseDTO> getAllUsers() {
        return new CrudRequest(
                RequestSpecs.getAdminSpec(),
                Endpoint.GET_ALL_USERS,
                ResponceSpecs.requestReturnsOk())
                .get()
                .extract()
                .as(new TypeRef<List<CreateUserResponseDTO>>() {});
    }


    public static CreateUserResponseDTO getUresById(int userId) {
        return getAllUsers()
                .stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElse(null);
    }



    public static AccountDTO getAccountById(int accountId) {
        return getAllUsers()
                .stream()
                .flatMap(user -> user.getAccounts()
                        .stream())
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

    public static CreateUserResponseDTO getUserByUsername(String username) {
        return getAllUsers().stream()
                .filter(userN -> userN.getUsername().equals(username))
                .findFirst()
                .orElse(null);

    }

    public static void deleteUser(CreateUserResponseDTO user) {
        deleteAllUserAccounts(user);
        new CrudRequest(
                RequestSpecs.getAdminSpec(),
                Endpoint.DELETE_USER,
                ResponceSpecs.requestReturnsOk())
                .delete(user.getId());
    }

    public static void deleteUser(String username) {
        CreateUserResponseDTO user = getAllUsers().stream().filter(userN -> userN.getUsername().equals(username)).findFirst().orElse(null);
        assert user != null;
        deleteAllUserAccounts(user);
        new CrudRequest(
                RequestSpecs.getAdminSpec(),
                Endpoint.DELETE_USER,
                ResponceSpecs.requestReturnsOk())
                .delete(user.getId());
    }

    public static String getCustomName(String username) {
        return getUserByUsername(username).getName();
    }

    public static void deleteAccount(CreateUserResponseDTO user, int accountId) {
        new CrudRequest(
                RequestSpecs.getUserSpec(user.getUsername(), user.getPassword()),
                Endpoint.DELETE_ACCOUNT,
                ResponceSpecs.requestReturnsOk())
                .delete(accountId);
    }

    public static void deleteAllUserAccounts(CreateUserResponseDTO user) {
        CreateUserResponseDTO newUser = getUresById(user.getId());
        for (AccountDTO acc : newUser.getAccounts()) {
            deleteAccount(user, acc.getId());
        }
    }

    public static CreateUserResponseDTO createUser() {
        CreateUserRequestDTO userRequest = RandomModelGenerator.generate(CreateUserRequestDTO.class);
        var user = new ValidatedCrudRequest<CreateUserResponseDTO>(
                RequestSpecs.getAdminSpec(),
                Endpoint.CREATE_USER,
                ResponceSpecs.entityWasCreated())
                .post(userRequest);

        RequestSpecs.getUserSpec(userRequest.getUsername(), userRequest.getPassword());

        return user;
    }

    public static CreateAccountResponseDTO createAccount(String username, String password) {
        return new ValidatedCrudRequest<CreateAccountResponseDTO>(
                RequestSpecs.getUserSpec(username, password),
                Endpoint.CREATE_ACCOUNT,
                ResponceSpecs.entityWasCreated())
                .post(new CreateAccountRequestDTO());
    }

    public static CreateAccountResponseDTO createAccount(CreateUserResponseDTO user) {
        return createAccount(user.getUsername(), user.getPassword());
    }
}
