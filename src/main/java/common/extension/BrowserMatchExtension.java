package common.extension;

import api.configs.Config;
import common.annotation.Browsers;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;

public class BrowserMatchExtension implements ExecutionCondition {
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        Browsers annotation = context.getElement()
                .map(el -> el.getAnnotation(Browsers.class))
                .orElse(null);

        if (annotation == null) {
            return ConditionEvaluationResult.enabled("Нет ограничений по браузеру");
        }

        String currentBrowser = Config.getProperty("browser");
        boolean matches = Arrays.stream(annotation.value())
                .anyMatch(browser -> browser.equals(currentBrowser));

        if (matches) {
            return ConditionEvaluationResult.enabled("Браузер соответствует требованиям: " + currentBrowser);
        } else {
            return ConditionEvaluationResult.disabled("Браузер не соответствует требованиям. Текущий: " + currentBrowser
                    +  " не находится в списке допустимых браузеров для теста: " + Arrays.toString(annotation.value()));
        }
    }
}
