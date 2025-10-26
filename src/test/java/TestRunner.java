import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("Running all JUnit 5 tests...");

        SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectPackage("org.example")) // your tests are in org.example
                .build();

        Launcher launcher = LauncherFactory.create();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        long failed = listener.getSummary().getTestsFailedCount();
        long succeeded = listener.getSummary().getTestsSucceededCount();

        System.out.println("\n==============================");
        System.out.println("Tests succeeded: " + succeeded);
        System.out.println("Tests failed: " + failed);
        System.out.println("==============================");

        if (failed == 0) {
            System.out.println("All tests passed successfully!");
            System.exit(0);
        } else {
            System.out.println("Some tests failed. Check the output above.");
            System.exit(1);
        }
    }
}