package utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import testRunner.Hooks;

public class WaitUtils 
{ 
	static int maxElementWait = 15;
	static int maxPageWait = 30;

	private static WebDriver jsWaitDriver;
    private static WebDriverWait jsWait;
    private static JavascriptExecutor jsExec;

	public static void WaitForTitle(WebDriver driver, String titleText) throws Exception
    {
		try {
			WebDriverWait wait = new WebDriverWait(driver, maxElementWait);
			wait.until(ExpectedConditions.titleContains(titleText));			
		}
		catch (Exception e) {
	        e.printStackTrace(); 
	        Log.exception(e, jsWaitDriver, Hooks.extentedReport);
	    }			
    }
	
	public static void WaitForPageToLoad(WebDriver driver) throws Exception
    {
		try {			
			ExpectedCondition<Boolean> documentLoad = new ExpectedCondition<Boolean>() {
	            public final Boolean apply(final WebDriver driver) {
	                final JavascriptExecutor js = (JavascriptExecutor) driver;
	                boolean docReadyState = false;
	                try {
	                    docReadyState = (Boolean) js.executeScript("return (function() { if (document.readyState != 'complete') {  return false; } return true;})();");
	                } catch (Exception e1) {
	                	try {
							throw new Exception("WaitForPageToLoad method failed due to JavaScript Executor scripts: "+ e1.getMessage());
						} catch (Exception e2) {
							e2.printStackTrace();
						}
	                }
	                return docReadyState;

	            }
	        };
	        
	        WebDriverWait wait = new WebDriverWait(driver, maxPageWait);
			wait.until(documentLoad); 
			
			setDriver(driver);
			waitJQueryAngular();		
			
		}
    	catch (Exception e) {
            e.printStackTrace(); 
            Log.exception(e, jsWaitDriver, Hooks.extentedReport);
        }		
    }

    //Get the driver 
    private static void setDriver(WebDriver driver) throws Exception {
    	try {
        	jsWaitDriver = driver;
	        jsWait = new WebDriverWait(jsWaitDriver, maxPageWait);
	        jsExec = (JavascriptExecutor) jsWaitDriver;
    	}
    	catch (Exception e) {
            e.printStackTrace(); 
            Log.exception(e, jsWaitDriver, Hooks.extentedReport);
        }
    }
 
    //Wait for JQuery Load
    private static void waitForJQueryLoad() throws Exception {
    	try {
    	    //Wait for jQuery to load
	        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((Long) ((JavascriptExecutor) jsWaitDriver)
					        .executeScript("return jQuery.active") == 0);
				}
			};
	 
	        //Get JQuery is Ready
	        boolean jqueryReady = (Boolean) jsExec.executeScript("return jQuery.active==0");
	 
	        //Wait JQuery until it is Ready!
	        if(!jqueryReady)//Wait for jQuery to load
	            jsWait.until(jQueryLoad);
	    }
    	catch (Exception e) {
            e.printStackTrace(); 
            Log.exception(e, jsWaitDriver, Hooks.extentedReport);
        }
    } 
 
    //Wait for Angular Load
    private static void waitForAngularLoad() throws Exception {
    	try {
	        WebDriverWait wait = new WebDriverWait(jsWaitDriver,maxPageWait);
	        JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;
	 
	        final String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";
	 
	        //Wait for ANGULAR to load
	        ExpectedCondition<Boolean> angularLoad = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return Boolean.valueOf(((JavascriptExecutor) driver)
					        .executeScript(angularReadyScript).toString());
				}
			};
	 
	        //Get Angular is Ready
	        boolean angularReady = Boolean.valueOf(jsExec.executeScript(angularReadyScript).toString());
	 
	        //Wait ANGULAR until it is Ready!
	        if(!angularReady)
	            wait.until(angularLoad);
    	}
    	catch (Exception e) {
            e.printStackTrace(); 
            Log.exception(e, jsWaitDriver, Hooks.extentedReport);
        }
    }
 
    //Wait Until JS Ready
    private static void waitUntilJSReady() throws Exception {
    	try {
    	    WebDriverWait wait = new WebDriverWait(jsWaitDriver,15);
	        JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;
	 
	        //Wait for Javascript to load
	        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor) jsWaitDriver)
					        .executeScript("return document.readyState").toString().equals("complete");
				}
			};
	 
	        //Get JS is Ready
	        boolean jsReady =  (Boolean) jsExec.executeScript("return document.readyState").toString().equals("complete");
	 
	        //Wait Javascript until it is Ready!
	        if(!jsReady) 
	            wait.until(jsLoad);
	    }
		catch (Exception e) {
	        e.printStackTrace(); 
	        Log.exception(e, jsWaitDriver, Hooks.extentedReport);
	    }
    }
 
    //Wait Until JQuery and JS Ready
    private static void waitUntilJQueryReady() throws Exception {
    	try {
        	JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;
 
	        //First check that JQuery is defined on the page. If it is, then wait AJAX
	        Boolean jQueryDefined = (Boolean) jsExec.executeScript("return typeof jQuery != 'undefined'");
	        if (jQueryDefined == true) {
	        	//Wait JQuery Load
	            waitForJQueryLoad();
	 
	            //Wait JS Load
	            waitUntilJSReady();
	        }
    	}
		catch (Exception e) {
	        e.printStackTrace(); 
	        Log.exception(e, jsWaitDriver, Hooks.extentedReport);
	    }
    }
 
    //Wait Until Angular and JS Ready
    private static void waitUntilAngularReady() throws Exception {
    	try {
            JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;
 
	        //First check that ANGULAR is defined on the page. If it is, then wait ANGULAR
	        Boolean angularUnDefined = (Boolean) jsExec.executeScript("return window.angular === undefined");
	        if (!angularUnDefined) {
	            Boolean angularInjectorUnDefined = (Boolean) jsExec.executeScript("return angular.element(document).injector() === undefined");
	            if(!angularInjectorUnDefined) {
	                //Wait Angular Load
	                waitForAngularLoad();
	 
	                //Wait JS Load
	                waitUntilJSReady();
	            }
	        }	        
    	}
		catch (Exception e) {
	        e.printStackTrace(); 
	        Log.exception(e, jsWaitDriver, Hooks.extentedReport);
	    }
    }
 
    //Wait Until JQuery Angular and JS is ready
    private static void waitJQueryAngular() throws Exception {
    	try {
            waitUntilJQueryReady();
        	waitUntilAngularReady();
    	}
		catch (Exception e) {
	        e.printStackTrace(); 
	        Log.exception(e, jsWaitDriver, Hooks.extentedReport);
	    }
    }
 
}