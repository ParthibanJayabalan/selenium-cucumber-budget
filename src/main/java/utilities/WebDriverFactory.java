package utilities;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.xml.XmlTest;

public class WebDriverFactory {
    
	private static String remoteWebDriver;
	private static String chromeDriverPath;
	private static String geckoDriverPath;
    private static String driverHost;
    private static String driverPort;
    private static URL hubURL;
    static String url;

    private static DesiredCapabilities firefoxCapabilities = DesiredCapabilities.firefox();
    private static DesiredCapabilities chromeCapabilities = DesiredCapabilities.chrome();
    private static ChromeOptions opt = new ChromeOptions();
    
	public static WebDriver get(String browserOS) throws MalformedURLException {

        XmlTest test = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest();
        driverHost = test.getParameter("deviceHost");
        driverPort = test.getParameter("devicePort");
        remoteWebDriver = test.getParameter("remoteDriver");
    	chromeDriverPath = System.getProperty("user.dir") + "//src//main//resources//drivers//chromedriver.exe";
    	geckoDriverPath = System.getProperty("user.dir") + "//src//main//resources//drivers//geckodriver.exe";
    	
        try {
            hubURL = new URL("http://" + driverHost + ":" + driverPort + "/wd/hub");
        } catch (MalformedURLException e) {
           //Can this be skipped
        }
        
        url = test.getParameter("deviceHost");
        
        String browserWithPlatform = browserOS.toLowerCase();
        WebDriver driver = null;
        String browser = null;
        String platform = null;
        System.out.println(browserWithPlatform);

        if (browserWithPlatform.contains("_")) {
            browser = browserWithPlatform.split("_")[0].toLowerCase().trim();
            platform = browserWithPlatform.split("_")[1].toUpperCase().trim();
        }

        try {
            if ("chrome".equalsIgnoreCase(browser)) {
            	if (remoteWebDriver.contains("No"))
            	{
            		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
            		driver = new ChromeDriver();
            	}
            	else
            	{
	                chromeCapabilities.setCapability(ChromeOptions.CAPABILITY, opt);
	                chromeCapabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
	                chromeCapabilities.setPlatform(Platform.fromString(platform));
	                driver = new RemoteWebDriver(hubURL, chromeCapabilities);
            	}
            }
            else if ("firefox".equalsIgnoreCase(browser)) {
            	if (remoteWebDriver.contains("No"))
            	{
            		System.setProperty("webdriver.gecko.driver",geckoDriverPath);
            		driver = new FirefoxDriver();
            	}
            	else
            	{
            		firefoxCapabilities.setCapability("firefox_binary", "C://Program Files//Mozilla Firefox//firefox.exe");
            		return new RemoteWebDriver(hubURL, firefoxCapabilities);
            	}
            }
                
            Assert.assertNotNull(driver, "Driver did not intialize...\n Please check if hub is running / configuration settings are corect.");
            
            driver.manage().deleteAllCookies();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(WaitUtils.maxElementWait, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(WaitUtils.maxPageWait, TimeUnit.SECONDS);
            
        } catch (UnreachableBrowserException e) {
            e.printStackTrace();
            throw new SkipException("Hub is not started or down.");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception encountered in getDriver Method." + e.getMessage().toString());
        } finally {
            try {
                Field f = Reporter.getCurrentTestResult().getClass().getDeclaredField("m_startMillis");
                f.setAccessible(true);
                f.setLong(Reporter.getCurrentTestResult(), Calendar.getInstance().getTime().getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return driver;
    }
}
