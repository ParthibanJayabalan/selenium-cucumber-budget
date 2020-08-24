package testRunner;


import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import cucumber.api.testng.AbstractTestNGCucumberTests;


@RunWith(Cucumber.class)
@CucumberOptions (
		monochrome = true,
		features = "src/test/resources/features"
		)
public class Runner extends AbstractTestNGCucumberTests {

}