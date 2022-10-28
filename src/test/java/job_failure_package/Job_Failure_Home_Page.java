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

public class Job_Failure_Home_Page {
	public WebDriver driver;
	public ExtentReports extent;
	public ExtentTest extentTest; //helps to generate the logs in test report.



	@BeforeTest
	public void setExtent(){
		// initialize the HtmlReporter
		extent = new ExtentReports("./test-output/Reports/HomePageReport.html", true);
		//To add system or environment info by using the addSystemInfo method.
		extent.addSystemInfo("User Name", "Suriya");
		extent.addSystemInfo("Environment", "Automation Testing");
		extent.addSystemInfo("Application","Job Failure Handling");
		extent.addSystemInfo("Test Scenario","Home Page Functionality");

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
		driver = new ChromeDriver(); //create object(instance)
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
		//Thread.sleep(3000);


	}



	@Test(enabled=true,priority=0)
	public void Verify_Add_confifuration_form() throws InterruptedException{
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		extentTest = extent.startTest("Verify Add configuration form");
		//Add configuration form
		extentTest.log(LogStatus.INFO, "Click on the Job Failure Handling");
		extentTest.log(LogStatus.INFO, "Select on the Add confifuration button");
		extentTest.log(LogStatus.INFO, "Click GO button then open the add configuration form");
		//Reporter.log("add form"); // this commad used to add the test step in emailable-report.html
		driver.findElement(By.cssSelector(".accordion:nth-child(1) .accordion-button")).click(); //job failure handling
		//Thread.sleep(5000);
		driver.findElement(By.cssSelector(".show .col:nth-child(3) .form-check-input")).click(); //Add configuration radio button
		driver.findElement(By.linkText("Go")).click();
		//Thread.sleep(5000);	
		driver.findElement(By.linkText("Home")).click();
		//Thread.sleep(5000);
	}

	@Test(enabled=true,priority=1)
	public void Verify_Edit_configuration_form() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		extentTest = extent.startTest("Verify Edit configuration form");

		driver.findElement(By.cssSelector(".accordion:nth-child(1) .accordion-button")).click(); //job failure handling
		driver.findElement(By.name("Configuration")).click(); //Edit configuration button
		driver.findElement(By.linkText("Go")).click();
		//Search page
		driver.findElement(By.name("searchText")).sendKeys("jobfail"); //Enter the valid application name in the search box
		driver.findElement(By.id("button-addon2")).click(); //Click Search box
		//Thread.sleep(5000);	
		driver.findElement(By.linkText("jobfail")).click(); //list on your application name
		driver.findElement(By.linkText("Home")).click();
		//Thread.sleep(3000);
		driver.findElement(By.linkText("LogOut")).click();
		//Thread.sleep(5000);
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
