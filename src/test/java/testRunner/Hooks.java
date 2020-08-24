package testRunner;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.xml.XmlTest;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.NetworkMode;

import pages.HomePage;
import utilities.DataUtils;
import utilities.FileUtils;
import utilities.Log;
import utilities.WebDriverFactory;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks {
	
	public static WebDriver driver;
	public static ExtentReports extent;
	public static String website;
	public static String browserOS;
	public static ExtentTest extentedReport;
	public static HomePage homepage;
	
	public ExtentTest addTestInfo(String testCaseId, String testDesc) {

		String test = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName();
		String os = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
				.getParameter("browser_os").split("_")[1];
		String browsername = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browser_os").split("_")[0];
		
		return Log.testCaseInfo( "Browser: " + browsername.toUpperCase() + " , Operating System: " +  os.toUpperCase(), testCaseId + " - " + testDesc, test);
	}

	@Before
	public void beforeScenario(Scenario scenario) throws Exception{
		XmlTest test = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest();
		website = test.getParameter("url");    
		browserOS = test.getParameter("browser_os");
		String tcID = scenario.getName().split(":")[0];
		String tcTitle = scenario.getName().split(":")[1];
		HashMap<String, String> testData = DataUtils.getTestData("JourneyDetails.xlsx", "JourneyDetails", tcID);
		
		extentedReport = addTestInfo(tcID, tcTitle);
		driver = WebDriverFactory.get(browserOS);
		driver.get(website);

		homepage = new HomePage(driver, extentedReport, testData);		
	}
	
	@After
	public void afterScenario(){
		if (driver != null)
			driver.quit();
		Log.endTestCase(extentedReport);
		Log.testCaseResult(extentedReport);	
	}
	
	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() {
		extent = new ExtentReports("target/surefire-reports/TestReport.html", true, DisplayOrder.OLDEST_FIRST, NetworkMode.ONLINE);		
	}

	@AfterSuite
	public void afterSuite() {
		extent.flush();
		File reportFolder = new File("test-reports/TestReports");
		File reportSourceFile = new File("target/surefire-reports/TestReport.html");
		File reportScreenshotFolder = new File("test-output/ScreenShot");
		
		try {
			reportFolder=  FileUtils.createReportFolder(reportFolder);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		String reportDestFolder = reportFolder + File.separator +"TestReport.html";
		File reportDestFile = new File(reportDestFolder);
		String screenshotFolder = reportFolder + File.separator + "ScreenShot";
		File screenshotDestFolder =  new File(screenshotFolder);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {			
			e1.printStackTrace();
		}

		try {
		    FileUtils.copyFile(reportSourceFile, reportDestFile);
		} catch (IOException e) {
		    e.printStackTrace();
		}

		try {
		    FileUtils.copyFolder(reportScreenshotFolder, screenshotDestFolder);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}
