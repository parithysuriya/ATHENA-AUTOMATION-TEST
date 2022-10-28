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
import org.openqa.selenium.JavascriptExecutor;
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

public class Job_Failure_Add_Page {
	public WebDriver driver;
	public ExtentReports extent;
	public ExtentTest extentTest; //helps to generate the logs in test report.

	@BeforeTest
	public void setExtent(){
		// initialize the HtmlReporter
		extent = new ExtentReports("./test-output/Reports/AddPageReport.html", true); // true - new data insert into report,false-append the old data
		//To add system or environment info by using the addSystemInfo method.
		extent.addSystemInfo("User Name", "Suriya");
		extent.addSystemInfo("Environment", "Automation Testing");
		extent.addSystemInfo("Application","Job Failure Handling"); 
		extent.addSystemInfo("Test Scenario","Add Configuration Form");

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
		Shadow shadow = new Shadow(driver);
		WebElement element = shadow.findElement("amplify-form-section");
		//File Read
		FileReader reader=new FileReader("./src/test/resources/datafile.properties");
		Properties p=new Properties();  
		p.load(reader);

		element.findElement(By.id("username")).sendKeys(p.getProperty("username"));

		element.findElement(By.id("password")).sendKeys(p.getProperty("password"));

		WebElement signin = element.findElement(By.cssSelector("[class=button][type=submit]"));
		signin.submit();
		Thread.sleep(5000);


	}

