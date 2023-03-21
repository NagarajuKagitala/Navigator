package ApodGUI;

import java.io.File;
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
public class ListenerViewlet  
{
	String FinalListenerName="";
	static WebDriver driver;
	static String Screenshotpath;
	static String DownloadPath;
	static String WGSName;
	static String Manager1;
	static String Manager2;
	static String Dnode;
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
		DownloadPath =Settings.getDownloadPath();
		WGSName =Settings.getWGSNAME();
		Dnode =Settings.getDnode();
		Manager1 =Settings.getManager1();
		Manager2 =Settings.getManager2();
		Node_Hostname =Settings.getNode_Hostname();
		Low =Settings.getLow();
		Medium =Settings.getMedium();
		High =Settings.getHigh();
		LowSleep=Integer.parseInt(Low);
		MediumSleep =Integer.parseInt(Medium);
		HighSleep=Integer.parseInt(High);
	}

	@Parameters({"sDriver", "sDriverpath", "Dashboardname"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname) throws Exception
	{
		Settings.read();
		String URL = Settings.getSettingURL();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			ChromeOptions options = new ChromeOptions(); 
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
		
		//ChromeDriver chromeDriver = new ChromeDriver();
		/*
		 * driver.get("chrome://settings/clearBrowserData"); Thread.sleep(5000); //
		 * driver.findElement(By.id("checkbox")).sendKeys(Keys.ENTER);
		 * driver.findElement(By.xpath("//settings-ui")).sendKeys(Keys.ENTER);
		 * Thread.sleep(6000);
		 */

	   
		
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		
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
		Thread.sleep(4000);
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		
		
		/*driver.findElement(By.id("createInitialViewlets")).click();
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByIndex(Integer.parseInt(WGS_INDEX));
		
		//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(MediumSleep);
	}
	
	@Parameters({"ViewletName", "ViewletValue"})
	@TestRail(testCaseId=148)
	@Test(priority=1)
	public static void AddListenerViewlet(String ViewletName, int ViewletValue, ITestContext context) throws InterruptedException
	{
		Viewlets obj=new Viewlets();
		obj.IBMMQViewlet(driver, ViewletValue, ViewletName, WGSName, Node_Hostname);
		
		if(driver.getPageSource().contains(ViewletName))
		{
			System.out.println("Listener Viewlet is created");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Listener viewlet is created successfully");
		}
		else
		{
			System.out.println("Listner viewlet is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create Listener viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
		Thread.sleep(1000);
	    }
	
	@Parameters({"Dashboardname", "schemaName"})
	@TestRail(testCaseId=149)
	@Test(priority=29)
	public static void ShowObjectAttributesForListener(String Dashboardname, String schemaName, ITestContext context) throws InterruptedException
	{
		try {
		ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.ObjectAttributesVerification(Dashboardname, driver, schemaName, WGSName);
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "Show object attributes for listeners are working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Faile to show object attributes for listeners, check details: "+ e.getMessage());
			driver.findElement(By.id("Attributes failed")).click();
		}
	}
	
	@Parameters({"Dashboardname", "ListenerName", "Description"})
	@TestRail(testCaseId=150)
	@Test(priority=3)
	public void CreateListener(String Dashboardname, String ListenerName, String Description, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//click on checkbox and choose create listener
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Create Listener")).click();
		Thread.sleep(LowSleep);
		
		//Create page 
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(ListenerName);
		Thread.sleep(LowSleep);
		
		//Description
		driver.findElement(By.id("description")).sendKeys(Description);
		Thread.sleep(LowSleep);
		//Click on OK button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
				
		try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(LowSleep);
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
		
		//Search with the added Listername name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ListenerName);
    	Thread.sleep(LowSleep);
		
		//Store the viewlet data into string
		String Listenerdata=driver.findElement(By.xpath("//datatable-body")).getText();
		
		//Edit the search field data
    	for(int j=0; j<=ListenerName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(LowSleep);
    	
		//Verification condition
		if(Listenerdata.contains(ListenerName))
		{
			System.out.println("Listener is created successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Listener is created successfully");
		}
		else
		{
			System.out.println("Listener is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create listeners");
			driver.findElement(By.xpath("Listener viewlet Failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"ListenerName"})
	@TestRail(testCaseId=151)
	@Test(priority=4)
	public void StartListener(String ListenerName, ITestContext context) throws InterruptedException
	{
		/*//Search with the added process name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ListenerName);
    	Thread.sleep(1000);*/
    	
    	//Select Start From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
    	driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li")).click();
    	Thread.sleep(2000);
    	
    	//Click on Confirmation
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(4000);
    	
    	//Store the Listener status into string
    	String Status=driver.findElement(By.cssSelector(".active > .datatable-body-cell-label > .ng-star-inserted")).getText();
    	System.out.println(Status);
    	
    	if(Status.equalsIgnoreCase("Running") || Status.equalsIgnoreCase("Starting"))
    	{
    		System.out.println("Listener is Running");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Listener is started using start command");
    	}
    	else
    	{
    		System.out.println("Listener is not Running");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to start listener using start command");
    		driver.findElement(By.xpath("Running failed")).click();
    	}
    	Thread.sleep(1000);
		
	}
	
	@Parameters({"ListenerName"})
	@TestRail(testCaseId=152)
	@Test(priority=5)
	public void StopListener(String ListenerName, ITestContext context) throws InterruptedException
	{
		/*//Search with the added process name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ListenerName);
    	Thread.sleep(1000);*/
    	
    	//Select Stop From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
    	driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li[2]")).click();
    	
    	//Click on Confirmation
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(4000);
    	
    	//Store the Listener status into string
    	String Status=driver.findElement(By.cssSelector(".active > .datatable-body-cell-label > .ng-star-inserted")).getText();
    	System.out.println(Status);
    	
    	if(Status.equalsIgnoreCase("Stopping") || Status.equalsIgnoreCase("Stopped"))
    	{
    		System.out.println("Listener is Stoped");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Listener is stopped using stop command");
    	}
    	else
    	{
    		System.out.println("Listener is not Stopped");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to stop listener using stop command");
    		driver.findElement(By.xpath("Stopping failed")).click();
    	}
    	Thread.sleep(1000);
    	
		
	}
	
	@Parameters({"Dashboardname", "CopyObjectName", "ListenerName"})
	@TestRail(testCaseId=153)
	@Test(priority=6, dependsOnMethods= {"CreateListener"})
	public void CopyAsFromCommands(String Dashboardname, String CopyObjectName, String ListenerName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with the added process name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ListenerName);
    	Thread.sleep(LowSleep);
    			
		//Select Copy as From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Copy As...")).click();
    	Thread.sleep(LowSleep);
    	
    	//Give the object name
    	driver.findElement(By.id("name")).sendKeys(CopyObjectName);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	
    	try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(3000);
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
    	    	
    	//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(LowSleep);
    	}
    	
    	//Combining the strings 
    	String CopyasListenerName=ListenerName+CopyObjectName;
    	System.out.println("Copied listener name is: " +CopyasListenerName);
    	    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println("Viewlet data is: " +Subviewlet);
    	
    	//Edit the search field data
    	for(int j=0; j<=ListenerName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(LowSleep);
    	
    	//Verification condition
    	if(Subviewlet.contains(CopyasListenerName))
    	{
    		System.out.println("Listener is copied");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Listener is copied using CopyAs command");
    	}
    	else
    	{
    		System.out.println("Listener is not copied");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to copy listener using CopyAs command");
    		driver.findElement(By.xpath("Listener failed to copy")).click();
    	}
    	Thread.sleep(1000);	
	}
	
	@Parameters({"Dashboardname", "RenameListener", "CopyObjectName", "ListenerName"})
	@TestRail(testCaseId=154)
	@Test(priority=7, dependsOnMethods= {"CopyAsFromCommands"})
	public void RenameFromCommands(String Dashboardname, String RenameListener, String CopyObjectName, String ListenerName, ITestContext context) throws InterruptedException
	{ 
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
    	//Combining the strings 
    	String CopyasListenerName=ListenerName+CopyObjectName;
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyasListenerName);
    	Thread.sleep(LowSleep);
    	    	
		//Select Rename From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Rename")).click();
    	Thread.sleep(LowSleep);
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//app-mod-viewlet-object-rename/div/div/div[2]/input")).sendKeys(RenameListener);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	
    	try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(3000);
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
    	
    	//Edit the search field data
    	for(int j=0; j<=CopyasListenerName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);	
    	
    	//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(LowSleep);
    	}
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameListener);
    	Thread.sleep(LowSleep);
    	
    	//Store the Subscription name into string
    	String ModifiedName=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println(ModifiedName);
    	
    	for(int k=0; k<=RenameListener.length(); k++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(1000);
    	
    	//Verification condition
    	if(ModifiedName.contains(RenameListener))
    	{
    		System.out.println("The Listener is renamed");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Listener is renamed using rename command");
    	}
    	else
    	{
    		System.out.println("The Listener rename is failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to rename listener using rename command");
    		driver.findElement(By.xpath("Rename for Listener is failed")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "RenameListener"})
	@TestRail(testCaseId=155)
	@Test(priority=8, dependsOnMethods= {"RenameFromCommands"})
	public void DeleteFromCommands(String Dashboardname, String RenameListener, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with the deleted listener name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameListener);
    	Thread.sleep(LowSleep);
    	    	
		//Select Delete From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Delete")).click();
    	Thread.sleep(LowSleep);
		
    	//Click on Yes
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Search with the new name
		for(int j=0; j<=RenameListener.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(LowSleep);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(RenameListener))
    	{
    		System.out.println("Listener is not deleted");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to delete listener using delete command");
    		driver.findElement(By.xpath("Listener delete failed")).click();
    	}
    	else
    	{
    		System.out.println("Listener is deleted");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Listener is deleted using delete command");
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 1001)
	@Test(priority=9)
	public void MQSCSnapshot(String Dashboardname, ITestContext context) throws InterruptedException
	{		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Save the Auth info name into string
		String Listenername=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println("Object name is: " +Listenername);
		
		//Select MQSCSnapshot option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("MQSC"))).perform();
    	driver.findElement(By.linkText("Snapshot...")).click();
    	Thread.sleep(LowSleep);
    	
    	//Get the snapshot data and store into string
    	String SnapshotData=driver.findElement(By.xpath("//textarea")).getText();
    	System.out.println("Snapshot data is: " +SnapshotData);
    	
    	//verification
    	if(SnapshotData.contains(Listenername))
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
			driver.findElement(By.id("Listener snapshot failed")).click();
    	}
	}
	
	
	@TestRail(testCaseId = 1002)
	@Test(priority=10, dependsOnMethods= {"MQSCSnapshot"})
	public void SaveListenerSnapshot(ITestContext context)
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
	@Test(priority=11)
	@TestRail(testCaseId=156)
	public void ListenerProperties(String Dashboardname, ITestContext context) throws InterruptedException
	{
		try
		{
			//Click on cancel button
			driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
			Thread.sleep(LowSleep);
		}
		catch (Exception e)
		{
			
		}
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//click on checkbox and choose properties
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(LowSleep);
		
		//storing the name field status into boolean
		boolean NameField=driver.findElement(By.id("name")).isEnabled();
		System.out.println(NameField);
		
		//Verification Condition
		if(NameField == false)
		{
			System.out.println("The Listener name field is Disabled");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "The Listener name field is Disabled");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(MediumSleep);
		}
		else
		{
			System.out.println("The Listener name field is Enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "The Listener name field is Enabled");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(MediumSleep);
			driver.findElement(By.xpath("Listener field is disabled")).click();
			
		}
		Thread.sleep(4000);
		
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=12)
	@TestRail(testCaseId=157)
	public static void ListenerEvents(String Dashboardname, ITestContext context) throws InterruptedException
	{
		AllEvents obj=new AllEvents();
		obj.Events(Dashboardname, driver, context);
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=158)
	@Test(priority=13)
	public static void AddToFavoriteViewlet(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{		
		//Store Listener name into string
		String ListenerName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		//Create favorite viewlet 
		driver.findElement(By.xpath("//button[3]")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.id("fav")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
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
		
		//Submit
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(MediumSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//click on checkbox and choose to Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
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
		Thread.sleep(MediumSleep);
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification of listener added to favorite viewlet
		if(Favdata.contains(ListenerName))
		{
			System.out.println("Listener name is added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "The Listener name is added to Favorite viewlet");
		}
		else
		{
			System.out.println("Listener name is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add Listener name to Favorite viewlet");
		
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
	}
		
	
	@Test(priority=14)
	@TestRail(testCaseId=159)
	public static void CompareListeners(ITestContext context) throws InterruptedException
	{
		CompareObjects com=new CompareObjects();
		com.Compare(driver, WGSName, context);
	}
	
	
	@TestRail(testCaseId = 776)
	@Test(priority=15)
	public void CheckDifferencesForListeners(ITestContext context) throws InterruptedException
	{
		DifferenceOfObjects diff=new DifferenceOfObjects();
		diff.Differences(driver, context);
	}
	
	@Parameters({"Dashboardname", "ListenerNameFromICon", "DescriptionFromIcon"})
	@TestRail(testCaseId=167)
	@Test(priority=16)
	public void CreateListenerFromPlusIcon(String Dashboardname, String ListenerNameFromICon, String DescriptionFromIcon, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		String[] Managers= {Manager1, Manager2};
		for(int m=0; m<=1; m++)
		{
		//Click on + icon present in the viewlet
		driver.findElement(By.xpath("//img[@title='Add Listener']")).click();
		Thread.sleep(LowSleep);
		
		//Select WGS
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
		
		/*
		 * //Select Node
		 * driver.findElement(By.xpath("//ng-select[2]/div/span")).click(); try {
		 * WebElement
		 * ChannelauthNode=driver.findElement(By.className("ng-dropdown-panel")).
		 * findElement(By.className("ng-dropdown-panel-items")); List<WebElement>
		 * divs=ChannelauthNode.findElements(By.tagName("div"));
		 * System.out.println(divs.size()); for (WebElement di : divs) {
		 * if(di.getText().equals(Dnode)) { di.click(); break; } } } catch(Exception ex)
		 * { ex.printStackTrace(); } Thread.sleep(2000);
		 */
		
		//Select Manager
		driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div[2]/ng-select/div/span")).click();
		Thread.sleep(LowSleep);
        try 
		{
        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
			List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
			System.out.println(mdivs.size());	
			
			for (WebElement mdi : mdivs)
			{
				if(mdi.getText().equals(Managers[m]))
				{
					mdi.click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
        Thread.sleep(1000);
		
		/*//Select Node 
		driver.findElement(By.xpath("//div[2]/input")).click();
		driver.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div")).click();
		
		//Select Manager
         driver.findElement(By.xpath("//div[2]/ng-select/div")).click();
	     //driver.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div")).click();
         driver.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div[2]")).click();*/
		
		//Click on Select path button
        driver.findElement(By.xpath("//button[contains(.,'Select path')]")).click();
		Thread.sleep(LowSleep);
		
		//Create page
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(ListenerNameFromICon);
		Thread.sleep(LowSleep);
		
		//Description
		driver.findElement(By.id("description")).sendKeys(DescriptionFromIcon);
		Thread.sleep(LowSleep);
		
		//Click on OK button
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(HighSleep);
				
		try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).click();
			//driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(LowSleep);
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
		
		//Click on Refresh
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(LowSleep);
		
		//Search option
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ListenerNameFromICon);
		Thread.sleep(LowSleep);
		
		//Store the viewlet data into string
		String Listenerdata=driver.findElement(By.xpath("//datatable-body")).getText();
		
		//Edit the search field data
    	for(int j=0; j<=ListenerNameFromICon.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(LowSleep);
		
    	//Verification condition
		if(Listenerdata.contains(ListenerNameFromICon))
		{
			System.out.println("Listener is created successfully from plus ICon");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Listener is created successfully using add Icon");
		}
		else
		{
			System.out.println("Listener is not created from Plus Icon");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Failed to create Listener using add Icon");
			driver.findElement(By.xpath("Listener viewlet Failed")).click();
		}
		Thread.sleep(1000);
	}
	}
	
	@Parameters({"ListenerName"})
	@TestRail(testCaseId=160)
	@Test(priority=17)
	public void StartListenerForMultiple(String ListenerName, ITestContext context) throws InterruptedException
	{
	/*	//Search with the added process name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ListenerName);
    	Thread.sleep(1000);*/
		    	
    	//Select Start From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]"))).perform();
    	driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]/ul/li")).click();
    	Thread.sleep(LowSleep);
    	
    	//Click on Confirmation
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(4000);
    	
    	//Store the Listener status into string
    	String Status1=driver.findElement(By.cssSelector(".active > .datatable-body-cell-label > .ng-star-inserted")).getText();
    	System.out.println(Status1);
    	String Status2=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[9]/div/span")).getText();
    	System.out.println(Status2);
    	
    	if(Status1.equalsIgnoreCase("Running") || Status1.equalsIgnoreCase("Starting") && Status2.equalsIgnoreCase("Running") || Status2.equalsIgnoreCase("Starting"))
    	{
    		System.out.println("Multiple Listeners are Running");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple listeners are started using start command");
    	}
    	else
    	{
    		System.out.println("Multiple Listeners are not Running");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to start Multiple listeners using start command");
    		driver.findElement(By.xpath("Running failed")).click();
    	}
    	Thread.sleep(1000);
		
	}
	
	@Parameters({"ListenerName"})
	@TestRail(testCaseId=161)
	@Test(priority=18)
	public void StopListenerForMultiple(String ListenerName, ITestContext context) throws InterruptedException
	{
		/*//Search with the added process name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ListenerName);
    	Thread.sleep(1000);*/
    	
    	//Select Stop From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]"))).perform();
    	driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[2]/ul/li[2]")).click();
    	Thread.sleep(2000);
    	
    	try {
    	//Click on Confirmation
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(4000);
    	
    	
    	
    	//Store the Listener status into string
    	String Status1=driver.findElement(By.cssSelector(".active > .datatable-body-cell-label > .ng-star-inserted")).getText();
    	System.out.println(Status1);
    	String Status2=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[9]/div/span")).getText();
    	System.out.println(Status2);
    	
    	if(Status1.equalsIgnoreCase("Stopping") || Status1.equalsIgnoreCase("Stopped") && Status1.equalsIgnoreCase("Stopping") || Status1.equalsIgnoreCase("Stopped"))
    	{
    		System.out.println("Multiple Listeners are Stoped");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple listeners are stpped using stop command");
    	}
    	else
    	{
    		System.out.println("Multiple Listeners are copyied not Stopped");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to stop Multiple listeners using stop command");
    		driver.findElement(By.xpath("Stopping failed")).click();
    	}
    	Thread.sleep(1000);
    	}
    	catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
    		driver.findElement(By.cssSelector(".btn-danger")).click();
		}
	}
	
	
	@Parameters({"Dashboardname", "CopyObjectNameForMUltiple", "ListenerName", "ListenerNameFromICon"})
	@TestRail(testCaseId=162)
	@Test(priority=19, dependsOnMethods= {"CreateListenerFromPlusIcon"})
	public void CopyAsFromCommandsForMultiple(String Dashboardname, String CopyObjectNameForMUltiple, String ListenerName, String ListenerNameFromICon, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ListenerNameFromICon);
    	Thread.sleep(LowSleep);
    	   	
		//Select Copy as From commands
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Copy As...")).click();
    	Thread.sleep(LowSleep);
    	
    	//Get the existing name
    	String ExistingListener=driver.findElement(By.id("name")).getAttribute("value");
    	System.out.println("Existing listener name is: " +ExistingListener);
    	
    	//Give the object name
    	driver.findElement(By.id("name")).sendKeys(CopyObjectNameForMUltiple);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	
    	try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(LowSleep);
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
    	
    	for(int j=0; j<=ListenerNameFromICon.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	FinalListenerName=ExistingListener+CopyObjectNameForMUltiple;
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(LowSleep);
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinalListenerName);
    	Thread.sleep(LowSleep);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	for(int j=0; j<=FinalListenerName.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification condition
    	if(Subviewlet.contains(FinalListenerName))
    	{
    		System.out.println("Multiple Listeners are copied");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple listeners are copied using CopyAs command");
    	}
    	else
    	{
    		System.out.println("Multiple Listeners are not copied");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to copy Multiple listeners using CopyAs command");
    		driver.findElement(By.xpath("Listener failed to copy")).click();
    	}
    	Thread.sleep(1000);	
	}
	
	@Parameters({"Dashboardname", "RenameListenerForMultiple", "CopyObjectNameForMUltiple"})
	@TestRail(testCaseId=163)
	@Test(priority=20, dependsOnMethods= {"CopyAsFromCommandsForMultiple"})
	public void RenameFromCommandsForMultiple(String Dashboardname, String RenameListenerForMultiple, String CopyObjectNameForMUltiple, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinalListenerName);
    	Thread.sleep(LowSleep);
    	    	
		//Select Rename From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Rename")).click();
    	Thread.sleep(LowSleep);
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//app-mod-viewlet-object-rename/div/div/div[2]/input")).sendKeys(RenameListenerForMultiple);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	
    	try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(LowSleep);
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
    	
    	for(int k=0; k<=FinalListenerName.length(); k++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameListenerForMultiple);
    	Thread.sleep(LowSleep);  	
    	
    	//Store the Subscription name into string
    	String ModifiedName=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println(ModifiedName);
    	
    	for(int j=0; j<=RenameListenerForMultiple.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification condition
    	if(ModifiedName.contains(RenameListenerForMultiple))
    	{
    		System.out.println("Multiple Listeners ares renamed");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple listeners are renamed using rename command");
    	}
    	else
    	{
    		System.out.println("Multiple Listeners rename is failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to rename Multiple listeners using rename command");
    		driver.findElement(By.xpath("Rename for Listener is failed")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "RenameListenerForMultiple"})
	@TestRail(testCaseId=164)
	@Test(priority=21, dependsOnMethods= {"RenameFromCommandsForMultiple"})
	public void DeleteFromCommandsForMultiple(String Dashboardname, String RenameListenerForMultiple, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameListenerForMultiple);
    	Thread.sleep(LowSleep);
    	    	
		//Select Delete From commands
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Delete")).click();
    	Thread.sleep(MediumSleep);
		
    	//Click on Yes
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	for(int j=0; j<=RenameListenerForMultiple.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(RenameListenerForMultiple))
    	{
    		System.out.println("Multiple Listener are not deleted");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to delete Multiple listeners using delete command");
    		driver.findElement(By.xpath("Multiple Listeners are failed")).click();
    	}
    	else
    	{
    		System.out.println("Listener is deleted");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple listeners are deleted using delete command");
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "ListenerDescription"})
	@TestRail(testCaseId=165)
	@Test(priority=22)
	public void ListenerMultipleProperties(String Dashboardname, String ListenerDescription, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		String TransportType=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[6]/div/span")).getText();
		System.out.println("Transport type is: " +TransportType);
		
		//Search with tranport type
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(TransportType);
    	Thread.sleep(LowSleep);
    			
		//Select Two Listeners and choose Add to favorite viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(LowSleep);
		
		try {
		//give the description
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys(ListenerDescription);
		Thread.sleep(LowSleep);
	
		//Close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//Search with tranport type
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(TransportType);
    	Thread.sleep(LowSleep);
		
		//Open the first listener properties page
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(LowSleep);
		
		//Store the First listener description into string
		String FirstDescription=driver.findElement(By.id("description")).getAttribute("value");
		
		//Close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che2=new ClearSelectionofCheckbox();
		che2.Deselectcheckbox(Dashboardname, driver);
		
		//Search with tranport type
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(TransportType);
    	Thread.sleep(LowSleep);
		
		//Open the second listener name
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(LowSleep);
		
		//Store the second listener description into string
		String SecondDescription=driver.findElement(By.id("description")).getAttribute("value");
		
		//Close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		for(int j=0; j<=TransportType.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
		
		//Verification
		if(FirstDescription.equals(ListenerDescription) && SecondDescription.equals(ListenerDescription))
		{
			System.out.println("Multiple listener properties verified");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple listener properties are verified successfully");
		}
		else
		{
			System.out.println("Multiple listener properties not verified");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to verify Multiple listener properties");
			driver.findElement(By.id("Multiple properties failed")).click();
		}
		Thread.sleep(1000);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
    		driver.findElement(By.cssSelector(".btn-danger")).click();
		}
		
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=166)
	@Test(priority=23, dependsOnMethods= {"AddToFavoriteViewlet"})
	public static void AddToFavoriteForMultipleListeners(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{		
		//Store the Listeners into strings 
		String Listener2=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		String Listener3=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select Two Listeners and choose Add to favorite viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
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
		Thread.sleep(MediumSleep);
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification of listeners added to favorite to favorite viewlet
		if(Favdata.contains(Listener2) && Favdata.contains(Listener3))
		{
			System.out.println("Multiple Listener names are added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple listener properties added successfully to Favorite viewlet");
		}
		else
		{
			System.out.println("Multiple Listener names are not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add Multiple listener properties to Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=30)
	public void Logout(String Dashboardname) throws InterruptedException
	{		
		//Logout
		LogoutForAll lo=new LogoutForAll();
		lo.LogoutMethod(driver, Dashboardname);
	}
	
	private static boolean checkprogress() throws InterruptedException {
		try {
			WebElement progressBar = driver.findElement(By.cssSelector(".progress-bar"));
			while (progressBar.isDisplayed()) {
				System.out.println("Progress bar loading....");
				Thread.sleep(1000);
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

