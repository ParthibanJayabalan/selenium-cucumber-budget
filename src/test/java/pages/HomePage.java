package pages;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.relevantcodes.extentreports.ExtentTest;

import testRunner.Hooks;
import utilities.Common;
import utilities.Log;
import utilities.WaitUtils;

public class HomePage {

	@FindBy(css="#PicLoc_value:nth-child(1)")
	public WebElement pickuplocation_txt;
	
	@FindBy(css="input[message-id='fDate']")
	public WebElement pickupdate_txt;

	@FindBy(css="select[name='reservationModel.pickUpTime']:nth-child(1)")
	public WebElement pickuptime_dropdown;

	@FindBy(css="#DropLoc_value:nth-child(1)")
	public WebElement returnlocation_txt;

	@FindBy(css="input[message-id='returnDate']")
	public WebElement returndate_txt;

	@FindBy(css="select[name='reservationModel.dropTime']:nth-child(1)")
	public WebElement returnTime_dropdown;

	@FindBy(css = "#res-home-select-car")
	public WebElement selectmycar_button;

	private static WebDriver driver;
	private static HashMap<String, String> Data;
	private static ExtentTest extentedReport;
	private Actions actions;
	
	public HomePage(WebDriver Driver, ExtentTest ExtentedReport, HashMap<String, String> testData) throws Exception{		
		try {
			driver = Driver;
			Data = testData;
			extentedReport = ExtentedReport;
			actions = new Actions(driver);
			isLoaded();
			PageFactory.initElements(driver, this);
			Log.message("Navigated to Home page of Budget car rental website successfully", driver, extentedReport);
		}
		catch (Exception e) {
            e.printStackTrace();
            Log.exception(e, driver, Hooks.extentedReport);
        }
	}    

	private void isLoaded() throws Exception {
		try {
			WaitUtils.WaitForPageToLoad(driver);
			WaitUtils.WaitForTitle(driver, "Discount car rental rates and rental car deals");
			
			if (!driver.getTitle().equals("Discount car rental rates and rental car deals | Budget Car Rental")) {
				Log.fail("Home page of Budget car rental website did not open unexpectedly.", driver, extentedReport, true);
			}
		}
		catch (Exception e) {
            e.printStackTrace();
            Log.exception(e, driver, Hooks.extentedReport);
        }
	}

	public void enterJourneyDetails() throws Exception{		
		try {
			if (!Common.isNullOrEmpty(Data)) {
				if (!Common.isNullOrEmpty(Data.get("UNQ_PickupLocation"))) {
					actions.moveToElement(pickuplocation_txt).build().perform();
					pickuplocation_txt.sendKeys(Data.get("UNQ_PickupLocation"));
					WebElement topResult = driver.findElement(By.cssSelector("div[class='angucomplete-row']"));
					actions.click(topResult).build().perform();
					WaitUtils.WaitForPageToLoad(driver);
				}
				
				if (!Common.isNullOrEmpty(Data.get("PickupDateFromToday"))) {
					String pickupDate = Common.generateDate(Integer.parseInt(Data.get("PickupDateFromToday").toString()));
					pickupdate_txt.clear();
					pickupdate_txt.sendKeys(pickupDate);
				}

				if (!Common.isNullOrEmpty(Data.get("PickupTime"))) {
					Select pickupTimeDD = new Select(pickuptime_dropdown);
					
					if (Data.get("PickupTime").toString().equals("-"))
						Data.put("PickupTime", "noon");
					
					pickupTimeDD.selectByVisibleText(Data.get("PickupTime").toString());
				}

				if (!Common.isNullOrEmpty(Data.get("ReturnDateFromPickup"))) {
					String returnDate = Common.generateDate(Integer.parseInt(Data.get("PickupDateFromToday").toString()) + Integer.parseInt(Data.get("ReturnDateFromPickup").toString()));
					returndate_txt.clear();
					returndate_txt.sendKeys(returnDate);
				}

				Log.message("Entered the journey details on home page", driver, extentedReport, true);
			}
			
		}
		catch (Exception e) {
            e.printStackTrace(); 
            Log.exception(e, driver, Hooks.extentedReport);
        }
	}    

	public SelectVehiclePage submitHomePage() throws Exception{		
		try {
			actions.click(selectmycar_button).build().perform();
			WaitUtils.WaitForPageToLoad(driver);
		}
		catch (Exception e) {
            e.printStackTrace();
            Log.exception(e, driver, Hooks.extentedReport);
        }
		
		return new SelectVehiclePage(driver, extentedReport, Data);	
	}    
	
}