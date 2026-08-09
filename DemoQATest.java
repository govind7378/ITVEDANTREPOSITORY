package Demo;

import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DemoQATest {

    WebDriver driver;
    WebDriverWait wait;
    String baseUrl = "https://demoqa.com/";

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    public void verifyTextBoxElement() {
        driver.get(baseUrl + "text-box");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userName")))
                .sendKeys("Rahul Patil");

        driver.findElement(By.id("userEmail")).sendKeys("rahul@test.com");
        driver.findElement(By.id("currentAddress")).sendKeys("Pune, Maharashtra");
        driver.findElement(By.id("permanentAddress")).sendKeys("Mumbai, Maharashtra");

        WebElement submitButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("submit")));

        scrollTo(submitButton);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", submitButton);

        WebElement output = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("output")));

        String actualOutput = output.getText();

        Assert.assertTrue(actualOutput.contains("Rahul Patil"),
                "Name is not displayed in submitted output.");

        Assert.assertTrue(actualOutput.contains("rahul@test.com"),
                "Email is not displayed in submitted output.");
    }

    @Test
    public void verifyCheckBoxElement() {
        driver.get(baseUrl + "checkbox");

        WebElement homeCheckBox = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("span[role='checkbox'][aria-label='Select Home']")));

        homeCheckBox.click();

        WebElement result = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("result")));

        Assert.assertTrue(result.getText().toLowerCase().contains("home"));
    }

    @Test
    public void verifyRadioButtonElement() {
        driver.get(baseUrl + "radio-button");

        driver.findElement(By.cssSelector("label[for='yesRadio']")).click();

        WebElement result = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//p[contains(text(),'You have selected')]")));

        Assert.assertEquals(result.getText(), "You have selected Yes");
    }

    @Test
    public void verifyPracticeForm() {
        driver.get(baseUrl + "automation-practice-form");

        driver.findElement(By.id("firstName")).sendKeys("Rahul");
        driver.findElement(By.id("lastName")).sendKeys("Patil");
        driver.findElement(By.id("userEmail")).sendKeys("rahul@test.com");

        driver.findElement(By.cssSelector("label[for='gender-radio-1']")).click();

        driver.findElement(By.id("userNumber")).sendKeys("9876543210");

        WebElement dateOfBirth = driver.findElement(By.id("dateOfBirthInput"));
        dateOfBirth.click();
        dateOfBirth.sendKeys(Keys.CONTROL + "a");
        dateOfBirth.sendKeys("10 May 1998");
        dateOfBirth.sendKeys(Keys.ENTER);

        selectReactOption("subjectsInput", "Maths");

        driver.findElement(By.cssSelector("label[for='hobbies-checkbox-1']")).click();

        driver.findElement(By.id("currentAddress"))
                .sendKeys("Pune, Maharashtra");

        selectReactOption("react-select-3-input", "NCR");
        selectReactOption("react-select-4-input", "Delhi");

        WebElement submitButton = driver.findElement(By.id("submit"));
        scrollTo(submitButton);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", submitButton);

        WebElement modalTitle = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("example-modal-sizes-title-lg")));

        Assert.assertEquals(modalTitle.getText(), "Thanks for submitting the form");
    }

    @Test
    public void verifyAllButtonActions() {
        driver.get(baseUrl + "buttons");

        Actions actions = new Actions(driver);

        WebElement doubleClickButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("doubleClickBtn")));

        scrollTo(doubleClickButton);

        actions.moveToElement(doubleClickButton)
                .doubleClick()
                .perform();

        WebElement doubleClickMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("doubleClickMessage")));

        Assert.assertEquals(doubleClickMessage.getText(),
                "You have done a double click");

        WebElement rightClickButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("rightClickBtn")));

        scrollTo(rightClickButton);

        actions.moveToElement(rightClickButton)
                .contextClick()
                .perform();

        WebElement rightClickMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("rightClickMessage")));

        Assert.assertEquals(rightClickMessage.getText(),
                "You have done a right click");

        WebElement singleClickButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[normalize-space()='Click Me']")));

        scrollTo(singleClickButton);
        singleClickButton.click();

        WebElement singleClickMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("dynamicClickMessage")));

        Assert.assertEquals(singleClickMessage.getText(),
                "You have done a dynamic click");
    }

    @Test
    public void verifyAllAlerts() {
        driver.get(baseUrl + "alerts");

        driver.findElement(By.id("alertButton")).click();

        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        Assert.assertEquals(alert.getText(), "You clicked a button");
        alert.accept();

        driver.findElement(By.id("timerAlertButton")).click();

        Alert timerAlert = wait.until(ExpectedConditions.alertIsPresent());
        Assert.assertEquals(timerAlert.getText(),
                "This alert appeared after 5 seconds");
        timerAlert.accept();

        driver.findElement(By.id("confirmButton")).click();

        Alert confirmAlert = wait.until(ExpectedConditions.alertIsPresent());
        Assert.assertEquals(confirmAlert.getText(), "Do you confirm action?");
        confirmAlert.dismiss();

        Assert.assertEquals(
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.id("confirmResult"))).getText(),
                "You selected Cancel");

        driver.findElement(By.id("promtButton")).click();

        Alert promptAlert = wait.until(ExpectedConditions.alertIsPresent());
        promptAlert.sendKeys("Rahul");
        promptAlert.accept();

        Assert.assertEquals(
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.id("promptResult"))).getText(),
                "You entered Rahul");
    }

    @Test
    public void verifyDynamicComponents() {
        driver.get(baseUrl + "dynamic-properties");

        WebElement enableButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("enableAfter")));

        Assert.assertFalse(enableButton.isEnabled());

        wait.until(ExpectedConditions.elementToBeClickable(By.id("enableAfter")));

        Assert.assertTrue(driver.findElement(By.id("enableAfter")).isEnabled());

        wait.until(ExpectedConditions.attributeContains(
                By.id("colorChange"), "class", "text-danger"));

        Assert.assertTrue(driver.findElement(By.id("colorChange"))
                .getAttribute("class").contains("text-danger"));

        WebElement visibleButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("visibleAfter")));

        Assert.assertTrue(visibleButton.isDisplayed());
    }

    public void selectReactOption(String inputId, String value) {
        WebElement input = wait.until(
                ExpectedConditions.elementToBeClickable(By.id(inputId)));

        scrollTo(input);
        input.sendKeys(value);

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@id,'option') and normalize-space()='"
                        + value + "']"))).click();
    }

    public void scrollTo(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}