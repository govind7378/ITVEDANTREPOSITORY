package Demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class OrangeHRMProject1 {

    public static void main(String[] args) throws Exception {

        WebDriver driver = new ChromeDriver();

        driver.manage().window().maximize();

        // Open Website
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        Thread.sleep(3000);

        // Login
        driver.findElement(By.name("username")).sendKeys("Admin");
        driver.findElement(By.name("password")).sendKeys("admin123");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        Thread.sleep(5000);

        System.out.println("Login Successful");

        // ===================== PIM =====================

        driver.findElement(By.xpath("//span[text()='PIM']")).click();

        Thread.sleep(3000);

        driver.findElement(By.linkText("Add Employee")).click();

        Thread.sleep(3000);

        driver.findElement(By.name("firstName")).sendKeys("Rahul");
        driver.findElement(By.name("lastName")).sendKeys("Patil");

        driver.findElement(By.xpath("//button[@type='submit']")).click();

        Thread.sleep(5000);

        System.out.println("Employee Added Successfully");

        // Leave

        driver.findElement(By.xpath("//*[@id='app']/div[1]/div[1]/aside/nav/div[2]/ul/li[3]/a")).click();

        Thread.sleep(3000);

        System.out.println("Leave Module Opened");

        // ===================== Admin =====================

        driver.findElement(By.xpath("//*[@id='app']/div[1]/div[1]/aside/nav/div[2]/ul/li[1]/a")).click();

        Thread.sleep(3000);

        System.out.println("Admin Module Opened");

        // Logout

        driver.findElement(By.xpath("//*[@id='app']/div[1]/div[1]/header/div[1]/div[3]/ul/li/span")).click();

        Thread.sleep(2000);

        driver.findElement(By.xpath("//*[@id='app']/div[1]/div[1]/header/div[1]/div[3]/ul/li/ul/li[4]/a")).click();

        Thread.sleep(3000);

        System.out.println("Logout Successful");

        driver.quit();
    }
}