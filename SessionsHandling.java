import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;

public class SessionsHandling {

    private static final String APP="https://github.com/cloudgrey-io/the-app/releases/" +
            "download/v1.9.0/TheApp-v1.9.0.apk";
    private static final String APPIUM="http://localhost:4723/wd/hub";
    private AndroidDriver driver;

    @Before
    public void setUp() throws Exception{
        DesiredCapabilities caps =  new DesiredCapabilities();
        caps.setCapability("platformName", "android");
        caps.setCapability("platformVersion", "9");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("app", APP);

        driver = new AndroidDriver(new URL(APPIUM), caps);
    }

    @After
    public void tearDown() {
        if(driver != null)
            driver.quit();
    }

    @Test
    public void test() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        //WebElement element = driver.findElement(MobileBy.AccessibilityId("Login Screen"));
        WebElement screen = wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Login Screen")));
        screen.click();

        WebElement username = wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("username")));
        username.sendKeys("alice");
        WebElement password = driver.findElement(MobileBy.AccessibilityId("password"));
        password.sendKeys("mypassword");

        WebElement login_btn = driver.findElement(MobileBy.AccessibilityId("loginBtn"));
        login_btn.click();

        WebElement landing_page = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        MobileBy.xpath("//android.widget.TextView[contains(@text, 'You are logged in')]")));

        assert(landing_page.getText().contains("alice"));
    }

    @Test
    public void testPageSource() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        //WebElement element = driver.findElement(MobileBy.AccessibilityId("Login Screen"));
        WebElement screen = wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Login Screen")));
        screen.click();

        try{ Thread.sleep(1000);} catch (Exception ing) {}
        //extract the xml related to a specific app page to investigate the structure of it and the elements
        System.out.println(driver.getPageSource());
    }

    @Test
    public void echoBox() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        //WebElement element = driver.findElement(MobileBy.AccessibilityId("Login Screen"));
        WebElement screen = wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Echo Box")));
        screen.click();

        WebElement inputBox = wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("messageInput")));
        inputBox.sendKeys("Testing!");
        WebElement save_btn = driver.findElement(
                MobileBy.xpath("//android.view.ViewGroup[@content-desc=\"messageSaveBtn\"]/android.widget.TextView"));

        save_btn.click();

        WebElement result = wait.until(ExpectedConditions.presenceOfElementLocated(
                MobileBy.xpath("//android.widget.TextView[@content-desc=\"Testing!\"]")));

        assert(result.getText().contains("Testing!"));
    }
}
