package org.example.common.extensions;

import com.codeborne.selenide.Configuration;
import org.example.common.annotations.Browsers;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;

public class BrowsersMatchExtesion implements ExecutionCondition {
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext extensionContext) {
        Browsers annotation = extensionContext.getElement()
                .map(el -> el.getAnnotation(Browsers.class))
                .orElse(null);

        if (annotation == null){
            return ConditionEvaluationResult.enabled("Нет ограничений к браузеру");
        }

        String currentBrowser = Configuration.browser;
        boolean matches = Arrays.stream(annotation.value())
                .anyMatch(browser -> browser.equals(currentBrowser));

        if (matches) {
            return ConditionEvaluationResult.enabled("Текущий браузер удовлетворяет условию: " + currentBrowser);
        } else {
            return ConditionEvaluationResult.disabled("Тест пропущен, так как текущий браузен не удовлетворяет условию");
        }
    }
}
