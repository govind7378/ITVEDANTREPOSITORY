package Demo;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ParaBankTest {

    WebDriver driver;
    WebDriverWait wait;

    String baseUrl = "https://parabank.parasoft.com/parabank/";
    String username;
    String password = "Test@123";

    @BeforeClass
    public void createNewDemoUser() {
        String uniqueValue = String.valueOf(System.currentTimeMillis());

        username = "qauser" + uniqueValue.substring(uniqueValue.length() - 8);

        driver = new ChromeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(25));

        driver.get(baseUrl + "register.htm?ConnType=JDBC");

        driver.findElement(By.id("customer.firstName")).sendKeys("Rahul");
        driver.findElement(By.id("customer.lastName")).sendKeys("Patil");
        driver.findElement(By.id("customer.address.street")).sendKeys("MG Road");
        driver.findElement(By.id("customer.address.city")).sendKeys("Pune");
        driver.findElement(By.id("customer.address.state")).sendKeys("Maharashtra");
        driver.findElement(By.id("customer.address.zipCode")).sendKeys("411001");
        driver.findElement(By.id("customer.phoneNumber")).sendKeys("9876543210");
        driver.findElement(By.id("customer.ssn")).sendKeys("123456789");

        driver.findElement(By.id("customer.username")).sendKeys(username);
        driver.findElement(By.id("customer.password")).sendKeys(password);
        driver.findElement(By.id("repeatedPassword")).sendKeys(password);

        driver.findElement(By.cssSelector("input[value='Register']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.linkText("Log Out")));

        driver.findElement(By.linkText("Log Out")).click();
        driver.quit();
    }

    @BeforeMethod
    public void setupAndLogin() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(25));

        driver.get(baseUrl + "index.htm?ConnType=JDBC");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("username"))).sendKeys(username);

        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("input[value='Log In']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.linkText("Log Out")));

        ensureTwoAccounts();
    }

    @Test(priority = 1)
    public void verifyLogin() {
        Assert.assertTrue(driver.findElement(By.linkText("Log Out")).isDisplayed(),
                "Login was not successful.");
    }

    @Test(priority = 2)
    public void verifyAccountOverview() {
        List<String> accountNumbers = getAccountNumbers();

        Assert.assertTrue(accountNumbers.size() >= 2,
                "Accounts are not displayed in Account Overview.");
    }

    @Test(priority = 3)
    public void verifyTransferFunds() {
        List<String> accountNumbers = getAccountNumbers();

        String fromAccount = accountNumbers.get(0);
        String toAccount = accountNumbers.get(1);

        driver.findElement(By.linkText("Transfer Funds")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("amount"))).sendKeys("1");

        Select fromAccountDropdown = waitForSelectWithOptions(
                By.id("fromAccountId"));

        Select toAccountDropdown = waitForSelectWithOptions(
                By.id("toAccountId"));

        fromAccountDropdown.selectByVisibleText(fromAccount);
        toAccountDropdown.selectByVisibleText(toAccount);

        WebElement transferButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("input[value='Transfer']")));

        scrollTo(transferButton);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", transferButton);

        WebElement successMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h1[contains(text(),'Transfer Complete')]")));

        Assert.assertTrue(successMessage.getText()
                .contains("Transfer Complete"));
    }

    @Test(priority = 4)
    public void verifyBillPayment() {
        String fromAccount = getAccountNumbers().get(0);

        driver.findElement(By.linkText("Bill Pay")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("payee.name"))).sendKeys("Electricity Board");

        driver.findElement(By.name("payee.address.street"))
                .sendKeys("MG Road");

        driver.findElement(By.name("payee.address.city"))
                .sendKeys("Pune");

        driver.findElement(By.name("payee.address.state"))
                .sendKeys("Maharashtra");

        driver.findElement(By.name("payee.address.zipCode"))
                .sendKeys("411001");

        driver.findElement(By.name("payee.phoneNumber"))
                .sendKeys("9876543210");

        driver.findElement(By.name("payee.accountNumber"))
                .sendKeys("1234567890");

        driver.findElement(By.name("verifyAccount"))
                .sendKeys("1234567890");

        driver.findElement(By.name("amount")).sendKeys("1");

        Select accountDropdown = waitForSelectWithOptions(
                By.name("fromAccountId"));

        accountDropdown.selectByVisibleText(fromAccount);

        WebElement sendPaymentButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("input[value='Send Payment']")));

        scrollTo(sendPaymentButton);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", sendPaymentButton);

        WebElement successMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h1[contains(text(),'Bill Payment Complete')]")));

        Assert.assertTrue(successMessage.getText()
                .contains("Bill Payment Complete"));
    }

    @Test(priority = 5)
    public void verifyTransactionHistory() {
        String accountNumber = getAccountNumbers().get(0);

        driver.findElement(By.linkText(accountNumber)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("transactionTable")));

        WebDriverWait transactionWait = new WebDriverWait(
                driver, Duration.ofSeconds(30));

        transactionWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector("#transactionTable tbody tr"), 0));

        List<WebElement> transactionRows = driver.findElements(
                By.cssSelector("#transactionTable tbody tr"));

        Assert.assertTrue(transactionRows.size() > 0,
                "Transaction records are not displayed.");
    }

    public void ensureTwoAccounts() {
        List<String> accountNumbers = getAccountNumbers();

        if (accountNumbers.size() >= 2) {
            return;
        }

        driver.findElement(By.linkText("Open New Account")).click();

        WebDriverWait accountWait = new WebDriverWait(
                driver, Duration.ofSeconds(45));

        accountWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("type")));

        Select accountType = new Select(driver.findElement(By.id("type")));
        accountType.selectByValue("1");

        By sourceAccountOptions = By.cssSelector("#fromAccountId option");

        accountWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                sourceAccountOptions, 0));

        Select sourceAccount = new Select(
                driver.findElement(By.id("fromAccountId")));

        sourceAccount.selectByIndex(0);

        WebElement openAccountButton = accountWait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("input[value='Open New Account']")));

        scrollTo(openAccountButton);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", openAccountButton);

        accountWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("newAccountId")));

        driver.findElement(By.linkText("Accounts Overview")).click();

        accountWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector("#accountTable a"), 1));
    }

    public List<String> getAccountNumbers() {
        driver.findElement(By.linkText("Accounts Overview")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("accountTable")));

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector("#accountTable a"), 0));

        return driver.findElements(By.cssSelector("#accountTable a"))
                .stream()
                .map(WebElement::getText)
                .filter(text -> !text.trim().isEmpty())
                .collect(Collectors.toList());
    }

    public Select waitForSelectWithOptions(By locator) {
        wait.until(driver -> {
            WebElement selectElement = driver.findElement(locator);

            return selectElement.findElements(By.tagName("option")).size() > 0;
        });

        return new Select(driver.findElement(locator));
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