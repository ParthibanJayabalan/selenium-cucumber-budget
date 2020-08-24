package components;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.ExtentTest;

import testRunner.Hooks;
import utilities.Common;
import utilities.Log;

public class JourneyDetails {

	@FindBy(css="div[class*=source] .location-info")
	public WebElement pickupLoc_label;

	@FindBy(css="div[class*=source] .day-time-info")
	public WebElement pickup_label;

	@FindBy(css="div[class*=destination] .location-info")
	public WebElement returnLoc_label;

	@FindBy(css="div[class*=destination] .day-time-info")
	public WebElement return_label;

	private static WebDriver driver;
	private static HashMap<String, String> Data;
	private static ExtentTest extentedReport;

	public JourneyDetails(WebDriver Driver, ExtentTest ExtentedReport, HashMap<String, String> testData) throws Exception{		
		try {
			driver = Driver;
			Data = testData;
			extentedReport = ExtentedReport;
			PageFactory.initElements(driver, this);
		}
		catch (Exception e) {
            e.printStackTrace();
            Log.exception(e, driver, Hooks.extentedReport);
        }
	}    

	public void verifyJourneyDetails() throws Exception{		
		try {
			if (!Common.isNullOrEmpty(Data.get("UNQ_PickupLocation"))) {
				if (!pickupLoc_label.getText().contains(Data.get("UNQ_PickupLocation")))
					Log.fail("Pickup location is mismatched between Home and Select vehicle pages.", driver, extentedReport, true);
			}
			
			String[] pickup = pickup_label.getText().split(",");
			
			if (!Common.isNullOrEmpty(Data.get("PickupDateFromToday"))) {
				String date = Common.generateDate(Integer.parseInt(Data.get("PickupDateFromToday")));
						
				if (!pickup[1].trim().contains(Common.MonthAndDateFormat(date).substring(5, 11)))
					Log.fail("Pickup date is mismatched between Home and Select vehicle pages.", driver, extentedReport, true);
			
				if (!pickup[0].trim().contains(Common.MonthAndDateFormat(date).substring(0, 3)))
					Log.fail("Pickup day is mismatched between Home and Select vehicle pages.", driver, extentedReport, true);
			}

			if (!Common.isNullOrEmpty(Data.get("PickupTime"))) {
				if (Data.get("PickupTime").toString().equals("noon"))
					Data.put("PickupTime", "12:00 PM");
				if (!pickup[2].trim().contains(Data.get("PickupTime")))
					Log.fail("Pickup time is mismatched between Home and Select vehicle pages.", driver, extentedReport, true);
			}

			if (!Common.isNullOrEmpty(Data.get("UNQ_ReturnLocation"))) {
				if (Data.get("UNQ_ReturnLocation").equalsIgnoreCase("same"))
					Data.put("UNQ_ReturnLocation", Data.get("UNQ_PickupLocation"));
				if (!returnLoc_label.getText().contains(Data.get("UNQ_ReturnLocation")))
					Log.fail("Return location is mismatched between Home and Select vehicle pages.", driver, extentedReport, true);
			}
			
			String[] returnL = return_label.getText().split(",");
			
			if (!Common.isNullOrEmpty(Data.get("ReturnDateFromPickup"))) {
				String date = Common.generateDate(Integer.parseInt(Data.get("PickupDateFromToday")) + Integer.parseInt(Data.get("ReturnDateFromPickup")));
						
				if (!returnL[1].trim().contains(Common.MonthAndDateFormat(date).substring(5, 11)))
					Log.fail("Return date is mismatched between Home and Select vehicle pages.", driver, extentedReport, true);
			
				if (!returnL[0].trim().contains(Common.MonthAndDateFormat(date).substring(0, 3)))
					Log.fail("Return day is mismatched between Home and Select vehicle pages.", driver, extentedReport, true);
			}
			
			if (!Common.isNullOrEmpty(Data.get("ReturnTime"))) {
				if (Data.get("ReturnTime").toString().equals("noon") || Data.get("ReturnTime").toString().equals("-"))
					Data.put("ReturnTime", "12:00 PM");
				if (!returnL[2].trim().contains(Data.get("ReturnTime")))
					Log.fail("Return time is mismatched between Home and Select vehicle pages.", driver, extentedReport, true);
			}

		}
		catch (Exception e) {
            e.printStackTrace();
            Log.exception(e, driver, Hooks.extentedReport);
        }
	}    

}
