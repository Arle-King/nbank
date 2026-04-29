package org.example.common.extensions;

import org.example.api.configs.Config;
import org.example.common.annotations.ApiVersion;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;

public class ApiVersionExtension implements ExecutionCondition {
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext extensionContext) {
        ApiVersion annotation = extensionContext.getElement()
                .map(el -> el.getAnnotation(ApiVersion.class))
                .orElse(null);

        if (annotation == null) {
            return ConditionEvaluationResult.enabled("Нет ограничений к запуску");
        }

        String currentApi = Config.getProperty("back.version");
        boolean matches = Arrays.stream(annotation.value())
                .anyMatch(api -> api.equals(currentApi));

        if (matches) {
            return ConditionEvaluationResult.enabled("Работаем");
        } else {
            return ConditionEvaluationResult.disabled("Не работаем");
        }
    }
}
