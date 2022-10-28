package job_failure_package;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


import io.github.sukgu.Shadow;

public class Job_Failure_LoginPage {
	public WebDriver driver;
	public ExtentReports extent;
	public ExtentTest extentTest; //helps to generate the logs in test report.

	@BeforeTest
	public void setExtent(){
		// initialize the HtmlReporter
		extent = new ExtentReports("./test-output/Reports/LoginPageReport.html", true); // true - new data insert into report,false-append the old data
		//To add system or environment info by using the addSystemInfo method.
		extent.addSystemInfo("User Name", "Suriya");
		extent.addSystemInfo("Environment", "Automation Testing");
		extent.addSystemInfo("Application","Job Failure Handling"); 
		extent.addSystemInfo("Test Scenario","Login Functionality");

	}

	@AfterTest
	public void endReport(){

		extent.flush(); // Flush method is used to erase any previous data on the report and create a new report.
		//extent.close(); 
	}
	public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException{	
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		// after execution, you could see a folder "FailedTestsScreenshots"
		// under src folder
		String destination = System.getProperty("user.dir") + "/FailedTestsScreenshots/" + screenshotName + dateName
				+ ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}


	@BeforeMethod
	public void setup() throws InterruptedException, IOException {	

		System.setProperty("webdriver.chrome.driver","C:\\Selenium\\chromedriver\\chromedriver.exe");	
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		driver.get("https://dev7542.d3ftuhm5tphaj8.amplifyapp.com/");
		//Thread.sleep(3000);

	}
	@Test(enabled=true,priority=0,description="Test case - Both User name and Password are entered correctly")
	public void user_and_password_correct() throws InterruptedException, IOException {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		extentTest = extent.startTest("Enter correct username and password");

		Shadow shadow = new Shadow(driver);
		WebElement element = shadow.findElement("amplify-form-section");
		//File Read
		FileReader reader=new FileReader("./src/test/resources/datafile.properties");
		Properties p=new Properties();  
		p.load(reader);

		extentTest.log(LogStatus.INFO, "Enter valid Username");
		element.findElement(By.id("username")).sendKeys(p.getProperty("username"));
		extentTest.log(LogStatus.INFO, "Enter valid Password");
		element.findElement(By.id("password")).sendKeys(p.getProperty("password"));

		extentTest.log(LogStatus.INFO, "Click on the SIGNIN button "); 
		WebElement signin = element.findElement(By.cssSelector("[class=button][type=submit]"));
		signin.submit();
		extentTest.log(LogStatus.INFO, "Home Page Open");
		//Thread.sleep(3000);
		driver.findElement(By.linkText("LogOut")).click();
		//Thread.sleep(5000);

	}
	@Test(enabled=true,priority=1,description="Test case - Both Username and Password Fields are blank")
	public void user_and_password_blank() throws InterruptedException {
		extentTest = extent.startTest("Username and Password fields are blank");
		extentTest.log(LogStatus.INFO, "Leave Username field");
		extentTest.log(LogStatus.INFO, "Leave Password field");

		Shadow shadow = new Shadow(driver);
		WebElement element = shadow.findElement("amplify-form-section");

		extentTest.log(LogStatus.INFO, "Click on the SIGNIN button");
		WebElement signin = element.findElement(By.cssSelector("[class=button][type=submit]"));
		signin.submit();
		//Thread.sleep(3000);

	}
	@Test(enabled=true,priority=2,description="Test case - Username field is filled and Password field is blank")
	public void username_filled_and_password_blank() throws InterruptedException, IOException {
		extentTest = extent.startTest("Enter correct username and password field is blank");

		Shadow shadow = new Shadow(driver);
		WebElement element = shadow.findElement("amplify-form-section");

		FileReader reader=new FileReader("./src/test/resources/datafile.properties");
		Properties p=new Properties();  
		p.load(reader);

		extentTest.log(LogStatus.INFO, "Enter valid Username");
		element.findElement(By.id("username")).sendKeys(p.getProperty("username"));

		extentTest.log(LogStatus.INFO, "Leave Password field");

		WebElement signin = element.findElement(By.cssSelector("[class=button][type=submit]"));
		signin.submit();
		extentTest.log(LogStatus.INFO, "Click on the SIGNIN button");

		System.out.println("Password cannot be empty");
		//Thread.sleep(3000);
	}

	@Test(enabled=true,priority=3,description="Test case - Enter invalid username & invalid password")
	public void Enter_invalid_username_and_password() throws InterruptedException {
		extentTest = extent.startTest("Enter invalid username and invalid password ");

		Shadow shadow = new Shadow(driver);
		WebElement element = shadow.findElement("amplify-form-section");

		extentTest.log(LogStatus.INFO, "Enter invalid Username");
		WebElement username = element.findElement(By.id("username"));
		username.click();
		username.sendKeys("suriya");

		extentTest.log(LogStatus.INFO, "Enter invalid Password");
		WebElement password = element.findElement(By.id("password"));
		password.click();
		password.sendKeys("athena");

		extentTest.log(LogStatus.INFO, "Click on the sign in button");
		WebElement signin = element.findElement(By.cssSelector("[class=button][type=submit]"));
		signin.submit();

		System.out.println("User does not exist.");
		//Thread.sleep(3000);
	}

	@AfterMethod
	public void Down(ITestResult result) throws IOException{

		if(result.getStatus()==ITestResult.FAILURE){
			extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS "+result.getName()); //to add name in extent report
			extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS "+result.getThrowable()); //to add error/exception in extent report

			String screenshotPath = getScreenshot(driver, result.getName());
			extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(screenshotPath)); //to add screenshot in extent report
			//extentTest.log(LogStatus.FAIL, extentTest.addScreencast(screenshotPath)); //to add screencast/video in extent report
		}
		else if(result.getStatus()==ITestResult.SKIP){
			extentTest.log(LogStatus.SKIP, "Test Case SKIPPED IS " + result.getName());
		}
		else if(result.getStatus()==ITestResult.SUCCESS){
			extentTest.log(LogStatus.PASS, "Test Case PASSED IS " + result.getName());

		}


		extent.endTest(extentTest); //ending test and ends the current test and prepare to create html report
		driver.quit();
	}

}

