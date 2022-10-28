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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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

public class Job_Failure_Edit_Page {
	public WebDriver driver;
	public ExtentReports extent;
	public ExtentTest extentTest; //helps to generate the logs in test report.

	@BeforeTest
	public void setExtent(){
		// initialize the HtmlReporter
		extent = new ExtentReports("./test-output/Reports/EditPageReport.html", true);
		//To add system or environment info by using the addSystemInfo method.
		extent.addSystemInfo("User Name", "Suriya");
		extent.addSystemInfo("Environment", "Automation Testing");
		extent.addSystemInfo("Application","Job Failure Handling");
		extent.addSystemInfo("Test Scenario","Edit Page Functionality");

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
	public void setup() throws IOException, InterruptedException{

		System.setProperty("webdriver.chrome.driver","C:\\Selenium\\chromedriver\\chromedriver.exe");	
		driver = new ChromeDriver(); 
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		driver.get("https://dev7542.d3ftuhm5tphaj8.amplifyapp.com/");


		Shadow shadow = new Shadow(driver);
		WebElement element = shadow.findElement("amplify-form-section");
		//Read the login data file
		FileReader reader=new FileReader("./src/test/resources/datafile.properties");
		Properties p=new Properties();  
		p.load(reader);

		element.findElement(By.id("username")).sendKeys(p.getProperty("username"));

		element.findElement(By.id("password")).sendKeys(p.getProperty("password"));

		WebElement signin = element.findElement(By.cssSelector("[class=button][type=submit]"));
		signin.submit();
		Thread.sleep(3000);


	}

	//Test case - update values in the edit form 
	@Test(enabled=true,priority=0)
	public void Update_value_edit_form() throws InterruptedException {
		//driver.get( "https://dev7542.d3ftuhm5tphaj8.amplifyapp.com");
		//Thread.sleep(5000);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		//Job failure handling - Open Search form
		extentTest = extent.startTest("Update the value in the edit form");

		extentTest.log(LogStatus.INFO, "Click on the Job Failure Handling");
		driver.findElement(By.cssSelector(".accordion:nth-child(1) .accordion-button")).click(); //job failure handling
		//Thread.sleep(3000);
		extentTest.log(LogStatus.INFO, "Select Edit Configuration radio button");
		driver.findElement(By.cssSelector(".show .col:nth-child(2) .form-check-input")).click(); //Edit configuration radio button
		extentTest.log(LogStatus.INFO, "Click on the Go button");
		driver.findElement(By.linkText("Go")).click();
		//Edit form open
		extentTest.log(LogStatus.INFO, "Search form opened");
		//Thread.sleep(5000);
		extentTest.log(LogStatus.INFO, "Enter the application name in search box");
		driver.findElement(By.id("test")).sendKeys("jobfailure");
		extentTest.log(LogStatus.INFO, "Click on the search button");
		driver.findElement(By.id("button-addon2")).click();//search button
		extentTest.log(LogStatus.INFO, "Click on the your application link");
		driver.findElement(By.linkText("jobfailure")).click();
		extentTest.log(LogStatus.INFO, "Edit form opened");
		extentTest.log(LogStatus.INFO, "Clear the Maximum Restart count value");
		driver.findElement(By.id("maximumRestartCount")).clear();
		//Thread.sleep(3000);
		extentTest.log(LogStatus.INFO, "Update the value");
		driver.findElement(By.id("maximumRestartCount")).sendKeys("6");
		extentTest.log(LogStatus.INFO, "Click on the Submit button");
		driver.findElement(By.cssSelector(".btn")).submit();

		WebElement actmessage = new WebDriverWait(driver, Duration.ofSeconds(15))
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("Toastify")));
		String act = actmessage.getText();
		System.out.println("Application Status :" + act);
		//Thread.sleep(5000);    	    
		// check the updated values in table	
		extentTest.log(LogStatus.INFO, "Check the updated value in the database");
		extentTest.log(LogStatus.INFO, "Enter the application name in search box");
		driver.findElement(By.id("test")).sendKeys("jobfailure");
		extentTest.log(LogStatus.INFO, "Click on the Search button");
		driver.findElement(By.id("button-addon2")).click();
		extentTest.log(LogStatus.INFO, "Application configuration data is displayed");
	}
	@AfterMethod
	public void Down(ITestResult result) throws IOException{

		if(result.getStatus()==ITestResult.FAILURE){
			extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS "+result.getName()); //to add name in extent report
			extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS "+result.getThrowable()); //to add error/exception in extent report

			String screenshotPath =getScreenshot(driver, result.getName());
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
