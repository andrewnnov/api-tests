package common.extensions;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.HashMap;
import java.util.Map;

public class TimingExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private Map<String, Long> startTimes = new HashMap<>();

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        String testName = context.getRequiredTestClass().getPackageName() + "." +
                context.getDisplayName();
        long startTime = System.currentTimeMillis();
        startTimes.put(testName, startTime);
        System.out.println("Thread " + Thread.currentThread().getName() +
                " started testName: " + testName + " at " + startTime + " ms");
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        String testName = context.getRequiredTestClass().getPackageName() + "." +
                context.getDisplayName();
        System.out.println("Thread " + Thread.currentThread().getName() +
                " Test finished: " + testName + " Test finished" + " test duration: " +
                (System.currentTimeMillis() - startTimes.get(testName)) + " ms");
    }
}
