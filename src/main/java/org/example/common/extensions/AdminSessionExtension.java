package org.example.common.extensions;

import org.example.BankWidget;
import org.example.common.annotations.AdminSession;
import org.example.ui.pages.BasePage;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class AdminSessionExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        AdminSession annotation = extensionContext.getRequiredTestMethod().getAnnotation(AdminSession.class);

        if (annotation != null) {
            BasePage.authAsUser(BankWidget.getAdmin());
        }
    }
}
