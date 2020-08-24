package utilities;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;

import testRunner.Hooks;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Log extends Hooks{
	private static File screenShotFolder = new File(Reporter.getCurrentTestResult().getTestContext().getOutputDirectory());
	private static String screenShotFolderPath = screenShotFolder.getParent() + File.separator +  "ScreenShot" + File.separator;
	private static AtomicInteger screenShotIndex = new AtomicInteger(0);
	
	private static final String TEST_TITLE_HTML_BEGIN = "&emsp;<div class=\"test-title\"> <strong><font size = \"3\" color = \"#000080\">";
	private static final String TEST_TITLE_HTML_END = "</font> </strong> </div>&emsp;<div><strong>Steps:</strong></div>";

	private static final String MESSAGE_HTML_BEGIN = "<div class=\"test-message\">&emsp;";
	private static final String MESSAGE_HTML_END = "</div>";

	private static final String PASS_HTML_BEGIN = "<div class=\"test-result\"><br><font color=\"green\"><strong> ";
	private static final String PASS_HTML_END1 = " </strong></font> ";
	private static final String PASS_HTML_END2 = "</div>&emsp;";

	private static final String FAIL_HTML_BEGIN = "<div class=\"test-result\"><br><font color=\"red\"><strong> ";
	private static final String FAIL_HTML_END1 = " </strong></font> ";
	private static final String FAIL_HTML_END2 = "</div>&emsp;";

	private static final String SKIP_EXCEPTION_HTML_BEGIN = "<div class=\"test-result\"><br><font color=\"orange\"><strong> ";
	private static final String SKIP_HTML_END1 = " </strong></font> ";
	private static final String SKIP_HTML_END2 = " </strong></font> ";

	
	private static String takeScreenShot(WebDriver driver) {
		String inputFile = "";
		inputFile = Reporter.getCurrentTestResult().getName() +"_"+ Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName()+ "_" + screenShotIndex.incrementAndGet() + ".png";
		ScreenshotManager.takeScreenshot(driver, screenShotFolderPath + inputFile);
		return inputFile;
	}

	private static String getScreenShotHyperLink(String inputFile) {
		String screenShotLink = "";
		screenShotLink = "<a href=\"." + File.separator + "ScreenShot" + File.separator + inputFile + "\" target=\"_blank\" >[ScreenShot]</a>";
		return screenShotLink;
	}

	private static Logger lsLog4j() {
		return Logger.getLogger(Thread.currentThread().getName());
	}

	private static String callerClass() {
		return Thread.currentThread().getStackTrace()[2].getClassName();
	}

	public static ExtentTest testCaseInfo(String description, String testName, String category) {

		lsLog4j().setLevel(Level.ALL);
		lsLog4j().info("");
		lsLog4j().log(callerClass(), Level.INFO, "****             " + description + "             *****", null);
		lsLog4j().info("");

		if (Reporter.getOutput(Reporter.getCurrentTestResult()).toString().contains("<div class=\"test-title\">")) {
			Reporter.log(TEST_TITLE_HTML_BEGIN + description + TEST_TITLE_HTML_END + "&emsp;");
		}
		else {
			Reporter.log(TEST_TITLE_HTML_BEGIN + description + TEST_TITLE_HTML_END + "<!-- Report -->&emsp;");
		}
		ExtentTest extentedReport = extent.startTest(testName, description);
		extentedReport.assignCategory(category);
		return extentedReport;
		
	}

	public static void testCaseResult(ExtentTest extentedReport) {
		if (Reporter.getOutput(Reporter.getCurrentTestResult()).toString().contains("FAILSOFT")) {
			fail("Test Failed.");			
		}
		else {
			pass("Test Passed.");
		}
		extent.endTest(extentedReport);
	}

	public static void endTestCase(ExtentTest extentReporterTests) {
		lsLog4j().info("****             " + "-E---N---D-" + "             *****");
		extent.endTest(extentReporterTests);
	}

	public static void message(String description, WebDriver driver, ExtentTest extentedReport, Boolean... takeScreenShot) {

		String inputFile = "";
		boolean finalDecision = true;
		if (takeScreenShot.length > 0 && takeScreenShot[0].equals(Boolean.FALSE)) {
			finalDecision = false;
		}
		
		if (finalDecision) {
			inputFile = takeScreenShot(driver);
			getScreenShotHyperLink(inputFile);
			Reporter.log(MESSAGE_HTML_BEGIN + description + "&emsp;" + getScreenShotHyperLink(inputFile) + MESSAGE_HTML_END);
		}
		else
			Reporter.log(MESSAGE_HTML_BEGIN + description + MESSAGE_HTML_END);

		lsLog4j().log(callerClass(), Level.INFO, description, null);

		extentedReport.log(LogStatus.INFO, description +"&emsp;" + getScreenShotHyperLink(inputFile));			
	}

	public static void message(String description, WebDriver driver, ExtentTest extentedReport) {

		Reporter.log(MESSAGE_HTML_BEGIN + description + "&emsp;"  + MESSAGE_HTML_END);
		lsLog4j().log(callerClass(), Level.INFO, description, null);
		extentedReport.log(LogStatus.INFO, description);		
	}

	private static void pass(String description) {

		Reporter.log(PASS_HTML_BEGIN + description + PASS_HTML_END1 + PASS_HTML_END2);
		lsLog4j().log(callerClass(), Level.INFO, description, null);
	}
	
	public static void pass(String description, WebDriver driver, ExtentTest extentedReport, Boolean... takeScreenShot) {

		String inputFile = "";
		boolean finalDecision = true;
		if (takeScreenShot.length > 0 && takeScreenShot[0].equals(Boolean.FALSE)) {
			finalDecision = false;
		}
		
		if (finalDecision) {
			inputFile = takeScreenShot(driver);//screenShotFolderPath
			getScreenShotHyperLink(screenShotFolderPath +inputFile);
			Reporter.log(PASS_HTML_BEGIN + description +"&emsp;" + getScreenShotHyperLink(inputFile)+ PASS_HTML_END1 + PASS_HTML_END2);
			
		}
		else
			Reporter.log(PASS_HTML_BEGIN + description + PASS_HTML_END1 + PASS_HTML_END2);

		lsLog4j().log(callerClass(), Level.INFO, description, null);

		extentedReport.log(LogStatus.PASS, description +"&emsp;" + getScreenShotHyperLink(inputFile));
			
	}

	private static void fail(String description) {
		lsLog4j().log(callerClass(), Level.ERROR, description, null);
		Reporter.log(FAIL_HTML_BEGIN + description + FAIL_HTML_END1 + FAIL_HTML_END2);
		Assert.fail(description);		
	}
	
	public static void fail(String description, WebDriver driver, ExtentTest extentedReport, Boolean... takeScreenShot) {
		String inputFile = takeScreenShot(driver);
		Reporter.log("<div class=\"test-result\"><font color=\"red\">&emsp;" + description + "</font>" + getScreenShotHyperLink(inputFile) + "</div>");
		lsLog4j().log(callerClass(), Level.ERROR, description, null);
		
		getScreenShotHyperLink(screenShotFolderPath +inputFile);
		Reporter.log(FAIL_HTML_BEGIN + description +"&emsp;" + getScreenShotHyperLink(inputFile)+ FAIL_HTML_END1 + FAIL_HTML_END2);
		
		extentedReport.log(LogStatus.FAIL, description +"&emsp;" + getScreenShotHyperLink(inputFile));
		Assert.fail(description);
	}
	
	public static void exception(Exception e, WebDriver driver, ExtentTest extentedReport) throws Exception {

		String inputFile = takeScreenShot(driver);
		String eMessage = e.getMessage();
		if (eMessage != null && eMessage.contains("\n")) {
			eMessage = eMessage.substring(0, eMessage.indexOf("\n"));			
		}

		fail(e.getMessage(), driver, extentedReport, Boolean.TRUE);
		
		lsLog4j().log(callerClass(), Level.FATAL, eMessage, e);
		if (e instanceof SkipException) {
			Reporter.log(SKIP_EXCEPTION_HTML_BEGIN + eMessage + SKIP_HTML_END1 + getScreenShotHyperLink(inputFile) + SKIP_HTML_END2);
			extentedReport.log(LogStatus.FAIL, eMessage +"&emsp;" + getScreenShotHyperLink(inputFile));
		}
		else {
			Reporter.log(FAIL_HTML_BEGIN + eMessage + FAIL_HTML_END1 + getScreenShotHyperLink(inputFile) + FAIL_HTML_END2);
			extentedReport.log(LogStatus.FAIL, eMessage +"&emsp;" + getScreenShotHyperLink(inputFile));
		}
		
	}

}