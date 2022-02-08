package EMS;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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
public class EMSQueues 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String WGSName;
	static String EMS_WGSNAME;
	static String CopyAsQueue="";
	static String EMSNode;
	static String EMSQueueManager;
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
		EMS_WGSNAME =Settings.getEMS_WGSNAME();
		EMSNode =Settings.getEMSNode();
		EMSQueueManager =Settings.getEMSQueueManager();
		Low =Settings.getLow();
		Medium =Settings.getMedium();
		High =Settings.getHigh();
		LowSleep=Integer.parseInt(Low);
		MediumSleep =Integer.parseInt(Medium);
		HighSleep=Integer.parseInt(High);
	}
	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname", "ViewletValue", "ViewletName"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname, int ViewletValue, String ViewletName) throws Exception
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
		
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(150, TimeUnit.SECONDS);
		
		//Login page
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(8000);
		
		//Delete existing dashboard
		Dashboard ob=new Dashboard();
		ob.DeleteExistDashboard(driver, Dashboardname);
		
		//Click on Create button
		//driver.findElement(By.xpath("//app-side-dashboard-menu/div/div/div[2]/div[2]")).click();				
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		Thread.sleep(4000);
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		        
		        
		//driver.findElement(By.id("createInitialViewlets")).click();
		
		
		//Work group server selection
		/*Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByIndex(wgs);
		
		//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create Dashboard button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(2000);
		
		//--------- Create Queue viewlet-----------	
		Viewlets obj=new Viewlets();
		obj.EMSViewlet(driver, ViewletValue, ViewletName, EMS_WGSNAME, EMSNode);
		
		//Restore Default settings
		  driver.findElement(By.cssSelector(".fa-cog")).click();
		  Thread.sleep(4000);
		  driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click(); 
		  Thread.sleep(4000);
		  driver.findElement(By.id("accept-true")).click();
		  Thread.sleep(4000);
		  
		  //UnCheck Show Empty Queues check box
			//driver.findElement(By.cssSelector(".fa-cog")).click();
			Thread.sleep(4000);
			boolean queues=driver.findElement(By.id("empty-queues")).isEnabled();
			if(queues)
			{
				driver.findElement(By.id("empty-queues")).click();
			}
			else
			{
				
			}
			Thread.sleep(3000);
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
			Thread.sleep(6000);
		
		
	}
	@Parameters({"Dashboardname"})
	@Test(priority=1)
	@TestRail(testCaseId=290)
	public static void BrowseMessages(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select Browse Messages Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Browse messages")).click();
		Thread.sleep(8000);               
		
		//Store the browse message page value into string
		String BrowseMessagespage=driver.findElement(By.cssSelector(".g-row-head:nth-child(2)")).getText();
		System.out.println(BrowseMessagespage);
		
		//verification
		if(BrowseMessagespage.equalsIgnoreCase("Message Cursor"))
		{
			System.out.println("Message browse page is opened");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Message browse page is opened");
		}
		else
		{
			System.out.println("Message browse page is not opened");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to open message browse page");
			driver.findElement(By.cssSelector(".close-button")).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath("Message browse page is failed")).click();
		}
		//Close the popup page
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId=790)
	@Parameters({"Dashboardname", "SchemaName"})
	@Test(priority=2)
	public static void ShowObjectAttributesForQueues(String Dashboardname, String SchemaName, ITestContext context) throws InterruptedException
	{	try {
		EMSObjects obj=new EMSObjects();
		obj.EMSQueueObjectAttributesVerification(Dashboardname, driver, SchemaName, EMS_WGSNAME);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Show object attributes working fine");
		
		}catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got exception while showing object attributes, check details: "+  e.getMessage());
			driver.findElement(By.id("Attributes failed")).click();
		}
		
	}
	@Parameters({"Dashboardname"})
	@Test(priority=3)
	@TestRail(testCaseId=291)
	public static void ShowQueueStatus(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Get the pending count of queue
		String Count=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println("EMS Queue name is: " +Count);
		Thread.sleep(2000);
		
		//Select show object Attributes Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show EMS Queue Status")).click();
		Thread.sleep(4000);
		
		//Store the column name "Name" into string
		String Queuestatuspage=driver.findElement(By.xpath("//td[2]")).getText();
		System.out.println("Name of EMS Queue is: " +Queuestatuspage);
		
		//Verification condition
		if(Queuestatuspage.equalsIgnoreCase(Count))
		{
			System.out.println("Show Queue Status page is opened");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show Queue Status page is opened");
		}
		else
		{
			System.out.println("Show Queue Status page is not opened");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to open Queue Status page");
			driver.findElement(By.xpath("Queue Status page opening is failed")).click();
		}
		
		//Close the Queue status page
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(1000);
			
	}
	
	@Parameters({"Dashboardname", "QueueNameFromOptions"})
	@TestRail(testCaseId=292)
	@Test(priority=4)
	public void CreateQueueFromOptions(String Dashboardname, String QueueNameFromOptions, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
	
		//Select show object Attributes Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Create EMS Queue")).click();
		Thread.sleep(8000);
		
		//Create Queue Window
		driver.findElement(By.name("name")).sendKeys(QueueNameFromOptions);
		Thread.sleep(10000);
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(10000);
		
		try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(3000);
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(3000);
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
		Thread.sleep(3000);
		
		//Change the Settings We need to check show empty queues for verification
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.id("empty-queues")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(2000);
		
		//Refresh the viewlet
		for(int i=0; i<=4; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);
		}
		
		//Search with input data
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(QueueNameFromOptions);
		
		//Store the first queue name into string
		String QueueData=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println(QueueData); 
		
		for(int i=0; i<=QueueNameFromOptions.length(); i++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Verification 
		if(QueueData.contains(QueueNameFromOptions))
		{
			System.out.println("Queue is created From options");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Queue is created From options");
		}
		else
		{
			System.out.println("Queue is not Created from options");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Queue is not created From options");
			driver.findElement(By.xpath("Queue creation failed")).click();
		}
			
		Thread.sleep(2000);
			
	}
	
	@Parameters({"Dashboardname", "ObjectName", "QueueNameFromOptions"})
	@TestRail(testCaseId=293) 
	@Test(priority=5, dependsOnMethods= {"CreateQueueFromOptions"})
	public static void CopyAsOptionFromQueueCommands(String Dashboardname, String ObjectName, String QueueNameFromOptions, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with input data
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(QueueNameFromOptions);
				
		//Select copy as option from Commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions CopyMousehover=new Actions(driver);
		CopyMousehover.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Copy As...")).click();
		Thread.sleep(4000);             
		
		//Store the Copy Queue name
		String CopyQueue=driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).getAttribute("value");
		System.out.println("Copy Queue name is:" +CopyQueue); 
		Thread.sleep(2000);
		
		//Object Details  
		//driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).clear();
		driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).sendKeys(ObjectName);
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(3000);
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(3000);
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
		
		CopyAsQueue=CopyQueue+ObjectName;
		System.out.println("Copy as Queue name is:" +CopyAsQueue);
		
		for(int i=0; i<=QueueNameFromOptions.length(); i++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Refresh the viewlet
		for(int i=0; i<=4; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);
		}
		
		//Search with input data
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyAsQueue);
		Thread.sleep(2000);
		
		//Store the first queue name into string
		String QueueData=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println(QueueData); 
		
		for(int j=0; j<=CopyAsQueue.length(); j++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Verification 
		if(QueueData.contains(CopyAsQueue))
		{
			System.out.println("Copy as option is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Copy as option is working fine");
		}
		else
		{
			System.out.println("Copy as option is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Copy as option is not working");
			driver.findElement(By.xpath("Queue creation failed")).click();
		}
		Thread.sleep(4000);
		
	}
	
	@TestRail(testCaseId = 1075)
	@Parameters({"Dashboardname", "RenameQueue"})
	@Test(priority=6)
	public void RenameQueueFromCommandsOption(String Dashboardname, String RenameQueue, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		Thread.sleep(2000);
		
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyAsQueue);
    	Thread.sleep(10000); 
    	
		//Select Rename From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Rename")).click();
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//app-mod-viewlet-object-rename/div/div/div[2]/input")).clear();
    	driver.findElement(By.xpath("//app-mod-viewlet-object-rename/div/div/div[2]/input")).sendKeys(RenameQueue);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(5000);
    	driver.findElement(By.id("accept-true")).click();
    	Thread.sleep(5000);
    	    	
    	//Edit the search field data
    	for(int i=0; i<=CopyAsQueue.length(); i++)
    	{
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(10000);	
    	
    	//Refresh the viewlet
    	for(int i=0; i<=4; i++)
    	{
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(4000);
    	}
    	
    	//Search with renamed name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameQueue);
    	Thread.sleep(1000); 
    	
    	//Store the Subscription name into string
    	String ModifiedName=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println("Viewlet data after Rename: " +ModifiedName);
    	
    	//Edit the new name
		for(int j=0; j<=RenameQueue.length(); j++)
    	{
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	//Verification condition
    	if(ModifiedName.contains(RenameQueue))
    	{
    		System.out.println("The Queue is renamed");
    		context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Rename command is working fine");
    	}
    	else
    	{
    		System.out.println("The Queue rename is failed");
    		context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Queue rename command failed");
    		driver.findElement(By.xpath("Rename for Queue is failed")).click();
    	}
    	Thread.sleep(1000);
		
	}
	
	
	@Parameters({"Dashboardname", "RenameQueue"})
	@TestRail(testCaseId=791)
	@Test(priority=7, dependsOnMethods= {"RenameQueueFromCommandsOption"})
	public void DeleteoptionFromQueueCommands(String Dashboardname, String RenameQueue, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with input data
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameQueue);
		
		//Select Commands option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions DeleteMousehover=new Actions(driver);
		DeleteMousehover.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Delete Queue")).click();
		Thread.sleep(1000);
		
		//Delete option
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		for(int i=0; i<=RenameQueue.length(); i++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Click on refresh button
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(6000);
		
		//Store the first queue name into string
		String AfterDeleteQueueData=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println(AfterDeleteQueueData);
		
		if(AfterDeleteQueueData.contains(RenameQueue))
		{
			System.out.println("Queue Deletion failed");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Delete option is not working");
			driver.findElement(By.xpath("Queue Delete failed")).click();
		}
		else
		{
			System.out.println("Queue is deleted successfully");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Queue Delete option is working fine");
		}	
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=8)
	@TestRail(testCaseId=294)
	public static void QueueProperties(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Change the Settings We need to check show empty queues for verification
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.id("empty-queues")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(2000);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select Queue properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(8000);
		
		boolean FieldNamevalue=driver.findElement(By.id("name")).isEnabled();
		System.out.println(FieldNamevalue);
		
		//Verification
		if(FieldNamevalue == false)
		{
			System.out.println("Queue Name field is UnEditable");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Queue Name field is UnEditable");
			 driver.findElement(By.cssSelector(".btn-primary")).click();
			 
			 try
			 {
				 driver.findElement(By.id("yes")).click();
				 driver.findElement(By.cssSelector(".btn-danger")).click();
			 }
			 catch (Exception e)
			 {
				 System.out.println("OK button is not working");
			 }
		}
		else
		{
			System.out.println("Queue Name field is Editable");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Queue Name field is Editable");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			 try
			 {
				 driver.findElement(By.id("yes")).click();
				 driver.findElement(By.cssSelector(".btn-danger")).click();
			 }
			 catch (Exception e)
			 {
				 System.out.println("OK button is not working");
			 }
			driver.findElement(By.xpath("Queue name edit function Failed")).click();
			
		}
	}
	@Parameters({"Dashboardname"})
	@Test(priority=9)
	@TestRail(testCaseId=295)
	public static void QueueEvents(String Dashboardname, ITestContext context) throws InterruptedException
	{
		AllEvents obj=new AllEvents();
		obj.Events(Dashboardname, driver, context);
	}
	
	@TestRail(testCaseId=793)
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@Test(priority=10)
	public void AddToFavoriteViewlet(String Dashboardname, String FavoriteViewletName) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the viewlet option and select the favorite checkbox
		driver.findElement(By.xpath("//button[3]")).click();
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.id("viewlet-type-ok")).click();
		
		//Viewlet Name
		driver.findElement(By.name("viewlet-name")).click();
		driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
		
		//Select WGS dropdown
		driver.findElement(By.xpath("//app-modal-add-viewlet-favorite/div/div/div[2]/div/ng-select/div/span")).click();
		Thread.sleep(3000);
		
		WebElement drop1=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
		List<WebElement> div1=drop1.findElements(By.tagName("div"));
		//System.out.println("Number of divs are: " +div.size());
		
		for(WebElement di1 : div1)
		{
			//System.out.println("text is :" +di.getText());
			if(di1.getText().equalsIgnoreCase(EMS_WGSNAME))
			{
				di1.click();
				break;
			}	
		}
		Thread.sleep(2000);
		
		//Submit
		driver.findElement(By.cssSelector(".g-block-bottom-buttons > .g-button-blue")).click();
		Thread.sleep(2000);
				
		//Store Queue name
		String QueueFav=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		
		//Select Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(3000);
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(3000);
		
		//Select the favorite viewlet name
		driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div/ng-select/div")).click();
		Thread.sleep(3000); 
		
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
		
		driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div[2]/button[2]")).click();
		Thread.sleep(6000);
		
		/*
		 * Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		 * fav.selectByVisibleText(FavoriteViewletName); Thread.sleep(2000);
		 * driver.findElement(By.
		 * cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue"
		 * )).click(); Thread.sleep(2000);
		 */
		
		//Store favorite viewlet data into string
		//String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		String Favdata=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		
		//verification
		if(Favdata.contains(QueueFav))
		{
			System.out.println("Queue is added to favorite viewlet");
		}
		else
		{
			System.out.println("Queue is not added to favorite viewlet");
			driver.findElement(By.xpath("Queue not add to favorite")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@Test(priority=11)
	@TestRail(testCaseId=296)
	public static void CompareQueues(ITestContext context) throws InterruptedException
	{
		CompareObjects com=new CompareObjects();
		com.Compare(driver, WGSName, context);
	}
	
	@TestRail(testCaseId=792)
	@Test(priority=12)
	public void CheckDifferencesForEMSQueues(ITestContext context) throws InterruptedException
	{
		DifferenceOfObjects diff=new DifferenceOfObjects();
		diff.Differences(driver, context);
	}
	@Parameters({"Dashboardname"})
	@Test(priority=13)
	@TestRail(testCaseId=297)
	public static void ShowQueueStatusForMultipleQueues(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Get the first queue count
		String CountQ1=driver.findElement(By.xpath("//datatable-body-cell[8]/div/span")).getText();
		System.out.println("First queue count: " +CountQ1);
		
		//get the Second queue count
		String CountQ2=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[8]/div/span")).getText();
		System.out.println("Second queue count : " +CountQ2);
		
		//Select show queue status option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show EMS Queues Status")).click();
		Thread.sleep(4000);
		
		//Store the column name "Name" into string
		String Queuestatus1=driver.findElement(By.xpath("//tr[3]/td[3]")).getText();
		System.out.println("Q1 status: " +Queuestatus1);
		
		//Store the Queue to status
		String Queuestatus2=driver.findElement(By.xpath("//tr[6]/td[3]")).getText();
		System.out.println("Q2 status :" +Queuestatus2);
		
		//Verification condition
		if(Queuestatus1.equalsIgnoreCase(CountQ1) && Queuestatus2.equalsIgnoreCase(CountQ2))
		{
			System.out.println("Show Multiple Queue Status page is opened");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show Multiple Queue Status page is opened");
		}
		else
		{
			System.out.println("Show Multiple Queue Status page is opened");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to open Multiple Queue Status page");
			driver.findElement(By.xpath("Queue Status page opening is failed")).click();
		}
		
		//Close the Queue status page
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "TestDescription"})
	@TestRail(testCaseId=298)
	@Test(priority=14)
	public void MultipleQueueProperties(String Dashboardname, String TestDescription, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(8000);
		
		WebElement Top=driver.findElement(By.id("name"));
		Actions a=new Actions(driver);
		a.moveToElement(Top).perform();
		
		//Get tooltip data
		String Tooltipdata=driver.findElement(By.tagName("ngb-tooltip-window")).getText();
		System.out.println("Tooltip data is: " +Tooltipdata);
		
		//close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		 try
		 {
			 driver.findElement(By.id("yes")).click();
			 driver.findElement(By.cssSelector(".btn-danger")).click();
		 }
		 catch (Exception e)
		 {
			 System.out.println("OK button is working");
		 }
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
			
		//Open the properties of first queue
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(6000);
		
		//Save the Connection URL value into string
		String FirstQueuedata=driver.findElement(By.id("name")).getAttribute("value");
		System.out.println("First Queue value is: " +FirstQueuedata);
		
		//close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(4000);
		try
		 {
			 driver.findElement(By.id("yes")).click();
			 driver.findElement(By.cssSelector(".btn-danger")).click();
		 }
		 catch (Exception e)
		 {
			 System.out.println("OK button is working");
		 }
		Thread.sleep(2000);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che2=new ClearSelectionofCheckbox();
		che2.Deselectcheckbox(Dashboardname, driver);
				
		//Open the properties of Second queue
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(6000);
		
		//Save the Connection URL value into string
		String SecondQueuedata=driver.findElement(By.id("name")).getAttribute("value");
		System.out.println("Second Queue data is: " +SecondQueuedata);
		
		//close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(4000);
		try
		 {
			 driver.findElement(By.id("yes")).click();
			 driver.findElement(By.cssSelector(".btn-danger")).click();
		 }
		 catch (Exception e)
		 {
			 System.out.println("OK button is working");
		 }
		
		//Verification
		if(Tooltipdata.contains(FirstQueuedata) && Tooltipdata.contains(SecondQueuedata))
		{
			System.out.println("multiple properties working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show multiple properties are working fine");
		}
		else
		{
			System.out.println("multiple properties are not wotrking");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to open multiple properties");
			driver.findElement(By.id("multiple properties failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@TestRail(testCaseId=794)
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@Test(priority=15, dependsOnMethods= {"AddToFavoriteViewlet"})
	public static void AddToFavoriteForMultipleQueues(String Dashboardname, String FavoriteViewletName) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Store Queue names
		String Queue2=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		String Queue3=driver.findElement(By.xpath("//datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		//Select Queue Events option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(1000);
		
		//Select the favorite viewlet name
		driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div/ng-select/div")).click();
		Thread.sleep(3000); 
		
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
		Thread.sleep(6000);
		
		/*
		 * Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		 * fav.selectByVisibleText(FavoriteViewletName); Thread.sleep(2000);
		 * driver.findElement(By.
		 * cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue"
		 * )).click(); Thread.sleep(2000);
		 */
		
	
		//Store favorite viewlet data into string
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification
		if(Favdata.contains(Queue2)  && Favdata.contains(Queue3))
		{
			System.out.println("Multiple Queues are added to Favorite viewlet");
		}
		else
		{
			System.out.println("Multiple Queues are not added to Favorite viewlet");
			driver.findElement(By.xpath("Multiple queues to fav failed")).click();
		}
		Thread.sleep(1000);
	}
	
	
	@Parameters({"Dashboardname", "QueueName", "QueueDescription"})
	@TestRail(testCaseId=299)
	@Test(priority=16)
	public void AddQueueFromPlusIcon(String Dashboardname, String QueueName, String QueueDescription, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		// Changing the Settings 
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click();
		Thread.sleep(4000);
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		
		try
		{
		//Click on + icon
		driver.findElement(By.id("add-object")).click();
		
		//Select WGS
		/*
		 * Select WGS=new Select(driver.findElement(By.xpath("//select")));
		 * WGS.selectByVisibleText(EMS_WGSNAME); Thread.sleep(3000);
		 */
		
		driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/ng-select/div/span")).click();
		try 
		{
			WebElement ChannelauthNode=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
			List<WebElement> divs=ChannelauthNode.findElements(By.tagName("div"));
			//System.out.println(divs.size());	
			for (WebElement di : divs)
			{					
				if(di.getText().equals(EMS_WGSNAME))
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
		 * if(di.getText().equals(EMSNode)) { di.click(); break; } } } catch(Exception
		 * ex) { ex.printStackTrace(); } Thread.sleep(2000);
		 */
		
		//Select Manager
		driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div[2]/ng-select/div/span")).click();
        try 
		{
        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
			List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
			System.out.println(mdivs.size());	
			
			for (WebElement mdi : mdivs)
			{
				if(mdi.getText().equals(EMSQueueManager))
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
 		
        driver.findElement(By.xpath("//button[contains(.,'Select path')]")).click();
		Thread.sleep(6000);
		
		//Create Queue Window
		driver.findElement(By.id("name")).sendKeys(QueueName);
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(6000);
		
		try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(3000);
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(3000);
		}
		catch (Exception e)
		{
			System.out.println("Error popup is not displayed");
		}
		
		//Refresh the viewlet
		for(int i=0; i<=4; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);
		}
				
		
		//Search with input data
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(QueueName);
		
		//Store the first queue name into string
		String QueueData=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println(QueueData); 
		
		for(int i=0; i<=QueueName.length(); i++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Change the Settings We need to check show empty queues for verification
		 driver.findElement(By.cssSelector(".fa-cog")).click();
		 driver.findElement(By.id("empty-queues")).click(); 
		 Thread.sleep(2000);
		 driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		 Thread.sleep(6000);
		
		//Verification 
		if(QueueData.contains(QueueName))
		{
			System.out.println("Queue is created From icon");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Queue is created From icon");
		}
		else
		{
			System.out.println("Queue is not Created from options");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Queue is not created From icon");
			driver.findElement(By.xpath("Queue creation failed")).click();
		}
		}
		
		catch (Exception e)
		{
			System.out.println("Exception occured while creating queue from the Icon");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured while creating queue from the Icon, check details: "+ e.getMessage());
			if(driver.findElement(By.cssSelector("//div[2]/div/div/div[3]/button")).isDisplayed())
			{
				driver.findElement(By.cssSelector("//div[2]/div/div/div[3]/button")).click();
				
			}
			
		}
		Thread.sleep(2000);		
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=25)
	public static void Logout(String Dashboardname) throws Exception
	{
		// Changing the Settings 
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click();
		Thread.sleep(4000);
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(6000);
		
		LogoutForAll lo=new LogoutForAll();
		lo.LogoutMethod(driver, Dashboardname);
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
