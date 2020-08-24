package pages;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.ExtentTest;

import components.JourneyDetails;
import testRunner.Hooks;
import utilities.Log;
import utilities.WaitUtils;

@SuppressWarnings("unused")
public class ExtrasPage {

	@FindBy(css=".vehicle-name:nth-child(1)")
	public static WebElement vehicle_label;

	@FindBy(css=".optBaseRateRow>span>span:nth-child(2)")
	public static WebElement baseRate_label;

	@FindBy(css=".optTaxesRow>span>span:nth-child(2)") 
	public static WebElement taxFees_label;

	@FindBy(css="div[class='estimate optEstimatedTotalRow']>span>span:nth-child(2)")
	public static WebElement total_label;

	private static WebDriver driver;
	private static HashMap<String, String> Data;
	private static ExtentTest extentedReport;
	private Actions actions;
	private JourneyDetails journeyDetails;
	
	public ExtrasPage(WebDriver Driver, ExtentTest ExtentedReport, HashMap<String, String> testData) throws Exception{		
		driver = Driver;
		Data = testData;
		extentedReport = ExtentedReport;
		actions = new Actions(driver);
		isLoaded();
		PageFactory.initElements(driver, this);
		journeyDetails = new JourneyDetails(driver, extentedReport, Data);
		Log.message("Navigated to Extras page successfully", driver, extentedReport);
	}    

	private void isLoaded() throws Exception {
		WaitUtils.WaitForPageToLoad(driver);
		WaitUtils.WaitForTitle(driver, "Reservations | Budget Car Rental");
		
		if (!driver.getTitle().contains("Reservations | Budget Car Rental")) {
			Log.fail("Extras page of Budget car rental website did not open unexpectedly.", driver, extentedReport, true);
		}
	}

	public void verifyJourneyDetails() throws Exception{		
		try {
			journeyDetails.verifyJourneyDetails();
		}
		catch (Exception e) {
            e.printStackTrace();
            Log.exception(e, driver, Hooks.extentedReport);
        }
	}    

	public void verifyVehicleDetails() throws Exception{		
		try {
			if (!vehicle_label.getText().equalsIgnoreCase(Data.get("VehicleChoosen")))
				Log.fail("Incorrect vehicle details are pulled through ion Extras page unexpectedly", driver, extentedReport, true);					
		}
		catch (Exception e) {
            e.printStackTrace();
            Log.exception(e, driver, Hooks.extentedReport);
        }
	}    
	public void verifyPriceDetails() throws Exception{		
		try {
			if (!baseRate_label.getText().equalsIgnoreCase(Data.get("BaseRate")))
				Log.fail("Incorrect base rate details are pulled through on Extras page unexpectedly", driver, extentedReport, true);
			
			float estimatedtotal = Float.valueOf(baseRate_label.getText()).floatValue() + Float.valueOf(taxFees_label.getText()).floatValue();
			
			if (Float.valueOf(total_label.getText()).floatValue() != estimatedtotal)
				Log.fail("Incorrect estimated total value is found on Extras page unexpectedly", driver, extentedReport, true);
			
		}
		catch (Exception e) {
            e.printStackTrace();
            Log.exception(e, driver, Hooks.extentedReport);
        }
	}    
	
}