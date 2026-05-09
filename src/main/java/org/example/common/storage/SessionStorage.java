package org.example.common.storage;

import org.example.BankWidget;
import org.example.api.models.admin.users.CreateUserRequestDTO;
import org.example.api.skelethon.requests.steps.AdminSteps;
import org.example.api.skelethon.requests.steps.UserSteps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SessionStorage {
    private static final SessionStorage INSTANCE = new SessionStorage();

    private final LinkedHashMap<CreateUserRequestDTO, UserSteps> userStepsMap = new LinkedHashMap<>();

    private SessionStorage() {};

    public static void addUsers(List<CreateUserRequestDTO> users) {
        for (CreateUserRequestDTO user : users) {
            INSTANCE.userStepsMap.put(user, new UserSteps(user.getUsername(), user.getPassword()));
        }
    }

    public static CreateUserRequestDTO getUser(int numder) {
        return new ArrayList<>(INSTANCE.userStepsMap.keySet()).get(numder - 1);
    }

    public static CreateUserRequestDTO getUser() {
        return getUser(1);
    }

    public static UserSteps getSteps(int numder) {
        return new ArrayList<>(INSTANCE.userStepsMap.values()).get(numder - 1);
    }

    public static UserSteps getSteps() {
        return getSteps(1);
    }

    public static void clear() {
        INSTANCE.userStepsMap.clear();
    }

    public static void deleteAllUsers() {
        for(CreateUserRequestDTO user : INSTANCE.userStepsMap.keySet()) {
            AdminSteps.deleteUser(user);
        }
    }
}