	//case 1 - Enter valid details in the form
	@Test(enabled=true,priority=0)
	public void Enter_valid_details() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));


		//Job failure handling - Open add configuration form
		extentTest = extent.startTest("Enter the valid data in add configuration form");

		extentTest.log(LogStatus.INFO, "Click on the Job Failure Handling");
		driver.findElement(By.cssSelector(".accordion:nth-child(1) .accordion-button")).click(); //job failure handling
		//Thread.sleep(5000);
		extentTest.log(LogStatus.INFO, "Select Add Configuration radio button");
		driver.findElement(By.cssSelector(".show .col:nth-child(3) .form-check-input")).click(); //Add configuration radio button
		extentTest.log(LogStatus.INFO, "Click on the Go button");
		driver.findElement(By.linkText("Go")).click();

		//Enter the Valid Details in the form
		extentTest.log(LogStatus.INFO, "Add Configuration Form opened");
		extentTest.log(LogStatus.INFO, "Enter the valid application name");
		driver.findElement(By.id("applicationName")).sendKeys("jobtest14"); //Application Name

		extentTest.log(LogStatus.INFO, "Enter the cluster name");
		driver.findElement(By.id("clusterName")).sendKeys("ECS"); //Cluster Name

		extentTest.log(LogStatus.INFO, "Enter the valid mail id");
		driver.findElement(By.id("slackChannel")).sendKeys("suriyaparithy@gmail.com"); //Slack Channel

		extentTest.log(LogStatus.INFO, "Enter the Application ARN");
		driver.findElement(By.id("applicationARN")).sendKeys("arn:aws:states:us-east-1:12345678:statemachine:mystatemachine");

		extentTest.log(LogStatus.INFO, "Enter the Failure handler ARN");
		driver.findElement(By.id("failureHandlerARN")).sendKeys("arn:aws:states:us-east-1:12345678:statemachine:handlermachine");

		extentTest.log(LogStatus.INFO, "Enter the Max restart count");
		driver.findElement(By.id("maximumRestartCount")).sendKeys("4");

		extentTest.log(LogStatus.INFO, "Enter the Monitor Duration");
		driver.findElement(By.id("monitorDuration")).sendKeys("10");

		extentTest.log(LogStatus.INFO, "Enter the Handler Payload");
		driver.findElement(By.id("handlerPayLoad")).sendKeys("{\"handler\":\"function\"}");

		extentTest.log(LogStatus.INFO, "Enter the CleanupARN");
		driver.findElement(By.id("cleanupARN")).sendKeys("arn:aws:states:us-east-1:12345678:statemachine:cleanupmachine");

		extentTest.log(LogStatus.INFO, "Select on the Restart Needed NO button");
		driver.findElement(By.cssSelector(".col-sm-4:nth-child(2) #restartNeeded")).click();

		//scroll down and up  page.
		extentTest.log(LogStatus.INFO, "Scroll down the form");
		JavascriptExecutor jsx = (JavascriptExecutor)driver;
		jsx.executeScript("window.scrollBy(0,5000)");
		//Thread.sleep(3000);

		extentTest.log(LogStatus.INFO, "Finally click on the submit button");
		driver.findElement(By.xpath("//button[@type='submit']")).submit(); //Submit button 

		//*get actual message submission failed or saved sucessfully	
		WebElement actmessage = new WebDriverWait(driver, Duration.ofSeconds(15))
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("Toastify")));
		String act = actmessage.getText();
		System.out.println("Application Status :" + act);
		//Thread.sleep(5000);

		extentTest.log(LogStatus.INFO, "Verify the application name in database");
		extentTest.log(LogStatus.INFO, "Search form opened");
		extentTest.log(LogStatus.INFO, "Enter the application name in Search box");
		driver.findElement(By.id("test")).sendKeys("jobtest1");

		extentTest.log(LogStatus.INFO, "Click on the search box");
		driver.findElement(By.id("button-addon2")).click();

		extentTest.log(LogStatus.INFO, "Application configuration data is displayed");
		// Thread.sleep(5000);
	}

	//Case 2 -  Enter already existing application name
	@Test(enabled=true,priority=1)
	public void existing_application_name() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		extentTest = extent.startTest("Validate the already existing application name");
		driver.get( "https://dev7542.d3ftuhm5tphaj8.amplifyapp.com/jobForm");
		//Thread.sleep(5000);

		extentTest.log(LogStatus.INFO, "Enter the already existing application name");
		extentTest.log(LogStatus.INFO, "Enter the other field values");
		driver.findElement(By.id("applicationName")).sendKeys("jobtest"); //Application Name

		driver.findElement(By.id("clusterName")).sendKeys("ECS"); //Cluster Name

		driver.findElement(By.id("slackChannel")).sendKeys("suriyaparithy@gmail.com"); //Slack Channel

		driver.findElement(By.id("applicationARN")).sendKeys("arn:aws:states:us-east-1:12345678:statemachine:mystatemachine");

		driver.findElement(By.id("failureHandlerARN")).sendKeys("arn:aws:states:us-east-1:12345678:statemachine:handlermachine");

		driver.findElement(By.id("maximumRestartCount")).sendKeys("4");

		driver.findElement(By.id("monitorDuration")).sendKeys("10");

		driver.findElement(By.id("handlerPayLoad")).sendKeys("{\"handler\":\"function\"}");

		driver.findElement(By.id("cleanupARN")).sendKeys("arn:aws:states:us-east-1:12345678:statemachine:cleanupmachine");

		driver.findElement(By.cssSelector(".col-sm-4:nth-child(2) #restartNeeded")).click();
		//scroll down and up  page.
		JavascriptExecutor jsx = (JavascriptExecutor)driver;
		jsx.executeScript("window.scrollBy(0,5000)");
		//Thread.sleep(3000);

		extentTest.log(LogStatus.INFO, "Click on the submit button");
		driver.findElement(By.xpath("//button[@type='submit']")).submit(); //Submit button 

		extentTest.log(LogStatus.INFO,"Message displayed on submission failed");
		//*get actual message submission failed or saved sucessfully	
		WebElement actmessage = new WebDriverWait(driver, Duration.ofSeconds(15))
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("Toastify")));
		String act = actmessage.getText();
		System.out.println("Application Status :" + act);
	}
	//case 3 - Handler payload format checking
	@Test(enabled=true,priority=2)
	public void Handler_payload_format_check() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		extentTest = extent.startTest("Handler payload field format checking");
		driver.get( "https://dev7542.d3ftuhm5tphaj8.amplifyapp.com/jobForm");
		//Thread.sleep(5000);

		extentTest.log(LogStatus.INFO, "Enter the all valid details in the form");
		driver.findElement(By.id("applicationName")).sendKeys("jobfail"); //Application Name

		driver.findElement(By.id("clusterName")).sendKeys("ECS"); //Cluster Name

		driver.findElement(By.id("slackChannel")).sendKeys("suriyaparithy@gmail.com"); //Slack Channel

		driver.findElement(By.id("applicationARN")).sendKeys("arn:aws:states:us-east-1:12345678:statemachine:mystatemachine");

		driver.findElement(By.id("failureHandlerARN")).sendKeys("arn:aws:states:us-east-1:12345678:statemachine:handlermachine");

		driver.findElement(By.id("maximumRestartCount")).sendKeys("4");

		driver.findElement(By.id("monitorDuration")).sendKeys("10");

		extentTest.log(LogStatus.INFO, "Enter the invalid format on Handler payload field");
		driver.findElement(By.id("handlerPayLoad")).sendKeys("handler:function");

		driver.findElement(By.id("cleanupARN")).sendKeys("arn:aws:states:us-east-1:12345678:statemachine:cleanupmachine");

		driver.findElement(By.cssSelector(".col-sm-4:nth-child(2) #restartNeeded")).click();
		//scroll down and up  page.
		JavascriptExecutor jsx = (JavascriptExecutor)driver;
		jsx.executeScript("window.scrollBy(0,5000)");
		// Thread.sleep(3000);

		extentTest.log(LogStatus.INFO, "Click on the submit button");
		driver.findElement(By.xpath("//button[@type='submit']")).submit(); //Submit button 

		extentTest.log(LogStatus.INFO, "Message displayed on Must be in json format");
		//*get actual message Handler payload - must be in json format	
		WebElement actmessage = new WebDriverWait(driver, Duration.ofSeconds(15))
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mb-3:nth-child(8) span")));
		String act = actmessage.getText();
		System.out.println("Handler Payload :" + act);

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
