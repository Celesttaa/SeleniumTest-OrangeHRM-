package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import java.awt.Window;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import junit.framework.Assert;

public class OrangeHRM_01 {

	public String baseURL = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";
	public WebDriver driver;
	public String browser = "edge";

	@BeforeTest
	public void setup() {
		if (browser.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		} else if (browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}
		driver.manage().window().maximize();
		driver.get(baseURL);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(40));
	}

	@Test(priority=2)
	public void loginWithValidCredentials() {
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Admin");
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys("admin123");
		driver.findElement(By.xpath("//button[@type='submit']")).submit();

		// verify the login was successful by checking the page title
		String pageTitle = driver.getTitle();

		Assert.assertEquals("OrangeHRM", pageTitle);
		logOut();
	}

	@Test(priority = 1)
	public void loginWithInvalidCredentials() throws InterruptedException {
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Admin");
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys("1234");
		driver.findElement(By.xpath("//button[@type='submit']")).submit();

		String message_expected = "Invalid credentials";

		String message_actual = driver
				.findElement(By.xpath("//p[@class='oxd-text oxd-text--p oxd-alert-content-text']")).getText();
		Assert.assertTrue(message_actual.contains(message_expected));
		Thread.sleep(3000);
	}

	@Test(priority = 3)
	public void addEmployee() throws InterruptedException {
		login();
		driver.findElement(By.xpath("//span[text()='PIM']")).click();
		driver.findElement(By.xpath("//a[text()='Add Employee']")).click();
		driver.findElement(By.xpath("//input[@name='firstName']")).sendKeys("Cel");
		driver.findElement(By.xpath("//input[@name='lastName']")).sendKeys("Sharma");
		driver.findElement(By.xpath("//button[@class='oxd-button oxd-button--medium oxd-button--secondary orangehrm-left-space']")).click();

		String confirmation_msg = driver.findElement(By.xpath("//h6[text()='Personal Details']")).getText();
		Assert.assertEquals("Personal Details", confirmation_msg);
		Thread.sleep(5000);
		logOut();
	}

	@Test(priority = 4)
	public void searchEmployeeByName() {
		login();
		driver.findElement(By.xpath("//span[text()='PIM']")).click();
		driver.findElement(By.xpath("//a[text()='Employee List']")).click();
		driver.findElement(By.xpath("(//input[@placeholder='Type for hints...'])[1]")).sendKeys("Cel Sharma");
		driver.findElement(By.xpath("//button[@class='oxd-button oxd-button--medium oxd-button--secondary orangehrm-left-space']")).click();
		String actual_msg = driver.findElement(By.xpath("//span[@class='oxd-text oxd-text--span']")).getText();

		String expected_msg = "Record Found";
		System.out.println(actual_msg);
		Assert.assertTrue(actual_msg.contains(expected_msg));
		logOut();
	}

	@Test(priority = 5)
	public void searchEmployeeById() throws InterruptedException {
		String emp_id = "0393";
		String msg_actual = " ";
		login();
		driver.findElement(By.xpath("//span[text()='PIM']")).click();
		driver.findElement(By.xpath("//a[@class='oxd-topbar-body-nav-tab-item']")).click();
		driver.findElements(By.tagName("input")).get(2).sendKeys(emp_id);
		driver.findElement(By.xpath("//button[@class='oxd-button oxd-button--medium oxd-button--secondary orangehrm-left-space']")).click();
		Thread.sleep(5000);
	      List<WebElement> rows=driver.findElements(By.xpath("//div[@role='row']"));
		
	      if(rows.size()>1)
	      {
	    	  msg_actual=driver.findElement(By.xpath("((//div[@role='row'])[2]/div[@role='cell'])[2]")).getText();
	      }
		Assert.assertEquals(emp_id, msg_actual);	
		logOut();
	}
	
	@Test(priority=6,enabled=false)
	public void fileUpload() throws IOException, InterruptedException
	{
		login();
		driver.findElement(By.xpath("//span[text()='PIM']")).click();
		driver.findElement(By.xpath("//span[normalize-space()='Configuration']")).click();
		driver.findElement(By.partialLinkText("Data Import")).click();
		driver.findElement(By.xpath("//div[@class='oxd-file-button']")).click();
		
		Thread.sleep(5000);
		Runtime.getRuntime().exec("C://Users//Dell//Documents//orangeHRmfileupload.exe");
		
		Thread.sleep(5000);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		logOut();
	}
	
	
	@Test(priority=7,enabled=false)
	public void uploadPhoto() throws InterruptedException, IOException
	{
		login();
		driver.findElement(By.xpath("//span[text()='PIM']")).click();
		driver.findElement(By.xpath("//a[text()='Add Employee']")).click();
		driver.findElement(By.xpath("//input[@name='firstName']")).sendKeys("Cel");
		driver.findElement(By.xpath("//input[@name='lastName']")).sendKeys("Sharma");
		driver.findElement(By.xpath("//button[@class='oxd-icon-button oxd-icon-button--solid-main employee-image-action']")).click();
		Thread.sleep(5000);
		Runtime.getRuntime().exec("C://Users//Dell//Documents//Photo2.exe");
		Thread.sleep(5000);
		logOut();
	}
	
	@Test(priority=8)
	public void deleteEmployee() throws InterruptedException
	{
		login();
		driver.findElement(By.xpath("//span[text()='PIM']")).click();
		driver.findElement(By.xpath("//a[text()='Employee List']")).click();
		driver.findElement(By.xpath("(//input[@placeholder='Type for hints...'])[1]")).sendKeys("Hello");
		driver.findElement(By.xpath("//button[@class='oxd-button oxd-button--medium oxd-button--secondary orangehrm-left-space']")).click();
		
		
		driver.findElement(By.xpath("//i[@class='oxd-icon bi-trash']")).click();
		driver.findElement(By.xpath("//button[@class='oxd-button oxd-button--medium oxd-button--label-danger orangehrm-button-margin']")).click();
		Thread.sleep(3000);
		
		String actual_msg = driver.findElement(By.xpath("//span[text()='No Records Found']")).getText();

		String expected_msg = "No Records Found";
		System.out.println(actual_msg);
		Assert.assertTrue(actual_msg.contains(expected_msg));
		Thread.sleep(3000);
		logOut();
	}
		
	
	
	public void login() {
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Admin");
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys("admin123");
		driver.findElement(By.xpath("//button[@type='submit']")).submit();
	} 

	public void logOut() {
		driver.findElement(By.xpath("//p[@class='oxd-userdropdown-name']")).click();
		driver.findElement(By.xpath("//a[normalize-space()='Logout']")).click();
	}

	@AfterTest
	public void tearDown() throws InterruptedException {
		Thread.sleep(5000);
		driver.close();
		driver.quit();
	}

}
