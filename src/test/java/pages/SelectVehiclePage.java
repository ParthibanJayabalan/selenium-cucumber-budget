package pages;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
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

public class SelectVehiclePage {

	@FindBy(css="#res-vehicles-filter-by-vehicle-type")
	public WebElement vehicleType_link;

	@FindBy(css="#res-vehicles-sort")
	public WebElement sortBy_link;
	
	@FindBy(css = "#res-home-select-car")
	public WebElement selectmycar_button;

	private static WebDriver driver;
	private static HashMap<String, String> Data;
	private static ExtentTest extentedReport;
	private Actions actions;
	private JourneyDetails journeyDetails;
	private List<WebElement> searchResults;
	
	public SelectVehiclePage(WebDriver Driver, ExtentTest ExtentedReport, HashMap<String, String> testData) throws Exception{		
		try {
			driver = Driver;
			Data = testData;
			extentedReport = ExtentedReport;
			actions = new Actions(driver);
			isLoaded();
			PageFactory.initElements(driver, this);
			journeyDetails = new JourneyDetails(driver, extentedReport, Data);
			Log.message("Navigated to Select Vehicle page successfully", driver, extentedReport);
		}
		catch (Exception e) {
            e.printStackTrace();
            Log.exception(e, driver, Hooks.extentedReport);
        }
	}    

	private void isLoaded() throws Exception {
		try {
			WaitUtils.WaitForPageToLoad(driver);
			WaitUtils.WaitForTitle(driver, "Reservations | Budget Car Rental");
			
			if (!driver.getTitle().equals("Reservations | Budget Car Rental")) {
				Log.fail("Select Vehicle page of Budget car rental website did not open unexpectedly.", driver, extentedReport);
			}
		}
		catch (Exception e) {
            e.printStackTrace();
            Log.exception(e, driver, Hooks.extentedReport);
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

	public void sortBySUV() throws Exception{		
		try {
			actions.click(vehicleType_link).build().perform();
			WaitUtils.WaitForPageToLoad(driver);
			
			WebElement suvOption = driver.findElement(By.cssSelector("li[vehiclecode='suv']"));
			suvOption.click();
			WaitUtils.WaitForPageToLoad(driver);
		}
		catch (Exception e) {
            e.printStackTrace();
            Log.exception(e, driver, Hooks.extentedReport);
        }
	}    

	public void sortByPrice() throws Exception{		
		try {
			actions.click(sortBy_link).build().perform();
			WaitUtils.WaitForPageToLoad(driver);
			
			WebElement sortByPriceOption = driver.findElement(By.cssSelector("ul>li[ng-click='vm.sortVehicle(sort)']"));
			sortByPriceOption.click();
			WaitUtils.WaitForPageToLoad(driver);
		}
		catch (Exception e) {
            e.printStackTrace();
            Log.exception(e, driver, Hooks.extentedReport);
        }
	}    

	private boolean verifyConstraintsOverCheapestCarDetails(int index, String doors, String seats) throws Exception{		
		try { 
			if (searchResults.size() == 0)
				Log.fail("No vehicles matched the search criteria.", driver, extentedReport, true);

			WebElement topOneInResults = searchResults.get(index);
			WebElement expandVehicleInfo = topOneInResults.findElement(By.cssSelector("a[id='res-vehicles-details']"));
			
			expandVehicleInfo.click();
			
			WebElement door = topOneInResults.findElement(By.cssSelector("span[class*=door]"));
			WebElement seat = topOneInResults.findElement(By.cssSelector("span[class*=seats]"));
			
			if (!door.getText().contains(doors) || !seat.getText().contains(seats))
				return false;			
		}
		catch (Exception e) {
            e.printStackTrace();
            Log.exception(e, driver, Hooks.extentedReport);
        }
		
		return true;
	}    
	
	private void payNowBy(int index) throws Exception{		
		try { 
			if (searchResults.size() == 0)
				Log.fail("No vehicles matched the search criteria.", driver, extentedReport, true);

			WebElement topOneInResults = searchResults.get(index);
			WebElement payNow = topOneInResults.findElement(By.cssSelector("#res-vehicles-pay-now"));
			
			actions.moveToElement(payNow).build().perform();
			actions.click(payNow).build().perform();
			
			WaitUtils.WaitForPageToLoad(driver);
		}
		catch (Exception e) {
            e.printStackTrace();
            Log.exception(e, driver, Hooks.extentedReport);
        }
	}    

	private List<WebElement> initializeSearchResults() throws Exception{		
		try { 
			searchResults = driver.findElements(By.cssSelector("div[ng-repeat*='filteredVehicles']"));
		}
		catch (Exception e) {
            e.printStackTrace();
            Log.exception(e, driver, Hooks.extentedReport);
        }
		
		return searchResults;
	}
	
	public ExtrasPage payNowForCheapestWithConstraints() throws Exception{		
		try { 
			int idx;
			
			initializeSearchResults();
			if (searchResults.size() == 0)
				Log.fail("No vehicles matched the search criteria.", driver, extentedReport, true);

			for (idx=0; idx<searchResults.size(); idx++) {
				if (verifyConstraintsOverCheapestCarDetails(idx, Data.get("DoorConstraint"), Data.get("SeatConstraint"))) {
					WebElement name = searchResults.get(idx).findElement(By.cssSelector("h3"));
					Data.put("VehicleChoosen", name.getText());
					
					WebElement price = searchResults.get(idx).findElement(By.cssSelector("price"));
					Data.put("BaseRate", price.getText().toString().substring(1));
					
					Log.message("Refined the search results and captured the cheapest car details.", driver, extentedReport, true);
					
					payNowBy(idx);
					break;
				}
			}
			
			if (idx == searchResults.size())
				Log.fail("No vehicles matched the required vehicle constraints - Doors: " + Data.get("DoorConstraint") + "Seats: " + Data.get("SeatConstraint"), driver, extentedReport, true);
						
		}
		catch (Exception e) {
            e.printStackTrace();
            Log.exception(e, driver, Hooks.extentedReport);
        }
		
		return new ExtrasPage(driver, extentedReport, Data);
	}    
	
}