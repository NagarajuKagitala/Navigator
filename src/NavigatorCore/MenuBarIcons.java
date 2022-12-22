package NavigatorCore;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class MenuBarIcons 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String WGSName;
	static String Node_Hostname;
	static String Low;
	static String Medium;
	static String High;
	static int LowSleep;
	static int MediumSleep;
	static int HighSleep;
	
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
		WGSName =Settings.getWGSNAME();
		Node_Hostname =Settings.getNode_Hostname();
		Low =Settings.getLow();
		Medium =Settings.getMedium();
		High =Settings.getHigh();
		LowSleep=Integer.parseInt(Low);
		MediumSleep =Integer.parseInt(Medium);
		HighSleep=Integer.parseInt(High);
	}
	
	@Test
	@Parameters({"sDriver", "sDriverpath"})
	public static void Login(String sDriver, String sDriverpath) throws Exception
	{
		Settings.read();
		String URL = Settings.getSettingURL();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new ChromeDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.edge.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new EdgeDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.ie.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new InternetExplorerDriver();
		}
		else
		{
			System.setProperty(sDriver, sDriverpath);
			
			FirefoxOptions options = new FirefoxOptions();
			options.setCapability("marionette", false);
			driver = new FirefoxDriver(options);
		}
		
		//Login
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(HighSleep);
	}
	
	@TestRail(testCaseId = 1019)
	@Test(priority=1)
	public void LoginUsername(ITestContext context) throws Exception
	{
		Settings.read();
		String uname=Settings.getNav_Username();
		
		//Store the username into string
		String LoginUser=driver.findElement(By.xpath("//div[2]/div")).getText();
		System.out.println("User name is: " +LoginUser);
		
		if(LoginUser.equalsIgnoreCase(uname))
		{
			System.out.println("Login user is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Login user working fine");
		}
		else
		{
			System.out.println("Login user not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Login user not working");
			driver.findElement(By.id("Login user failed")).click();
		}
	}
	
	@TestRail(testCaseId = 1020)
	@Test(priority=2)
	public void RequestHistoryIcon(ITestContext context) throws InterruptedException
	{
		//Click on request history icon
		driver.findElement(By.cssSelector(".fa-server")).click();
		Thread.sleep(MediumSleep);
		
		//Get the title
		String Requesttitle=driver.findElement(By.xpath("//app-modal-title/div")).getText();
		System.out.println("Title is: " +Requesttitle);
		
		//close the popup
		driver.findElement(By.cssSelector(".btn-danger")).click();
		Thread.sleep(MediumSleep);
		
		if(Requesttitle.equalsIgnoreCase("Request History"))
		{
			System.out.println("Request history icon working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Request history icon working fine");
		}
		else
		{
			System.out.println("Request history failed");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Request history icon not working");
			driver.findElement(By.id("Request history failed")).click();
		}
		
	}
	
	@TestRail(testCaseId = 1021)
	@Test(priority=3)
	public void SchedularIcon(ITestContext context) throws InterruptedException
	{
		//Click on schedular icon
		driver.findElement(By.cssSelector(".fa-clock-o")).click();
		Thread.sleep(MediumSleep);
		
		//Store the page title into string
		String ScheduleTitle=driver.findElement(By.xpath("//app-modal-title/div")).getText();
		System.out.println("Schedular title is: " +ScheduleTitle);
		
		//Close schedules popup page
		driver.findElement(By.cssSelector(".btn-danger")).click();
		Thread.sleep(MediumSleep);
				
		if(ScheduleTitle.equalsIgnoreCase("Schedules"))
		{
			System.out.println("Schedules icon is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Schedules icon is working fine");
		}
		else
		{
			System.out.println("Schedule icon is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Schedules icon not working");
			//Close schedules popup page
			driver.findElement(By.cssSelector(".btn-danger")).click();
			Thread.sleep(3000);
			driver.findElement(By.id("Schedules icon failed")).click();
		}
	}
	
	@TestRail(testCaseId = 1045)
	@Test(priority=4)
	public void SchedularColumnbutton(ITestContext context) throws InterruptedException
	{
		//Click on schedular icon
		driver.findElement(By.cssSelector(".fa-clock-o")).click();
		Thread.sleep(MediumSleep);
		
		//Click on column button
		driver.findElement(By.xpath("//button[contains(.,'Columns')]")).click();
		Thread.sleep(MediumSleep);
		
		//Store column name into string
		String Addedcolumn=driver.findElement(By.xpath("//td[2]")).getText();
		System.out.println("Adding column name is: " +Addedcolumn);
		
		//status of the job id
		boolean sta=driver.findElement(By.xpath("//td/input")).isSelected();
		System.out.println("Status of: " +sta);
		
		if(sta)
		{
			System.out.println("Already selected");
		}
		else
		{
			driver.findElement(By.xpath("//td/input")).click();
			Thread.sleep(LowSleep);
		}
		
		//Click on OK button
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(MediumSleep);
		
		//Get the column value
		String column=driver.findElement(By.xpath("//div/div/ngx-datatable/div/datatable-header/div/div[2]/datatable-header-cell/div")).getText();
		System.out.println("Column name is: " +column);
		
		//Click on column button
		driver.findElement(By.xpath("//button[contains(.,'Columns')]")).click();
		Thread.sleep(MediumSleep);
				
		//status of the job id
		boolean sta1=driver.findElement(By.xpath("//td/input")).isSelected();
		System.out.println("Status of: " +sta);
		
		if(sta1)
		{
			driver.findElement(By.xpath("//td/input")).click();
			Thread.sleep(LowSleep);
			
		}
		else
		{
			System.out.println("Already selected");
		}
		
		//Click on OK button
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(MediumSleep);
		
		//verification
		if(column.equalsIgnoreCase(Addedcolumn))
		{
			System.out.println("Column is added successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Column is added working fine");
		}
		else
		{
			System.out.println("Column is not added");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Column is not added working");
			//Close schedules popup page
			driver.findElement(By.cssSelector(".btn-danger")).click();
			Thread.sleep(3000);
			driver.findElement(By.id("Column adding failed")).click();
		}
		//Click on cancel button
		driver.findElement(By.cssSelector(".btn-danger")).click();
		Thread.sleep(MediumSleep);
	}
	
	@TestRail(testCaseId = 1022)
	@Parameters({"SecurityManagerURL", "SecurityManagerURLLocal"})
	@Test(priority=5)
	public void SecurityManagerIcon(String SecurityManagerURL, String SecurityManagerURLLocal, ITestContext context) throws InterruptedException
	{
		//Click on security manager icon
		driver.findElement(By.cssSelector(".fa-shield")).click();
		Thread.sleep(HighSleep);
		
		java.util.Set<String> h=driver.getWindowHandles();
		//System.out.print("no of handles"+h.size());
		
		String handle[]=new String[h.size()];
		int i=0;
		for(String s:h)
		{
			handle[i]=s;
			i++;
		}
			
		//Switch to opened window
		driver.switchTo().window(handle[1]);
		
		//get current url of the page
		String CurrentUrl = driver.getCurrentUrl();
		System.out.println("URL of the page:" +CurrentUrl);
		driver.close();
		Thread.sleep(MediumSleep);
		
		driver.switchTo().window(handle[0]);
		
		if(CurrentUrl.equalsIgnoreCase(SecurityManagerURL) || CurrentUrl.equalsIgnoreCase(SecurityManagerURLLocal))
		{
			System.out.println("Security manager icon is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Security manager icon working fine");
		}
		else
		{
			System.out.println("Security manager is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Security manager icon not working");
			driver.findElement(By.id("Security manager icon failed")).click();
		}
	}
	
	@TestRail(testCaseId = 1023)
	@Parameters({"HelpURL"})
	@Test(priority=6)
	public void HelpIcon(String HelpURL, ITestContext context) throws InterruptedException
	{
		//click on Help icon
		driver.findElement(By.cssSelector(".fa-question-circle")).click();
		Thread.sleep(HighSleep);
		
		java.util.Set<String> h=driver.getWindowHandles();
		//System.out.print("no of handles"+h.size());
		
		String handle[]=new String[h.size()];
		int i=0;
		for(String s:h)
		{
			handle[i]=s;
			i++;
		}
			
		//Switch to opened window
		driver.switchTo().window(handle[1]);
		
		//get current url of the page
		String CurrentUrl = driver.getCurrentUrl();
		System.out.println("URL of the page:" +CurrentUrl);
		
		driver.close();
		Thread.sleep(MediumSleep);
		
		driver.switchTo().window(handle[0]);
		
		if(CurrentUrl.equalsIgnoreCase(HelpURL))
		{
			System.out.println("Help icon is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Help url icon working fine");
		}
		else
		{
			System.out.println("Help is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Help url icon not working");
			driver.findElement(By.id("Help url icon failed")).click();
		}
		
	}
	
	@TestRail(testCaseId = 1024)
	@Test(priority=7)
	public void AboutIcon(ITestContext context) throws InterruptedException
	{
		//Click on About icon
		driver.findElement(By.cssSelector(".fa-info-circle")).click();
		Thread.sleep(MediumSleep);
		
		//Get the about page title
		String AboutTitle=driver.findElement(By.xpath("//app-modal-title/div")).getText();
		System.out.println("About title is: " +AboutTitle);
		
		//Close the popup
		driver.findElement(By.xpath("//app-modal-title/div/div[2]/i")).click();
		Thread.sleep(MediumSleep);
		
		if(AboutTitle.equalsIgnoreCase("About Navigator"))
		{
			System.out.println("About icon is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "About navigator icon working fine");
		}
		else
		{
			System.out.println("About icon is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "ABout navigator icon not working");
			driver.findElement(By.id("About navigator icon failed")).click();
		}
	}
	
	@Test(priority=12)
	public static void Logout() throws InterruptedException
	{		
		//Logout icon
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("yesButton")).click();
		driver.close();
	}
	
	@AfterMethod
	public void tearDown(ITestResult result) {

		final String dir = System.getProperty("user.dir");
		String screenshotPath;
		
		System.out.println("result getStatus: " + result.getStatus());
		// System.out.println("dir: " + dir);
		if (!result.getMethod().getMethodName().contains("Logout")) {
			if (ITestResult.FAILURE == result.getStatus()) {
				this.capturescreen(driver, result.getMethod().getMethodName(), "FAILURE");
				Reporter.setCurrentTestResult(result);

				Reporter.log("<br/>Failed to execute method: " + result.getMethod().getMethodName() + "<br/>");
				// Attach screenshot to report log
				screenshotPath = dir + "/" + Screenshotpath + "/ScreenshotsFailure/"
						+ result.getMethod().getMethodName() + ".png";

			} else {
				this.capturescreen(driver, result.getMethod().getMethodName(), "SUCCESS");
				Reporter.setCurrentTestResult(result);

				// Attach screenshot to report log
				screenshotPath = dir + "/" + Screenshotpath + "/ScreenshotsSuccess/"
						+ result.getMethod().getMethodName() + ".png";

			}

			String path = "<img src=\" " + screenshotPath + "\" alt=\"\"\"/\" />";
			// To add it in the report
			Reporter.log("<br/>");
			Reporter.log(path);
			try {
				//Update attachment to testrail server
				int testCaseID=0;
				//int status=(int) result.getTestContext().getAttribute("Status");
				//String comment=(String) result.getTestContext().getAttribute("Comment");
				  if (result.getMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(TestRail.class))
					{
					TestRail testCase = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestRail.class);
					// Get the TestCase ID for TestRail
					testCaseID = testCase.testCaseId();
					
					
					
					TestRailAPI api=new TestRailAPI();
					api.Getresults(testCaseID, result.getMethod().getMethodName());
					
					}
				}catch (Exception e) {
					// TODO: handle exception
					//e.printStackTrace();
				}
		}

	}

	public void capturescreen(WebDriver driver, String screenShotName, String status) {
		try {

			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			if (status.equals("FAILURE")) {
				FileHandler.copy(scrFile,
						new File(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png"));
				Reporter.log(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png");
			} else if (status.equals("SUCCESS")) {
				FileHandler.copy(scrFile,
						new File(Screenshotpath + "./ScreenshotsSuccess/" + screenShotName + ".png"));

			}

			System.out.println("Printing screen shot taken for className " + screenShotName);

		} catch (Exception e) {
			System.out.println("Exception while taking screenshot " + e.getMessage());
		}

	}

}
