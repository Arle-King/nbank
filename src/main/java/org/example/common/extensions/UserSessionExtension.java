package org.example.common.extensions;

import org.example.api.models.admin.users.CreateUserRequestDTO;
import org.example.api.skelethon.requests.steps.AdminSteps;
import org.example.common.annotations.UserSession;
import org.example.common.storage.SessionStorage;
import org.example.ui.pages.BasePage;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.LinkedList;
import java.util.List;

public class UserSessionExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        UserSession annotation = extensionContext.getRequiredTestMethod().getAnnotation(UserSession.class);

        if (annotation != null) {
            //BasePage.authAsUser(BankWidget.getAdmin());
            int userCount = annotation.value();

            //возможно стоит это удалить
            SessionStorage.clear();

            List<CreateUserRequestDTO> users = new LinkedList<>();

            for (int i = 0; i < userCount; i++) {
                CreateUserRequestDTO user = AdminSteps.createUser();
                users.add(user);
            }

            SessionStorage.addUsers(users);

            int authAsUser = annotation.auth();

            BasePage.authAsUser(SessionStorage.getUser(authAsUser));
        }
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        UserSession annotation = extensionContext.getRequiredTestMethod().getAnnotation(UserSession.class);

        if (annotation != null) {
            SessionStorage.deleteAllUsers();
            SessionStorage.clear();
        }
    }
}
