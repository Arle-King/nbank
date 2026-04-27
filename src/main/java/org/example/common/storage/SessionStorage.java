package org.example.common.storage;

import org.example.api.models.admin.users.CreateUserRequestDTO;
import org.example.api.skelethon.requests.steps.AdminSteps;
import org.example.api.skelethon.requests.steps.UserSteps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SessionStorage {
    private static final ThreadLocal<SessionStorage> INSTANCE = ThreadLocal.withInitial(SessionStorage::new);

    private final LinkedHashMap<CreateUserRequestDTO, UserSteps> userStepsMap = new LinkedHashMap<>();

    private SessionStorage() {};

    public static void addUsers(List<CreateUserRequestDTO> users) {
        for (CreateUserRequestDTO user : users) {
            INSTANCE.get().userStepsMap.put(user, new UserSteps(user.getUsername(), user.getPassword()));
        }
    }

    public static CreateUserRequestDTO getUser(int numder) {
        return new ArrayList<>(INSTANCE.get().userStepsMap.keySet()).get(numder - 1);
    }

    public static CreateUserRequestDTO getUser() {
        return getUser(1);
    }

    public static UserSteps getSteps(int numder) {
        return new ArrayList<>(INSTANCE.get().userStepsMap.values()).get(numder - 1);
    }

    public static UserSteps getSteps() {
        return getSteps(1);
    }

    public static void clear() {
        INSTANCE.get().userStepsMap.clear();
    }

    public static void deleteAllUsers() {
        for(CreateUserRequestDTO user : INSTANCE.get().userStepsMap.keySet()) {
            AdminSteps.deleteUser(user);
        }
    }
}
