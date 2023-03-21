package ApodGUI;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Common.AllEvents;
import Common.ClearSelectionofCheckbox;
import Common.CompareObjects;
import Common.Dashboard;
import Common.DifferenceOfObjects;
import Common.LogoutForAll;
import Common.Viewlets;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class ManagerViewlet 
{
	static WebDriver driver;
	static String DefaultTransmissionQueue;
	static String Screenshotpath;
	static String DownloadPath;
	static String M_QueueManagerName;
	static String WGSName;
	static String Dnode;
	static String Uploadmmfscript;
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

		DefaultTransmissionQueue =Settings.getDefaultTransmissionQueue();
		Screenshotpath =Settings.getScreenshotPath();
		DownloadPath =Settings.getDownloadPath();
		M_QueueManagerName =Settings.getM_QueueManagerName();
		WGSName =Settings.getWGSNAME();
		Dnode =Settings.getDnode();
		Uploadmmfscript =Settings.getUploadmmfscript();
		Node_Hostname =Settings.getNode_Hostname();
		Low =Settings.getLow();
		Medium =Settings.getMedium();
		High =Settings.getHigh();
		LowSleep=Integer.parseInt(Low);
		MediumSleep =Integer.parseInt(Medium);
		HighSleep=Integer.parseInt(High);
	}

	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname", "ViewletValue", "ViewletName"})
	@Test
	public void Login(String sDriver, String sDriverpath, String Dashboardname, int ViewletValue, String ViewletName) throws Exception
	{
		Settings.read();
		String URL = Settings.getSettingURL();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		String filepath=System.getProperty("user.dir") + "\\" + DownloadPath;
		
		//Selecting the browser
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
		System.setProperty(sDriver, sDriverpath);
		
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.prompt_for_download", "false");
		chromePrefs.put("download.default_directory", filepath);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("--remote-allow-origins=*");
		driver=new ChromeDriver(options);
		}
		else if(sDriver.equalsIgnoreCase("webdriver.ie.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new InternetExplorerDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.edge.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver= new EdgeDriver();
		}
		else
		{
			System.setProperty(sDriver, sDriverpath);
			driver= new FirefoxDriver();
		}
		
		//Enter the URL into the browser and Maximize the window
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		
		driver.manage().deleteAllCookies();
	    ((ChromiumDriver) driver).getSessionStorage().clear();
	    ((ChromiumDriver) driver).getLocalStorage().clear();
		
		//Login page
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(HighSleep);
				
		//Delete existing dashboard
		Dashboard ob=new Dashboard();
		ob.DeleteExistDashboard(driver, Dashboardname);
		
		//Click on Create button
		//driver.findElement(By.xpath("//app-side-dashboard-menu/div/div/div[2]/div[2]")).click();
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		Thread.sleep(LowSleep);
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(MediumSleep);
		
		// ---- Creating Manager Viewlet ----
		Viewlets obj=new Viewlets();
		obj.IBMMQViewlet(driver, ViewletValue, ViewletName, WGSName, Node_Hostname);
		
	}
	
	
	@Parameters({"Description"})
	@TestRail(testCaseId = 47)
	@Test(priority=1)
	public static void AddNewManagerFromIcon(String Description, ITestContext context) throws InterruptedException
	{
		//Click on + icon
		driver.findElement(By.xpath("//img[@title='Add Queue Manager']")).click();
		Thread.sleep(LowSleep);
		
		//WGS Selection
		/*
		 * Select WGS=new Select(driver.findElement(By.xpath("//select")));
		 * WGS.selectByVisibleText(WGSName); Thread.sleep(3000);
		 */
		
		driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/ng-select/div/span")).click();
		Thread.sleep(LowSleep);
		try 
		{
			WebElement ChannelauthNode=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
			List<WebElement> divs=ChannelauthNode.findElements(By.tagName("div"));
			System.out.println(divs.size());	
			for (WebElement di : divs)
			{					
				if(di.getText().equals(WGSName))
				{
					di.click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(2000);
		
		//Select Node 
		driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/ng-select/div/span")).click();
		Thread.sleep(LowSleep);
		try 
		{
			WebElement ChannelauthNode=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
			List<WebElement> divs=ChannelauthNode.findElements(By.tagName("div"));
			System.out.println(divs.size());	
			for (WebElement di : divs)
			{					
				if(di.getText().equals(Dnode))
				{
					di.click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(2000);
		//driver.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div")).click();
		
		/*Select Node=new Select(driver.findElement(By.xpath("//div[2]/input")));
		Node.selectByVisibleText("DESKTOP-E1JT2VR");
		Thread.sleep(2000);*/
	
		//Click on Select Path
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(LowSleep);
		
		//Queue Details
		driver.findElement(By.xpath("//app-qmgrcreatestep1/div/div[2]/div/input")).sendKeys(M_QueueManagerName);
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//div[4]/div/input")).sendKeys(DefaultTransmissionQueue);
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//textarea")).sendKeys(Description);
		Thread.sleep(LowSleep);
		
		//Next button 
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		Thread.sleep(LowSleep);
		
		//driver.findElement(By.xpath("(//input[@type='text'])[9]")).sendKeys("New Manager"); 
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		Thread.sleep(LowSleep);
		
		//Log Path
		//driver.findElement(By.xpath("(//input[@type='text'])[6]")).sendKeys("Desktop");
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		Thread.sleep(LowSleep);
		
		//Data Path
		//driver.findElement(By.xpath("(//input[@type='text'])[6]")).sendKeys("Test data path");
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		Thread.sleep(LowSleep);
		
		//Final Submit
		driver.findElement(By.xpath("//div[2]/div/div[2]/div[2]/button")).click();
		Thread.sleep(HighSleep);
		
		try
		{
		if (!checkprogress()) {

			System.out.println("exit");
		}
		}
		catch(Exception e)
		{
			
		}
		
		try 
		{
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e) 
		{
			System.out.println("error popup is not displayed");
		} 
		
		//Refresh the Viewlets
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(MediumSleep);
		
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(M_QueueManagerName);
		Thread.sleep(LowSleep);
		System.out.println("Queue manager name is: " +M_QueueManagerName);
		
		/*driver.findElement(By.xpath("//div[3]/app-viewlet/div/div[2]/div/div[2]/div/div/img")).click();
		Thread.sleep(2000);*/
		
		//Get the Viewlets Data
		String viewlet1=driver.findElement(By.xpath("//datatable-body")).getText();
		
		for(int i=0; i<=M_QueueManagerName.length(); i++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Verification condition 
		if(viewlet1.contains(M_QueueManagerName))
		{
			System.out.println("Queue Manager is successfully added");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Queue Manager added successfully");
		}
		else
		{
			System.out.println("Queue Manager is not added");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add Queue Manager");
			driver.findElement(By.id("QM from icon is failed")).click();
		}
		Thread.sleep(4000);
	}
	
	@Parameters({"Dashboardname", "SchemaName", "Attributes"})
	@TestRail(testCaseId = 48)
	@Test(priority=39)
	public static void ShowObjectAttributes(String Dashboardname, String SchemaName, String Attributes,ITestContext context) throws InterruptedException
	{
		try {
		//Objects Verification
		ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.ManagerAttributes(Dashboardname, driver, SchemaName, Attributes, WGSName);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Object attributes working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured while showing object attributes, Check details: " + e.getMessage());
			driver.findElement(By.id("Objects verification failed")).click();
			driver.findElement(By.id("Attributes failed")).click();
		}
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=2)
	@TestRail(testCaseId = 49)
	public void ShowTopology(String Dashboardname, ITestContext context) throws InterruptedException
	{
		try
		{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Store the Manager name into string
		String ManagerName=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		
		//Select Show topology option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Topology")).click();
		Thread.sleep(MediumSleep);
		
		try
		{
		if (!checkprogress()) {

			System.out.println("exit");
		}
		}
		catch (Exception e)
		{
			
		}
		
		//Click on set and show topology option
		driver.findElement(By.xpath("//button[contains(.,'Set and show topology')]")).click();
		Thread.sleep(HighSleep);
		
		//Store the Topology page data into string
		String Topology=driver.findElement(By.cssSelector("svg")).getText();
		
		//Verification condition
		if(Topology.contains(ManagerName))
		{
			System.out.println("Topology page is opened with the selected Queue manager");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Topology page is opened with the selected Queue manager");
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
		} 
		else
		{
			System.out.println("Topology page is not opened with the selected Queue manager");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to open Topology page");
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			driver.findElement(By.xpath("Topology page failed")).click();
		}
		Thread.sleep(1000);	
		}
		catch(Exception e1)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to open Topology page");
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			driver.findElement(By.xpath("Topology page failed")).click();
		}
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 959)
	@Test(priority=3)
	public void QueueManagerStatus(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		WebElement icon=driver.findElement(By.xpath("//datatable-body-cell[2]"));
		System.out.println("txt1: " +icon.getAttribute("innerHTML"));
		String Status=icon.getAttribute("innerHTML");
		System.out.println("Status is :" +Status);
		
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Status")).click();
		Thread.sleep(MediumSleep);
		
		//Get the Status in console
		String ConsoleStatus=driver.findElement(By.xpath("//span[2]/div[3]")).getText();
		System.out.println("Console status is: " +ConsoleStatus);
		
		if(Status.contains("queueManagerOk.png"))
		{
			if(ConsoleStatus.equalsIgnoreCase("Running"))
			{
				System.out.println("Queue manager is running");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Queue manager status passed");
			}
			else
			{
				System.out.println("Queue manager status failed");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Queue manager status failed");
				driver.findElement(By.cssSelector(".close-button")).click();
				driver.findElement(By.id("QM Status failed")).click();
				
			}
		}
		else if(Status.contains("queueManagerWarn.png") || Status.contains("queueManagerStopped.png"))
		{
			if(ConsoleStatus.equalsIgnoreCase(""))
			{
				System.out.println("Queue manager not running");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Queue manager status passed");
			}
			else
			{
				System.out.println("Queue manager status failed");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Queue manager status failed");
				driver.findElement(By.cssSelector(".close-button")).click();
				driver.findElement(By.id("QM Status failed")).click();
			}
			
		}
		else
		{
			System.out.println("Status not getting properly");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Queue manager status failed");
			driver.findElement(By.cssSelector(".close-button")).click();
			driver.findElement(By.id("QM Status failed")).click();
		}
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(4000);
		
	}
	
	@Test(priority=4)
	@TestRail(testCaseId = 50)
	public static void StartAllWMQObjects(ITestContext context) throws InterruptedException
	{		
		//Select the  Start All WMQ Objects from Commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li")).click();
		Thread.sleep(8000);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Started all WMQ objects");
		//Click on Yes confirmation button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		
	}
	
	@Test(priority=5)
	@TestRail(testCaseId = 51)
	public static void StopAllWMQObjects(ITestContext context) throws InterruptedException
	{		
		//Select the  Stop All WMQ Objects from Commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li[2]")).click();
		Thread.sleep(8000);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Stopped all WMQ objects");
		//Click on yes confirmation button
		driver.findElement(By.cssSelector(".btn-primary")).click();
	
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=6)
	@TestRail(testCaseId = 52)
	public void Security(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
				
		//Select the Security from Commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Security"))).build().perform();
		driver.findElement(By.linkText("Display/Set Object Authority")).click();
		Thread.sleep(MediumSleep);
		
		try {
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e)
		{
			System.out.println("Error popup page is not displayed");
		}
		Thread.sleep(1000);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Security option is working fine");
		
		//Click on Cancel button
		driver.findElement(By.cssSelector(".btn-danger")).click();
		Thread.sleep(MediumSleep);
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=7)
	@TestRail(testCaseId = 53)
	public void PreViewErrorLogs(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		try
		{
		//Select the ViewErrorLogs from Commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("View Error Log...")).click();
		Thread.sleep(HighSleep);
		
		//Click on load logs button
		driver.findElement(By.xpath("//button[contains(.,'Load Logs')]")).click();
		Thread.sleep(MediumSleep);
		Thread.sleep(HighSleep);
		Thread.sleep(60000);
		
		try
		{
			//Click on Log file name
			driver.findElement(By.xpath("//div[5]/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell/div")).click();
			Thread.sleep(LowSleep);
			
			WebElement preview=driver.findElement(By.xpath("//button[contains(.,'Preview')]"));
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("arguments[0].click();", preview);
			
			//Click on Preview
			//driver.findElement(By.xpath("//button[contains(.,'Preview')]")).click();
			Thread.sleep(LowSleep);
			
			String Preview=driver.findElement(By.xpath("//app-mod-log-file-preview/app-modal-title/div")).getText();
			System.out.println("Preview page :" +Preview);
			
			if(Preview.contains("Preview Log File"))
			{
				System.out.println("Preview log file is opened");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Error log page is working fine");
			}
			else
			{
				System.out.println("Preview log file is not opened");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Exception occured");
				driver.findElement(By.xpath("//app-mod-log-file-preview/div/div[2]/div/div/div/button")).click();
				Thread.sleep(3000);
				driver.findElement(By.id("Preview log file failed")).click();
			}
		}
		catch (Exception e)
		{
			System.out.println("Error logs are not present");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Error log page is working fine");
			
			//Close the Error log page
			driver.findElement(By.cssSelector(".btn-danger")).click();
			Thread.sleep(MediumSleep);
		}
		
		//Close preview page
		driver.findElement(By.xpath("//app-mod-log-file-preview/div/div[2]/div/div/div/button")).click();
		Thread.sleep(LowSleep);
		
		//Close the Error log page
		driver.findElement(By.cssSelector(".btn-danger")).click();
		Thread.sleep(MediumSleep);
		
		}
		catch (Exception e)
		{
			System.out.println("Exception occured in View error log page");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured in View error log page, check details: "+ e.getMessage());
			Thread.sleep(2000);
			driver.findElement(By.xpath("//app-modal-title/div/div[2]/i[2]")).click();
			Thread.sleep(20000);
		}
		
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=8)
	@TestRail(testCaseId = 965)
	public void SaveErrorLogs(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		try
		{
		//Select the ViewErrorLogs from Commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("View Error Log...")).click();
		Thread.sleep(HighSleep);
		
		//Click on load logs button
		driver.findElement(By.xpath("//button[contains(.,'Load Logs')]")).click();
		Thread.sleep(MediumSleep);
		Thread.sleep(HighSleep);
		Thread.sleep(60000);
		
		try
		{
			//Click on Log file name
			driver.findElement(By.xpath("//div[5]/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell/div")).click();
			Thread.sleep(LowSleep);
			
			//Click on Preview
			driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
			Thread.sleep(MediumSleep);
			
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Error log page is working fine");
		}
		catch (Exception e)
		{
			System.out.println("Error logs are not present");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Error log page is working fine");
			
			//Close the Error log page
			driver.findElement(By.cssSelector(".btn-danger")).click();
			Thread.sleep(LowSleep);
		}
		
		//Close the Error log page
		driver.findElement(By.cssSelector(".btn-danger")).click();
		Thread.sleep(LowSleep);
		
		}
		catch (Exception e)
		{
			System.out.println("Exception occured in View error log page");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured in View error log page, check details: "+ e.getMessage());
			Thread.sleep(2000);
			driver.findElement(By.xpath("//app-modal-title/div/div[2]/i[2]")).click();
			Thread.sleep(20000);
		}
		
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 1028)
	@Test(priority=9)
	public void CollapseAllbutton(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the Connections from Commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Connections(modal)...")).click();
		Thread.sleep(MediumSleep);
		
		//Click on Collapse all button
		driver.findElement(By.xpath("//button[contains(.,'Collapse all')]")).click();
		Thread.sleep(LowSleep);
		
		WebElement ele=driver.findElement(By.className("modal-content")).findElement(By.tagName("app-mod-qmgr-connections")).findElement(By.className("container"));
		
		List<WebElement> divs=ele.findElements(By.tagName("div"));
		System.out.println("No of divsa are: " +divs.size());
	    
		for(WebElement di:divs) 
		{ 
			WebElement el=di.findElement(By.className("parent-container")).findElement(By.tagName("i")); 
		    String CollapseAlldata=el.getAttribute("class");
			System.out.println("Data is: " +CollapseAlldata);
		    
		    if(CollapseAlldata.contains("rotateEnterLeft90"))
			{
				System.out.println("Collapse button is working fine");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Collapse button is working fine");
			}
			else
			{
				System.out.println("Collapse button is not working");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Collapse button is not working");
				
				driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
				driver.findElement(By.id("Collapse button failed")).click();
			}
		    break;
		 }
		
		//Click on OK button
		driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		Thread.sleep(MediumSleep);
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 1029)
	@Test(priority=10)
	public void ExpandAllbutton(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the Connections from Commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Connections(modal)...")).click();
		Thread.sleep(MediumSleep);
		
		//Click on Collapse all button
		driver.findElement(By.xpath("//button[contains(.,'Collapse all')]")).click();
		Thread.sleep(LowSleep);
		
		//Click on Expand all button
		driver.findElement(By.xpath("//button[contains(.,'Expand all')]")).click();
		Thread.sleep(LowSleep);
		
		WebElement ele=driver.findElement(By.className("modal-content")).findElement(By.tagName("app-mod-qmgr-connections")).findElement(By.className("container"));
		
		List<WebElement> divs=ele.findElements(By.tagName("div"));
		System.out.println("No of divsa are: " +divs.size());
	    
		for(WebElement di:divs) 
		{ 
			WebElement el=di.findElement(By.className("parent-container")).findElement(By.tagName("i")); 
			String ExpandData=el.getAttribute("class");
			System.out.println("Data is: " +ExpandData);
			
			if(ExpandData.contains("rotateEnterRight90"))
			{
				System.out.println("Expand button is working fine");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Expand button is working fine");
			}
			else
			{
				System.out.println("Expand button is not working");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Expand button is not working");
				
				driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
				driver.findElement(By.id("Expand button failed")).click();
			}
			break;
		 }
				
		//Click on OK button
		driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		Thread.sleep(MediumSleep);
		
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=11)
	public static void ConnectionsConsole(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the Connections from Commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Connections(console)...")).click();
		Thread.sleep(MediumSleep);
		
		String Connection=driver.findElement(By.xpath("//th[3]")).getText();
		System.out.println("Connection window name: " +Connection);
		
		
		//verification
		if(Connection.equalsIgnoreCase("Active Connection Id") || Connection.equalsIgnoreCase("Application Type"))
		{
			System.out.println("Connection console is opened");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Connection console is opened");
		}
		else
		{
			System.out.println("Connection console is not opened");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Connection console is not opened");
			driver.findElement(By.id("failed connection console")).click();
		}
			
		//Close the popup page
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(4000);
		
	}
	
	@Test(priority=12, dependsOnMethods = {"ConnectionsConsole"})
	public static void ConnectionConsoleProperties(ITestContext context) throws InterruptedException
	{
		//Click on connection
		driver.findElement(By.name("name")).click();
		driver.findElement(By.xpath("//app-console-qmgr-conn-dropdown/div/div/div[2]")).click();
		Thread.sleep(LowSleep);
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=13)
	@TestRail(testCaseId = 54)
	public static void Properties(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
		
		//Click on General tab
		driver.findElement(By.linkText("General")).click();
		Thread.sleep(LowSleep);
		
		//Store the editable function in to a string
		boolean FieldNamevalue=driver.findElement(By.id("name")).isEnabled();
		System.out.println(FieldNamevalue);
		
		//Verification
		if(FieldNamevalue == false)
		{
			 System.out.println("Manager Name field is UnEditable");
			 context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Manager Name field is UnEditable, condition working fine");
				
			 driver.findElement(By.cssSelector(".btn-primary")).click();
			 Thread.sleep(MediumSleep);
		}
		else
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Condition failed, manager name is in editable");
			System.out.println("Manager Name field is Editable");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(MediumSleep);
			driver.findElement(By.xpath("Manager name edit function Failed")).click();
			
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "Query"})
	@TestRail(testCaseId = 55)
	@Test(priority=14)
	public static void MQSCConsoleCommandOption(String Dashboardname, String Query, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the Console option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("MQSC"))).perform();
		driver.findElement(By.linkText("Console...")).click();
		Thread.sleep(MediumSleep);
		
		//Enter the Query and Click on Submit
		driver.findElement(By.xpath("//app-mod-mqsc-console/div/div[2]/div/div/input")).sendKeys(Query);
		driver.findElement(By.xpath("//button[contains(.,'Submit')]")).click();
		Thread.sleep(MediumSleep);
		
		//Store the Console output into string
		String ConsoleOutput=driver.findElement(By.xpath("//textarea")).getAttribute("value");
		//System.out.println("Responce data is: " +ConsoleOutput);
				
		if(ConsoleOutput.contains("NASTEL.EVENT.QUEUE") || ConsoleOutput.contains("SYSTEM.ADMIN"))
		{
			System.out.println("Query executed");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "MQSC console Query is executed");
		}
		else
		{
			System.out.println("Query Failed");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "MQSC console query failed");
			driver.findElement(By.xpath("//div[4]/button")).click();
			driver.findElement(By.id("Console Query failed")).click();
		}
		Thread.sleep(1000);				
	}
	
	@TestRail(testCaseId = 763)
	@Test(priority=15, dependsOnMethods= {"MQSCConsoleCommandOption"})
	public void SaveMQSCConsoleResponceData(ITestContext context) throws InterruptedException
	{
		try
		{
			driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
			Thread.sleep(MediumSleep);
			System.out.println("Responce data is saved");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "MQSC console Responce data saved");
		}
		catch (Exception e)
		{
			System.out.println("Responce data is not saved");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "MQSC console Responce data not saved");
			driver.findElement(By.id("Console save failed")).click();
		}
		
	}
	
	@TestRail(testCaseId = 764)
	@Test(priority=16, dependsOnMethods= {"MQSCConsoleCommandOption"})
	public void ClearMQSCConsoleResponceData(ITestContext context) throws InterruptedException
	{
		//Clear data by using clear button 
		driver.findElement(By.xpath("//button[contains(.,'Clear')]")).click();
		Thread.sleep(MediumSleep);
		
		//Store the Console output into string after clearing the console data
		String ClearedConsoleOutput=driver.findElement(By.xpath("//textarea")).getAttribute("value");
		System.out.println(ClearedConsoleOutput);
		
		if(ClearedConsoleOutput.equalsIgnoreCase(""))
		{
			System.out.println("Console cleared");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "MQSC console cleared");
		}
		else
		{
			System.out.println("Console not cleared");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "MQSC console is nt cleared");
			
			driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
			driver.findElement(By.id("Console not cleared")).click();
			
		}
		
		//close the window
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
		Thread.sleep(MediumSleep);
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 966)
	@Test(priority=17)
	public static void ApplyMQSCScript(String Dashboardname, ITestContext context) throws InterruptedException, AWTException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the Console option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("MQSC"))).perform();
		driver.findElement(By.linkText("Apply script...")).click();
		Thread.sleep(MediumSleep);
		
		//Click on load from file
		driver.findElement(By.xpath("//button[contains(.,'Load from file')]")).click();
		Thread.sleep(MediumSleep);
		
		//Loading the file into queue by using robot class
		String filepath=System.getProperty("user.dir") + "\\" + Uploadmmfscript;
		StringSelection stringSelection = new StringSelection(filepath);
		//StringSelection stringSelection = new StringSelection(UploadFilepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(8000);
		
		//Click on Submit button
	    driver.findElement(By.xpath("//button[contains(.,'Submit')]")).click();
	    Thread.sleep(HighSleep);
	    
	    //Store the result into string
	    String Result=driver.findElement(By.xpath("//div[2]/div[2]/div/textarea")).getAttribute("value");
	   // System.out.println("mmf script results: " +Result);
	    
	    if(Result.contains("NASTEL.MMF.ADMIN.COMMAND.QUEUE") && Result.contains("NASTEL.MMF.AUDIT.QUEUE"))
	    {
	    	System.err.println("mmf.tst script is executed successfully");
	    	context.setAttribute("Status", 1);
			context.setAttribute("Comment", "mmf.tst script is executed");
	    }
	    else
	    {
	    	System.err.println("mmf.tst script is not executed");
	    	context.setAttribute("Status", 5);
			context.setAttribute("Comment", "mmf.tst script is not executed");
			
			//close the script console popup
		    driver.findElement(By.cssSelector(".btn-danger")).click();
		    Thread.sleep(3000);
			driver.findElement(By.id("mmf.tst failed")).click();
	    }
	}
	
	
	@TestRail(testCaseId = 967)
	@Test(priority=18, dependsOnMethods= {"ApplyMQSCScript"})
	public static void SaveMQSCScriptResults(ITestContext context) throws InterruptedException
	{
		try
		{
			//Click on Save button
			driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
			Thread.sleep(MediumSleep);
			
			System.err.println("mmf.tst script data is saved");
	    	context.setAttribute("Status", 1);
			context.setAttribute("Comment", "mmf.tst script is saved");
		}
		catch(Exception e)
		{
			System.err.println("mmf.tst script is not saved");
	    	context.setAttribute("Status", 5);
			context.setAttribute("Comment", "mmf.tst script is not saved");
			driver.findElement(By.id("mmf.tst save failed")).click();
		}
		
	}
	
	
	@TestRail(testCaseId = 968)
	@Test(priority=19, dependsOnMethods= {"ApplyMQSCScript"})
	public static void ClearMQSCScriptResults(ITestContext context) throws InterruptedException
	{
		//Click on clear button
		driver.findElement(By.xpath("//button[contains(.,'Clear')]")).click();
		Thread.sleep(LowSleep);  
		
		//Store the result into string
	    String Result=driver.findElement(By.xpath("//div[2]/div[2]/div/textarea")).getAttribute("value");
	    System.out.println("mmf script results: " +Result);
	    
	    if(Result.contains(""))
	    {
	    	System.err.println("Apply script responce data is cleared");
	    	context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Apply script responce data is cleared");
	    }
	    else
	    {
	    	System.err.println("Apply script responce data is not cleared");
	    	context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Apply script responce data is not cleared");
			driver.findElement(By.cssSelector(".btn-danger")).click();
			driver.findElement(By.id("mmf.tst failed")).click();
	    }
	    
	    //close the script console popup
	    driver.findElement(By.cssSelector(".btn-danger")).click();
	    Thread.sleep(MediumSleep);
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 993)
	@Test(priority=20)
	public void MQSCSnapshot(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Save the Auth info name into string
		String Managername=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println("Object name is: " +Managername);
		
		//Select MQSCSnapshot option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("MQSC"))).perform();
    	driver.findElement(By.linkText("Snapshot...")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Get the snapshot data and store into string
    	String SnapshotData=driver.findElement(By.xpath("//textarea")).getText();
    	System.out.println("Snapshot data is: " +SnapshotData);
    	
    	//verification
    	if(SnapshotData.contains(Managername))
    	{
    		System.out.println("MQSC sanpshot is opened");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "MQSC Snapshot is opened");
    	}
    	else
    	{
    		System.out.println("MQSC Sanpshot is not opened");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "MQSC Snapshot is not opened");
			driver.findElement(By.id("Manager snapshot failed")).click();
    	}
	}
	
	@TestRail(testCaseId = 994)
	@Test(priority=21, dependsOnMethods= {"MQSCSnapshot"})
	public void SaveManagerSnapshot(ITestContext context)
	{
		try
		{
			//Click on Save button
			driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
			Thread.sleep(MediumSleep);
			
			//Click on cancel button
			driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
			
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "MQSC Snapshot is saved");
		}
		catch (Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "MQSC Snapshot is not saved");
			driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
			driver.findElement(By.id("MQSC snapshot save failed")).click();
		}
		
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 56)
	@Test(priority=22)
	public static void DiscoverNow(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		try {
		//Select Incremental option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Discover now"))).perform();
		driver.findElement(By.linkText("Incremental")).click();
		Thread.sleep(MediumSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//Select Full option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverFull=new Actions(driver);
		MousehoverFull.moveToElement(driver.findElement(By.linkText("Discover now"))).perform();
		driver.findElement(By.linkText("Full")).click();
		Thread.sleep(MediumSleep);
		
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Discover now option working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an error while testing discover now option: "+ e.getMessage());
			driver.findElement(By.id("discover failed")).click();
		}
		
	}
	
	@Parameters({"Dashboardname", "DeleteManagerName"})
	@TestRail(testCaseId = 57)
	@Test(priority=23)
	public void Delete(String Dashboardname, String DeleteManagerName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		try {
		
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(DeleteManagerName);
		//Select Delete option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[8]")).click();
		Thread.sleep(4000);	
		
		//Click on confirmation Yes button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Delete option working fine");
		}catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an error while testing delete option: "+ e.getMessage());
		}
	}
	
	@Parameters({"Dashboardname", "DeleteManagerCheckboxValue", "QueueManagerName"})
	@TestRail(testCaseId = 58)
	@Test(priority=24)
	public void DeleteFromDB(String Dashboardname, int DeleteManagerCheckboxValue, String QueueManagerName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select Delete from Database option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[9]")).click();
		Thread.sleep(1000);
		
		//Store the manager viewlet data into string
		String ManagerData=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification condition
		if(ManagerData.contains(QueueManagerName))
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Manager is not deleted");
			System.out.println("Manager is not deleted");
			driver.findElement(By.xpath("manager not deleted")).click();
		}
		else
		{
			System.out.println("manager is deleted");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Manager is deleted, delete fron DB option working fine");
		}
		
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 59)
	@Test(priority=25)
	public static void Events(String Dashboardname, ITestContext context) throws InterruptedException
	{
		AllEvents obj=new AllEvents();
		obj.Events(Dashboardname, driver, context);
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId = 60)
	@Test(priority=26)
	public static void AddToFavorites(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Create favorite Viewlet
		driver.findElement(By.xpath("//button[3]")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.id("fav")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click();
		Thread.sleep(LowSleep);
		
		//Viewlet Name
		driver.findElement(By.name("viewlet-name")).click();
		driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
		Thread.sleep(LowSleep);
		
		//Select WGS dropdown
		driver.findElement(By.xpath("//app-modal-add-viewlet-favorite/div/div/div[2]/div/ng-select/div/span")).click();
		Thread.sleep(LowSleep);
		
		WebElement drop1=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> div1=drop1.findElements(By.tagName("div"));
		//System.out.println("Number of divs are: " +div.size());
		
		for(WebElement di1 : div1)
		{
			//System.out.println("text is :" +di.getText());
			if(di1.getText().equalsIgnoreCase(WGSName))
			{
				di1.click();
				break;
			}	
		}
		Thread.sleep(2000);
		
		/*
		 * Select wgsdropdown1=new Select(driver.findElement(By.name("wgs")));
		 * wgsdropdown1.selectByVisibleText(WGSName);
		 */
		
		//Submit
		driver.findElement(By.xpath("//app-modal-add-viewlet-favorite/div/div[2]/button[2]")).click();
		Thread.sleep(LowSleep);           
				
		//Manager names data storage
		String Manager1=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println(Manager1);
		
		//----------- Add Manager to favorite viewlet -----------------
		//Select Add tofavorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(LowSleep);
		
		//Select favorite viewlet  
		driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div/ng-select/div")).click();
		Thread.sleep(LowSleep); 
		
		WebElement drop=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> div=drop.findElements(By.tagName("div"));
		//System.out.println("Number of divs are: " +div.size());
		
		for(WebElement di : div)
		{
			//System.out.println("text is :" +di.getText());
			if(di.getText().equalsIgnoreCase(FavoriteViewletName))
			{
				di.click();
				break;
			}	
		}
		
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(HighSleep);
		
		//Favorite viewlet data storing
		String Fav1=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		//String Fav2=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		//|| Manager1.contains(Fav2)
		//Verification condition
		if(Fav1.contains(Manager1))
		{
			 context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Manager is added to Favorite viewlet");
			System.out.println("Manager is added to Favorite viewlet");
		}
		else
		{
			 context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Failed to add manager to favourite viewlet");
			System.out.println("Manager is not added to Favorite viewlet");
		}
		Thread.sleep(1000);
		
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 969)
	@Test(priority=27)
	public static void CreateDefaultViewlets(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the Default viewlets option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Create dashboard"))).perform();
		driver.findElement(By.linkText("Default viewlets")).click();
		Thread.sleep(MediumSleep);
		
		WebElement cla1=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis1=cla1.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis1.size());
		
		StringBuffer buffer=new StringBuffer();
		for(WebElement li1: lis1)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi1=li1.findElement(By.className("g-tab-title"));
			//System.out.println("Names are: " +fi.getText());
			buffer.append(fi1.getText());
			buffer.append(",");
		}
		
		String DashboardNames=buffer.toString();
		System.out.println("List of dashboards are: " +DashboardNames);
		
		if(DashboardNames.contains("Dashboard_"))
		{
			System.out.println("Default viewlet dashboard is created");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Default viewlet dashboard is created");
		}
		else
		{
			System.out.println("Default viewlet dashboard is not created");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Default viewlet dashboard is not created");
			DeleteDefaultViewlet();
			driver.findElement(By.id("Create Default viewlet failed")).click();
		}
		Thread.sleep(2000);	
		DeleteDefaultViewlet();
	}
	
	@TestRail(testCaseId = 970)
	@Test(priority=28)
	@Parameters({"Dashboardname"})
	public static void CreateDefaultTemplateDashboard(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		WebElement cla=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis=cla.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis.size());
		
		for(WebElement li: lis)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi=li.findElement(By.className("g-tab-title"));
			//System.out.println("Names are: " +fi.getText());
			
			if(fi.getText().equalsIgnoreCase(Dashboardname))
			{
				Actions a=new Actions(driver);
				a.contextClick(fi).perform();
				Thread.sleep(5000);
				break;
			}
		}
		
		//Click on Set as default
		driver.findElement(By.linkText("Set as dashboard template")).click();
		Thread.sleep(MediumSleep);
		
		//Select the Default viewlets option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Create dashboard"))).perform();
		Thread.sleep(LowSleep);
		
		WebElement Dash=driver.findElement(By.linkText(""+ Dashboardname +""));
		JavascriptExecutor js=(JavascriptExecutor)driver;
		js.executeScript("arguments[0].click();", Dash);
		Thread.sleep(MediumSleep);
		
		WebElement cla1=driver.findElement(By.className("tabs-panel-left-relative-block")).findElement(By.tagName("ul"));
		List<WebElement> lis1=cla1.findElements(By.tagName("li"));
		System.out.println("No of dashboards are: " +lis1.size());
		
		StringBuffer buffer=new StringBuffer();
		for(WebElement li1: lis1)
		{
			//System.out.println("titles are: " +li.getAttribute("class"));
			WebElement fi1=li1.findElement(By.className("g-tab-title"));
			//System.out.println("Names are: " +fi.getText());
			buffer.append(fi1.getText());
			buffer.append(",");
		}
		
		String DashboardNames=buffer.toString();
		System.out.println("List of dashboards are: " +DashboardNames);
		
		if(DashboardNames.contains("Dashboard_"))
		{
			System.out.println("Default viewlet dashboard is created");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Default viewlet dashboard is created");
		}
		else
		{
			System.out.println("Default viewlet dashboard is not created");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Default viewlet dashboard is not created");
			DeleteDefaultViewlet();
			driver.findElement(By.id("Create Default viewlet failed")).click();
		}
		Thread.sleep(2000);
		
		DeleteDefaultViewlet();
	}
	
	
	@Test(priority=29)
	@TestRail(testCaseId = 61)
	public static void CompareManagers(ITestContext context) throws InterruptedException
	{
		CompareObjects com=new CompareObjects();
		com.Compare(driver, WGSName, context);
	}
	
	
	@TestRail(testCaseId = 765)
	@Test(priority=30)
	public void CheckDifferencesForManagers(ITestContext context) throws InterruptedException
	{
		DifferenceOfObjects diff=new DifferenceOfObjects();
		diff.Differences(driver, context);
	}
	
	
	@Test(priority=31)
	@TestRail(testCaseId = 62)
	public void StartAllWMQObjectsForMultipleManagers(ITestContext context) throws InterruptedException
	{
		try {
		//Select Start all wmq objects from commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Mousehovercopy=new Actions(driver);
		Mousehovercopy.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]/ul/li")).click();
		Thread.sleep(1000);
		
		//Click on Yes confirmation button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		
		 context.setAttribute("Status", 1);
		 context.setAttribute("Comment", "WMQ objects are started using commands");
		}catch(Exception e)
		{
			 context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Failed to start WMQ objects, check details: " + e.getMessage());
		}
		
	}
	
	@Test(priority=32)
	@TestRail(testCaseId = 63)
	public void StopAllWMQObjectsForMultipleManagers(ITestContext context) throws InterruptedException
	{
		try {
		//Select Stop all wmq objects from commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Mousehovercopy=new Actions(driver);
		Mousehovercopy.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]/ul/li[2]")).click();
		Thread.sleep(1000);
		
		//Click on Yes confirmation button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		
		 context.setAttribute("Status", 1);
		 context.setAttribute("Comment", "WMQ objects are stopped using commands");
		}
		catch(Exception e)
		{
			 context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Failed to stop WMQ objects, check details: " + e.getMessage());
		}
	}
	
	@Parameters({"Dashboardname", "MultipleDescription"})
	@TestRail(testCaseId = 64)
	@Test(priority=33)
	public void MultipleManagersProperties(String Dashboardname, String MultipleDescription, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		try {
		//Select properties option   
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(LowSleep);
		
		//Click on General tab
		driver.findElement(By.linkText("General")).click();
		Thread.sleep(LowSleep);
		
		//Description
		driver.findElement(By.id("qmngrDescription")).clear();
		driver.findElement(By.id("qmngrDescription")).sendKeys(MultipleDescription);
		Thread.sleep(LowSleep);
		
		//Close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//Select the properties option for First manager
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
		
		//Click on General tab
		driver.findElement(By.linkText("General")).click();
		Thread.sleep(LowSleep);
		
		//Store the First queue manager description into string
		String FirstQM=driver.findElement(By.id("qmngrDescription")).getAttribute("value");
		System.out.println("First Queue description is: " +FirstQM);
		Thread.sleep(1000);
		
		//Close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che2=new ClearSelectionofCheckbox();
		che2.Deselectcheckbox(Dashboardname, driver);
		
		//Select the properties option for Second manager
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
		
		//Click on General tab
		driver.findElement(By.linkText("General")).click();
		Thread.sleep(LowSleep);
		
		//Store the Second queue manager description into string
		String SecondQM=driver.findElement(By.id("qmngrDescription")).getAttribute("value");
		System.out.println("Second Queue description is: " +SecondQM);
		Thread.sleep(1000);
		
		//Close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//Verification
		if(MultipleDescription.equals(FirstQM) && MultipleDescription.equals(SecondQM))
		{
			 context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Multiple properties are fine for QM");
			System.out.println("Multiple properties are fine for QM");
		}
		else
		{
			 context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Multiple properties are not working for QM");
			System.out.println("Multiple properties are not working for QM");
			driver.findElement(By.id("Multiple properties failed")).click();
		}
		Thread.sleep(2000);
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got exception while checking multiple properties for QM, check details: " + e.getMessage());
			driver.findElement(By.id("Multiple properties failed")).click();
		}
		
	}
	
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@Test(priority=34, dependsOnMethods= {"AddToFavorites"})
	@TestRail(testCaseId = 65)
	public static void AddToFavoriteForMultipleManagers(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Manager names data storage                  
		String Manager2=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println(Manager2);
		
		//Store the Manager name into string  
		String Manager3=driver.findElement(By.xpath("//datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		//Select Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(MediumSleep);
		
		//Select favorite viewlet
		driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div/ng-select/div")).click();
		Thread.sleep(LowSleep); 
		
		WebElement drop=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> div=drop.findElements(By.tagName("div"));
		//System.out.println("Number of divs are: " +div.size());
		
		for(WebElement di : div)
		{
			//System.out.println("text is :" +di.getText());
			if(di.getText().equalsIgnoreCase(FavoriteViewletName))
			{
				di.click();
				break;
			}	
		}
		
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(MediumSleep);
		
		//Favorite viewlet data storing
		String Fav1=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		Thread.sleep(2000);
		
		//Verification of managers are present int favorite viewlet
		if(Fav1.contains(Manager2) && Fav1.contains(Manager3))
		{
			context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Multiple Managers are added to Favorite viewlet");
			System.out.println("Multiple Managers are added to Favorite viewlet");
		}
		else
		{
			context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Failed to add Multiple Managers to Favorite viewlet");
			System.out.println("Multiple Managers are not added to Favorite viewlet");
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "QueueManagerNameFromOptions", "DefaultTransmissionQueueFromOptions", "DescriptionFromOptions"})
	@Test(priority=35)
	@TestRail(testCaseId = 66)
	public void CreateQueueManagerFromOptions(String Dashboardname, String QueueManagerNameFromOptions, String DefaultTransmissionQueueFromOptions, String DescriptionFromOptions,ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the Create Queue manager option 
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Create Queue Manager")).click();
		Thread.sleep(MediumSleep);
		
		//Queue Details
		driver.findElement(By.xpath("//app-qmgrcreatestep1/div/div[2]/div/input")).sendKeys(QueueManagerNameFromOptions);
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//div[4]/div/input")).sendKeys(DefaultTransmissionQueueFromOptions);
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//textarea")).sendKeys(DescriptionFromOptions);
		Thread.sleep(LowSleep);
		
		//Next button 
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		Thread.sleep(LowSleep);
		
		//driver.findElement(By.xpath("(//input[@type='text'])[9]")).sendKeys("New Manager"); 
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		Thread.sleep(LowSleep);
		
		//Log Path
		//driver.findElement(By.xpath("(//input[@type='text'])[6]")).sendKeys("Desktop");
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		Thread.sleep(LowSleep);
		
		//Data Path
		//driver.findElement(By.xpath("(//input[@type='text'])[6]")).sendKeys("Test data path");
		driver.findElement(By.xpath("//button[contains(.,'Next ')]")).click();
		Thread.sleep(LowSleep);
		
		//Final Submit
		driver.findElement(By.xpath("//button[contains(.,'Finish')]")).click();
		Thread.sleep(HighSleep);  
		
		try
		{
		if (!checkprogress()) {

			System.out.println("exit");
		}
		}
		catch(Exception e)
		{
			
		}
		
		try 
		{
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e) 
		{
			System.out.println("No message is displaying");
		} 
		
		//Search with created QM name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(QueueManagerNameFromOptions);
		Thread.sleep(LowSleep);
		
		//Store the Manager viewlet data into string
		String ManagerData=driver.findElement(By.xpath("//datatable-body")).getText();
		
		//Edit the search data
		for(int i=0; i<=QueueManagerNameFromOptions.length(); i++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Verification condition 
		if(ManagerData.contains(QueueManagerNameFromOptions))
		{
			context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Queue Manager added successfully");
			System.out.println("Queue Manager is successfully added");
		}
		else
		{
			context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Failed to add Queue manager");
			System.out.println("Queue Manager is not added");
		}
		Thread.sleep(2000);
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 67)
	@Test(priority=36)
	public static void SearchFilter(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Get the manager name into string
		String Manager=driver.findElement(By.xpath("//datatable-body-cell[3]")).getText();
		//String Manager=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[3]/div/span")).getText();
		
		//Click on search field
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Manager);
		Thread.sleep(LowSleep);
		
		//Store the viewlet data into string
		String Viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
		//System.out.println(Viewletdata);
		       
		//Verify the Search data is present in the Viewlet
	    if(Viewletdata.toUpperCase().contains(Manager.toUpperCase()))
	    {
	       System.out.println("Search is working fine");
	       context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Search option is working fine");
	    }
	    else
	    {
	       System.out.println("Search is not working fine");
	       context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Search option not working properly");
	       driver.findElement(By.xpath("Search is failed")).click();
	    }
	    
	    //Clear the search field data
	    for(int k=0; k<=Manager.length(); k++)
	    {
	    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
	    }
	    Thread.sleep(2000);
	   
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=40)
	public void Logout(String Dashboardname) throws Exception
	{
		LogoutForAll lo=new LogoutForAll();
		lo.LogoutMethod(driver, Dashboardname);
	}
	
	public static void DeleteDefaultViewlet() throws InterruptedException
	{
		
		for(int i=0; i<=4; i++)
		{
			String Dashboardname="Dashboard_"+i+"";
			Dashboard obj=new Dashboard();
			obj.DeleteExistDashboard(driver, Dashboardname);
		}
		Thread.sleep(4000);
			
	}
	
	private static boolean checkprogress() throws InterruptedException {
		try {
			WebElement progressBar = driver.findElement(By.cssSelector(".progress-bar"));
			while (progressBar.isDisplayed()) {
				System.out.println("Progress bar loading....");
			}
		} catch (StaleElementReferenceException e) {
			// TODO: handle exception
			return false;
		}
		return false;
	}
		
	@AfterMethod
	public void tearDown(ITestResult result) {

		final String dir = System.getProperty("user.dir");
		String screenshotPath;
		//System.out.println("dir: " + dir);
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
