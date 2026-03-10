package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.ReportHandler;

public class Hooks {
    @Before
    public void beforeScenario(Scenario scenario) {
        ReportHandler.initReports();
        ReportHandler.createTest(scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            ReportHandler.getTest().fail("Scenario Failed");
        } else {
            ReportHandler.getTest().pass("Scenario Passed");
        }
        ReportHandler.flushReports();
    }
}
