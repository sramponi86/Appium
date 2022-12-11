import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;

public class testBrowser {

    private static final String APPIUM="http://localhost:4723/wd/hub";
    private RemoteWebDriver driver;

    @Before
    public void setUp() throws Exception{
        DesiredCapabilities caps =  new DesiredCapabilities();
        caps.setCapability("platformName", "android");
        caps.setCapability("platformVersion", "9");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("browserName", "Chrome");

        //selenium class instead of android specific one
        driver = new RemoteWebDriver(new URL(APPIUM), caps);
    }

    @After
    public void tearDown() {
        if(driver != null)
            driver.quit();
    }

    @Test
    public void test() {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        driver .get("https://appiupro.com");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".toggleMenu"))).click();
        driver.findElement(By.linkText("All Editions")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".editionList")));
    }
}
