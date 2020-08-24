package testRunner;

import org.openqa.selenium.WebDriver;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.And;

import pages.HomePage;
import pages.SelectVehiclePage;
import pages.ExtrasPage;
import utilities.Log;

public class StepDefinition {

	WebDriver driver = Hooks.driver;
	HomePage homePage = Hooks.homepage;
	SelectVehiclePage selectVehiclePage;
	ExtrasPage extrasPage;
	
	@When("^Enter and submit the journey details on home page$")
	public void enterAndSubmitHomePage() throws Exception {
		try{
			homePage.enterJourneyDetails();
			selectVehiclePage = homePage.submitHomePage();			
		}
		catch (Exception e) {
			Log.exception(e, driver, Hooks.extentedReport);
		}
	}

	@And("^Verify that the journey details are carried over as expected on select vehicle page$")
	public void verifyJourneyDetailsOnSelect() throws Exception {
		try{
			selectVehiclePage.verifyJourneyDetails();	
		}
		catch (Exception e) {
			Log.exception(e, driver, Hooks.extentedReport);
		}
	}

	@And("^Refine the search results by SUV and cheaper$")
	public void refineSearch() throws Exception {
		try{
			selectVehiclePage.sortBySUV();
			selectVehiclePage.sortByPrice();
		}
		catch (Exception e) {
			Log.exception(e, driver, Hooks.extentedReport);
		}
	}

	@And("^Capture and click on \"([^\"]*)\" for the cheapest car from the search results$")
	public void payNowSelectedCar(String buttonToClick) throws Exception {
		try{
			if (buttonToClick.equalsIgnoreCase("pay now"))
				extrasPage = selectVehiclePage.payNowForCheapestWithConstraints();	
		}
		catch (Exception e) {
			Log.exception(e, driver, Hooks.extentedReport);
		}
	}

	@Then("^Verify that the journey details and details of selected car are carried over as expected on extras page$")
	public void verifyJourneyDetailsOnExtras() throws Exception {
		try{
			extrasPage.verifyJourneyDetails();
			extrasPage.verifyVehicleDetails();
		}
		catch (Exception e) {
			Log.exception(e, driver, Hooks.extentedReport);
		}
	}

	@And("^Verify that the components of rental cost are as expected$")
	public void verifyRentalComponentsOnExtras() throws Exception {
		try{
			extrasPage.verifyPriceDetails();
			Log.pass("Selected the cheapest SUV car and verified that the journey details and details of selected car are carried over as expected", driver, Hooks.extentedReport, true);
		}
		catch (Exception e) {
			Log.exception(e, driver, Hooks.extentedReport);
		}
	}

}
