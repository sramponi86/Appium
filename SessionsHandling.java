import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.GsmCallActions;
import javafx.stage.Screen;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Interaction;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.SimpleTimeZone;

public class SessionsHandling {

    private static final String APP="https://github.com/cloudgrey-io/the-app/releases/" +
            "download/v1.9.0/TheApp-v1.9.0.apk";
    private static final String APPIUM="http://localhost:4723/wd/hub";
    private AndroidDriver driver;
    private By VERIFY_TXT = MobileBy.AccessibilityId("Verify Phone Number");
    private By SMS_WAITING = MobileBy.xpath("//android.widget.TextView[contains(@text, 'Waiting to receive')]");
    private By SMS_VERIFIED = MobileBy.xpath("//android.widget.TextView[contains(@text, 'verified')]");

    @Before
    public void setUp() throws Exception{
        DesiredCapabilities caps =  new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("platformVersion", "9");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("app", APP);
        caps.setCapability("udid","39V4C20526000672");
        caps.setCapability("autoGrantPermissions",true);
        //caps.setCapability("appPackage","com.android.settings");
        //caps.setCapability("appActivity", ".Settings");

        driver = new AndroidDriver(new URL(APPIUM), caps);
    }

    @After
    public void tearDown() {
        if(driver != null)
            driver.quit();
    }

    @Nullable
    private String getWebContext(AppiumDriver driver) {
        ArrayList<String> contexts = new ArrayList(driver.getContextHandles());
        for (String context : contexts) {
            if (!context.equals("NATIVE_APP")) {
                return context;
            }
        }
        return null;
    }

    @Test
    public void testSMS() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 5);

        WebElement element = driver.findElement(MobileBy.AccessibilityId("Login Screen"));
        driver.makeGsmCall("5551237890", GsmCallActions.CALL);
        Thread.sleep(200);
        driver.makeGsmCall("5551237890", GsmCallActions.ACCEPT);
        driver.findElementByAccessibilityId("username").sendKeys("hi");
        Thread.sleep(2000);
        driver.makeGsmCall("5551237890", GsmCallActions.CANCEL);
        driver.navigate().back();

        wait.until(ExpectedConditions.presenceOfElementLocated(VERIFY_TXT)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(SMS_WAITING));
        driver.sendSMS("5551237890", "Your code is 123456");
        wait.until(ExpectedConditions.presenceOfElementLocated(SMS_VERIFIED));
    }
    @Test
    public void testScreenMethods() throws InterruptedException {
        ScreenOrientation screen = driver.getOrientation();
        System.out.println(screen);

        if(screen != ScreenOrientation.LANDSCAPE)
            driver.rotate(ScreenOrientation.LANDSCAPE);

        Dimension size = driver.manage().window().getSize();
        System.out.println(size);

        File file = driver.getScreenshotAs(OutputType.FILE);
    }
    @Test
    public void testSystemApp() throws InterruptedException {
        Thread.sleep(5000);
        System.out.println(driver.getPageSource());
    }
    @Test
    public void testHybridApp() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement screen = wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Webview Demo")));

        screen.click();

        //driver.context(getWebContext(driver));
        WebElement url = wait.until(ExpectedConditions.presenceOfElementLocated(
                MobileBy.AccessibilityId("urlInput")));
        url.sendKeys("https://cloudgrey.io");
        driver.findElementByXPath("\t\n" +
                "//android.view.ViewGroup[@content-desc=\"navigateBtn\"]/android.widget.TextView")
                .click();
        Thread.sleep(1000);
        Alert alert = driver.switchTo().alert();
        assert alert.getText().contains("Alert");
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

    @Test
    public void Scroll() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        //WebElement element = driver.findElement(MobileBy.AccessibilityId("Login Screen"));
        WebElement screen = wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("List Demo")));
        screen.click();

        WebElement list = wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Altocumulus")));

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Interaction moveToStart = finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), 520, 1530);
        Interaction pressDown = finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg());
        Interaction moveToEnd = finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), 520, 490);
        Interaction pressUp = finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg());

        Sequence Swipe = new Sequence(finger,0);
        Swipe.addAction(moveToStart);
        Swipe.addAction(pressDown);
        Swipe.addAction(moveToEnd);
        Swipe.addAction(pressUp);

        driver.perform(Arrays.asList(Swipe));

        driver.findElement(MobileBy.AccessibilityId("Stratus"));
    }
}
