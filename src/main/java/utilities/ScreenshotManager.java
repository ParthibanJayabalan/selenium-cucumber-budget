package utilities;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.Reporter;
import org.testng.log4testng.Logger;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
/**
 * ScreenshotManager to take screenshots using logger class
 * 
 */
class ScreenshotManager {
	private static final Logger logger = Logger.getLogger(ScreenshotManager.class);

	static void takeScreenshot(WebDriver driver, String filepath) {
		
		try {
			if (Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browser_os").contains("firefox")) {
				TakesScreenshot takesScreenshot;
				File screenshot = null;
				try {
					takesScreenshot = (TakesScreenshot) driver;
				}
				catch (ClassCastException ignored){
					takesScreenshot = (TakesScreenshot) new Augmenter().augment(driver);
				}
				screenshot = takesScreenshot.getScreenshotAs(OutputType.FILE);
				File destFile = new File(filepath);
				destFile.getParentFile().mkdirs();
				FileUtils.copyFile(screenshot, destFile);
				screenshot.delete();
			}
			else {
				Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(5)).takeScreenshot(driver);
				ImageIO.write(screenshot.getImage(), "PNG", new File(filepath));				
			}
			logger.debug("screenshot taken and stored at " + filepath);
		}
		catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
