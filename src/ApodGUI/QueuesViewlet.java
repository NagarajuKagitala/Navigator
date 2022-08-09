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
public class QueuesViewlet 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String WGSName;
	static String Q_QueueName;
	static String Dnode;
	static String DestinationManager;
	static String FinalQueuename="";
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
		Q_QueueName =Settings.getQ_QueueName();
		Dnode =Settings.getDnode();
		DestinationManager =Settings.getDestinationManager();
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
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		
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
		dd.selectByIndex(wgs);*/
		
		/*//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(MediumSleep);
		
		//--------- Create Queue viewlet-----------
		//Click on Viewlet		
		Viewlets obj=new Viewlets();
		obj.IBMMQViewlet(driver, ViewletValue, ViewletName, WGSName, Node_Hostname);
		Thread.sleep(MediumSleep);
		
		//Go to edit viewlet
		driver.findElement(By.id("dropdownMenuButton")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		Thread.sleep(4000);
		
		//Update result limit
		driver.findElement(By.xpath("//input[@type='number']")).clear();
		driver.findElement(By.xpath("//input[@type='number']")).sendKeys("1000");
		Thread.sleep(3000);
		
		//Click on Apply changes
		driver.findElement(By.xpath("//button[contains(.,'Apply changes')]")).click(); 
		Thread.sleep(MediumSleep);
		
		//Restore Default settings
		  driver.findElement(By.cssSelector(".fa-cog")).click();
		  driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click(); 
		  Thread.sleep(4000);
		  driver.findElement(By.id("accept-true")).click();
		  driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		  Thread.sleep(MediumSleep);
		
		
		for(int m=1; m<=3; m++)
		{
			//Select the put new message option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper["+ m +"]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions PutMessagesMousehour=new Actions(driver);
			PutMessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
			driver.findElement(By.linkText("Put New Message")).click();
			Thread.sleep(1000);
			
			//Select the number of messages
			driver.findElement(By.name("generalNumberOfMsgs")).click();
			driver.findElement(By.name("generalNumberOfMsgs")).clear();
			driver.findElement(By.name("generalNumberOfMsgs")).sendKeys("2");
			
			//Put a message data
			//driver.findElement(By.id("encoding-text-9")).click();
			driver.findElement(By.xpath("//textarea")).sendKeys("Test Message");
			driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
			Thread.sleep(MediumSleep);
			
			try
			{
				driver.findElement(By.id("yes")).click();
				driver.findElement(By.cssSelector(".btn-danger")).click();
				Thread.sleep(1000);
			}
			catch (Exception e)
			{
				System.out.println("No Exception");
			}
		}
		
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=1)
	@TestRail(testCaseId = 68)
	public static void BrowseMessages(String Dashboardname, ITestContext context) throws InterruptedException
	{		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select Browse Messages Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Browse messages")).click();
		Thread.sleep(MediumSleep);               
		
		//Store the browse message page value into string
		String BrowseMessagespage=driver.findElement(By.cssSelector(".g-row-head:nth-child(2)")).getText();
		System.out.println(BrowseMessagespage);
		
		//verification
		if(BrowseMessagespage.equalsIgnoreCase("Message Cursor"))
		{
			System.out.println("Message browse page is opened");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Message browse option working fine");
		}
		else
		{
			System.out.println("Message browse page is not opened");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Faile to load message browse option");
			driver.findElement(By.cssSelector(".close-button")).click();
			driver.findElement(By.xpath("Message browse page is failed")).click();
		}
		//Close the popup page
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "schemaName"})
	@TestRail(testCaseId = 69)
	@Test(priority=29)
	public static void ShowObjectAttributesForQueues(String Dashboardname, String schemaName, ITestContext context) throws InterruptedException
	{		
		try {
		ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.QueuesAttributesVerification(Dashboardname, driver, schemaName, WGSName);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Attributes verified successfully");
		}catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception, check details: " + e.getMessage());
			driver.findElement(By.id("Objects verification failed")).click();
		}
		
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=2)
	@TestRail(testCaseId = 70)
	public static void ShowQueueStatus(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Find the queue current depth
		String currentdepth=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
		int result = Integer.parseInt(currentdepth);
		System.out.println(result);
		
		//Select show object Attributes Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Queue Status")).click();
		Thread.sleep(MediumSleep);
				
		//Store status
		String Sta=driver.findElement(By.xpath("//tr[3]/td[3]")).getText();
		int Status=Integer.parseInt(Sta);
		System.out.println("Status result: " +Status);
		
		//Verification condition
		if(result == Status)
		{
			System.out.println("Show Queue Status page is opened");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show Queue Status page is opened");
		}
		else
		{
			System.out.println("Failed to open Show Queue Status page");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to open Show Queue Status page");
			driver.findElement(By.cssSelector(".close-button")).click();
			driver.findElement(By.xpath("Queue Status page opening is failed")).click();
		}
		
		//Close the Queue status page
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(4000);
			
	}
	
	@Parameters({"Dashboardname", "QueueNameFromOptions", "QueueDescriptionFromOptions"})
	@TestRail(testCaseId = 71)
	@Test(priority=3)
	public void CreateQueueFromOptions(String Dashboardname, String QueueNameFromOptions, String QueueDescriptionFromOptions, ITestContext context) throws InterruptedException
	{
		try
		{
			
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
		//Select show object Attributes Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Create Queue")).click();
		Thread.sleep(1000);
		
		//Create Queue Window
		driver.findElement(By.name("name")).sendKeys(QueueNameFromOptions);
		driver.findElement(By.name("description")).sendKeys(QueueDescriptionFromOptions);
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(HighSleep);
				
		try
		{
			driver.findElement(By.id("yes")).click();
		}
		catch (Exception e)
		{
			System.out.println("No Exception occured");
		}
		Thread.sleep(2000);
		
		/*
		 * //Change the Settings We need to check show empty queues for verification
		 * driver.findElement(By.cssSelector(".fa-cog")).click();
		 * driver.findElement(By.id("empty-queues")).click(); Thread.sleep(2000);
		 * driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		 * Thread.sleep(2000);
		 */
		
		//Serach with empty queue name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(QueueNameFromOptions);
		Thread.sleep(3000);
		
		//Store the first queue name into string
		String Firstqueue=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println(Firstqueue);
		
		//Edit the search
		for(int k=0; k<=QueueNameFromOptions.length(); k++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		Thread.sleep(1000);
		
		if(Firstqueue.equalsIgnoreCase(QueueNameFromOptions))
		{
			System.out.println("Queue is added successfully");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Queue added successfully");
		}
		else
		{
			System.out.println("Queue is added not added");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add Queue");
			driver.findElement(By.id("Add Queue failed")).click();
		}
		
		/*//Applying the loop from second queue onwords
		for(int q=2; q<=100; q++)
		{
		String AddedQueuename=driver.findElement(By.xpath("//datatable-row-wrapper["+ q +"]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println(AddedQueuename);  
		
		//Verification 
		if(AddedQueuename.equalsIgnoreCase(QueueNameFromOptions) || Firstqueue.equalsIgnoreCase(QueueNameFromOptions))
		{
			System.out.println("Queue is created From options");
			break;
		}
		else
		{
			System.out.println("Queue is not Created from options");
			driver.findElement(By.xpath("Queue creation failed")).click();
		}
		}*/
		}
		
		catch (Exception e)
		{
			System.out.println("Exception occured while creating a queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured while adding queue, check details: " + e.getMessage());
			if(driver.findElement(By.cssSelector(".btn-danger")).isDisplayed())
			{
				driver.findElement(By.cssSelector(".btn-danger")).click();
			}
		}
		Thread.sleep(2000);
			
	}
	
	@Parameters({"Dashboardname", "ObjectName", "ObjectDescription"})
	@TestRail(testCaseId = 72)
	@Test(priority=4)
	public static void CopyAsQueueCommands(String Dashboardname, String ObjectName, String ObjectDescription, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Take the Queue name whcih one you want to delete
		String Queuenamebefore=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println(Queuenamebefore);
		
		//Select copy as option from Commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions CopyMousehover=new Actions(driver);
		CopyMousehover.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Copy As...")).click();
		Thread.sleep(4000);
		
		//Object Details
		driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).sendKeys(ObjectName);
		driver.findElement(By.xpath("//div[2]/div[2]/input")).sendKeys(ObjectDescription);
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(4000);
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(4000);
		}
		catch (Exception e)
		{
			System.out.println("No Exception occured");
		}
		Thread.sleep(2000);
		
		//Refresh viewlet
		for(int i=0; i<=2; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(6000);
		}
				
		FinalQueuename=Queuenamebefore + ObjectName;
		
		//Search with empty queue name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinalQueuename);
		Thread.sleep(3000);
		
		//Store the Queue name after deleting the Queue
		String ViewletData=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println("Viewlet data is: " +ViewletData);
		
		for(int i=0; i<=FinalQueuename.length(); i++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		Thread.sleep(2000);
		
		if(ViewletData.contains(FinalQueuename))
		{
			System.out.println("Copy as option is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Copy as working");
		}
		else
		{
			System.out.println("Copy as option is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Copy as failed");
			driver.findElement(By.xpath("Copy As failed")).click();
		}
				
	}
	
	@TestRail(testCaseId = 971)
	@Parameters({"Dashboardname", "RenameQueue"})
	@Test(priority=5)
	public void RenameQueueFromCommandsOption(String Dashboardname, String RenameQueue, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinalQueuename);
    	Thread.sleep(2000); 
    	
		//Select Rename From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Rename")).click();
    	Thread.sleep(3000);
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//app-mod-viewlet-object-rename/div/div/div[2]/input")).sendKeys(RenameQueue);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	driver.findElement(By.id("accept-true")).click();
		Thread.sleep(MediumSleep);
    	
    	try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(4000);
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(4000);
		}
		catch (Exception e)
		{
			System.out.println("No Exception occured");
		}
		Thread.sleep(2000);
    	    	
    	//Edit the search field data
    	for(int j=0; j<=FinalQueuename.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);	
    	
    	//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(4000);
    	}
    	
    	//Search with renamed name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameQueue);
    	Thread.sleep(3000); 
    	
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
	@TestRail(testCaseId = 766)
	@Test(priority=6)
	public void DeleteQueueFromCommandsOption(String Dashboardname, String RenameQueue, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with empty queue name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameQueue);
		Thread.sleep(3000);
				
		//Select Commands option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions DeleteMousehover=new Actions(driver);
		DeleteMousehover.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Delete Queue")).click();
		Thread.sleep(3000);
		
		//Delete option
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		for(int i=0; i<=2; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);
		}
		
		try
		{
		if(driver.findElement(By.xpath("//app-mod-errors-display/div/button")).isDisplayed())
		{
			driver.findElement(By.xpath("//app-mod-errors-display/div/button")).click();
			
			//Click on Cancel button
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		
		else
		{
			//Store the Queue name after deleting the Queue
			String ViewletData=driver.findElement(By.xpath("//datatable-body")).getText();
			System.out.println(ViewletData);
			
			//Edit the search
			for(int k=0; k<=RenameQueue.length(); k++)
			{
				driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
			}
			Thread.sleep(1000);
			
			if(ViewletData.contains(RenameQueue))
			{
				System.out.println("Queue is not deleted");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to delete queue");
				driver.findElement(By.xpath("Queue Delete failed")).click();
			}
			else
			{
				
				System.out.println("Queue is deleted Successfully");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Queue deleted Successfully");
			}
			Thread.sleep(1000);
		}
		}
		catch (Exception e) 
		{
			//Store the Queue name after deleting the Queue
			String ViewletData=driver.findElement(By.xpath("//datatable-body")).getText();
			System.out.println(ViewletData);
			
			//Edit the search
			for(int k=0; k<=RenameQueue.length(); k++)
			{
				driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
			}
			Thread.sleep(1000);
			
			if(ViewletData.contains(RenameQueue))
			{
				System.out.println("Queue is not deleted");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to delete queue");
				driver.findElement(By.xpath("Queue Delete failed")).click();
			}
			else
			{
				
				System.out.println("Queue is deleted Successfully");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Queue deleted Successfully");
			}
			Thread.sleep(1000);
			
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Message deleted"); 
        }	
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 944)
	@Test(priority=7)
	public void InhibitGetFromCommands(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select Commands option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions DeleteMousehover=new Actions(driver);
		DeleteMousehover.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Inhibit Get")).click();
		Thread.sleep(MediumSleep);
		
		//Click on accept button
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(LowSleep);
		
		//Refresh viewlet
		for(int i=0; i<=2; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(6000);
		}
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		WebElement op=driver.findElement(By.id("dropdown-block")).findElement(By.tagName("div")).findElement(By.tagName("ul"));
		List <WebElement> Lis=op.findElements(By.tagName("li"));
		System.out.println("List of lis are: " +Lis.size());
		StringBuilder buffer=new StringBuilder();
		for(WebElement anc : Lis)
		{
			String Options=anc.getText();
			if(Options.equalsIgnoreCase(""))
			{
				
			}
			else
			{
				buffer.append(Options);
				buffer.append(",");
			}
		}
		
		String OptionsNames=buffer.toString();
		System.out.println("List of dashboards are: " +OptionsNames);
		
		//Refresh viewlet
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(4000);
		
		if(OptionsNames.contains("Browse messages"))
		{
			System.out.println("inhibited get is not working");
			Allowget(Dashboardname);
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "inhibited get is failed");
			driver.findElement(By.id("Inhibited get failed")).click();
		}
		else
		{
			System.out.println("inhibited get is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "inhibited get is working");
		}
		Allowget(Dashboardname);
		
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 945)
	@Test(priority=8)
	public void InhibitPutFromCommands(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select Commands option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions DeleteMousehover=new Actions(driver);
		DeleteMousehover.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Inhibit Put")).click();
		Thread.sleep(MediumSleep);
		
		//Click on accept button
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(LowSleep);
		
		//Refresh viewlet
		for(int i=0; i<=2; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(6000);
		}
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		
		List<WebElement> lst=driver.findElements(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[6]/ul/li"));
		
		StringBuilder buffer=new StringBuilder();
		for(WebElement lstvalue : lst)
		{
			List<WebElement> links= lstvalue.findElements(By.tagName("a"));
			//System.out.println("options are: " +lstvalue.getAttribute("innerHTML"));
			for(WebElement weblink : links)
			{
				//System.out.println("options links html:  " +weblink.getAttribute("innerHTML"));
				String options=weblink.getAttribute("innerHTML");
				buffer.append(options);
				buffer.append(",");
				
			}
		}
		
		String Listofoptions=buffer.toString();
		System.out.println("Final options are: " +Listofoptions);
		
		
		//Refresh viewlet
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
		Thread.sleep(4000);
		
		if(Listofoptions.contains("Put New Message"))
		{
			System.out.println("inhibited put is not working");
		    Allowput(Dashboardname);
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "inhibited put is failed");
			driver.findElement(By.id("Inhibited put failed")).click();
		}
		else
		{
			System.out.println("inhibited put is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "inhibited put is working");
		}
		Allowput(Dashboardname);	
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 946)
	@Test(priority=9)
	public void MQSCSnapshot(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Save the object name into string
		String Queuename=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println("Object name is: " +Queuename);
		
		
		//Select Commands option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions DeleteMousehover=new Actions(driver);
		DeleteMousehover.moveToElement(driver.findElement(By.linkText("MQSC"))).perform();
		driver.findElement(By.linkText("Snapshot...")).click();
		Thread.sleep(MediumSleep);
		
		//Get snapshot data
		String SnapshotData=driver.findElement(By.xpath("//textarea")).getText();
    	System.out.println("Snapshot data is: " +SnapshotData);
		
		if(SnapshotData.contains(Queuename))
		{
			System.out.println("Snap shot popup page is opened");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Snapshot page is Opened");
		}
		else
		{
			System.out.println("Snapshot popup page not opened");
			//Close the popup page
			driver.findElement(By.cssSelector(".btn-danger")).click();
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Snapshot page not opened");
			driver.findElement(By.id("Snapshot open failed")).click();
		}
		
		//Close the popup page
		driver.findElement(By.cssSelector(".btn-danger")).click();
		Thread.sleep(2000);	
	}
	
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 947)
	@Test(priority=10)
	public void SaveMQSCSnapshot(String Dashboardname, ITestContext context) throws InterruptedException
	{
		try
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
		//Select Commands option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions DeleteMousehover=new Actions(driver);
		DeleteMousehover.moveToElement(driver.findElement(By.linkText("MQSC"))).perform();
		driver.findElement(By.linkText("Snapshot...")).click();
		Thread.sleep(8000);
		
		//Click on Save button
		driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
		Thread.sleep(MediumSleep);
		
		System.out.println("Snap shot data is saved");
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Snapshot data is saved");
		driver.findElement(By.cssSelector(".btn-danger")).click();
		Thread.sleep(2000);	
		
		}
		catch(Exception e)
		{
			System.out.println("Snap shot data is save failed");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Snapshot data is save failed");
			driver.findElement(By.cssSelector(".btn-danger")).click();
			Thread.sleep(2000);	
			driver.findElement(By.id("Save snapshot data failed")).click();
		}
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=11)
	@TestRail(testCaseId = 73)
	public static void QueueProperties(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		/*
		 * // Changing the Settings
		 * driver.findElement(By.cssSelector(".fa-cog")).click();
		 * driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click
		 * (); Thread.sleep(4000);
		 * driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		 * Thread.sleep(3000);
		 */
		
		//Select Queue properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
		
		boolean FieldNamevalue=driver.findElement(By.id("name")).isEnabled();
		System.out.println(FieldNamevalue);
		
		//Verification
		if(FieldNamevalue == false)
		{
			System.out.println("Queue Name field is UnEditable");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Queue Name field is UnEditable");
			 driver.findElement(By.cssSelector(".btn-primary")).click();
			 Thread.sleep(8000);
		}
		else
		{
			System.out.println("Queue Name field is Editable");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Queue Name field is Editable");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(8000);
			driver.findElement(By.xpath("Queue name edit function Failed")).click();
			
		}
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=12)
	@TestRail(testCaseId = 74)
	public static void QueueEvents(String Dashboardname, ITestContext context) throws InterruptedException
	{
		AllEvents obj=new AllEvents();
		obj.Events(Dashboardname, driver, context);
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId = 75)
	@Test(priority=13)
	public void AddToFavoriteViewlet(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the viewlet option and select the favorite checkbox
		driver.findElement(By.xpath("//button[3]")).click();
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.cssSelector("div.mod-select-viewlet-buttons > button.g-button-blue")).click();
		Thread.sleep(4000);
		
		//Viewlet Name
		driver.findElement(By.name("viewlet-name")).click();
		driver.findElement(By.name("viewlet-name")).sendKeys(FavoriteViewletName);
		
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
		
		//Submit
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(MediumSleep);
		
		//Store Queue name
		String QueueFav=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		
		//Select Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(4000);
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(MediumSleep);
		
		//Select the favorite viewlet name
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
		
		/*
		 * Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		 * fav.selectByVisibleText(FavoriteViewletName); Thread.sleep(2000);
		 * driver.findElement(By.
		 * cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue"
		 * )).click(); Thread.sleep(2000);
		 */
		
		//Store favorite viewlet data into string
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//verification
		if(Favdata.contains(QueueFav))
		{
			System.out.println("Queue is added to favorite viewlet");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Queue is added to favorite viewlet");
		}
		else
		{
			System.out.println("Queue is not added to favorite viewlet");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Faile to add Queue  to favorite viewlet");
			driver.findElement(By.xpath("Queue not add to favorite")).click();
		}
		Thread.sleep(1000);
		
	}
	
	
	@Test(priority=14)
	@TestRail(testCaseId = 76)
	public static void CompareQueues(ITestContext context) throws InterruptedException
	{
		CompareObjects com=new CompareObjects();
		com.Compare(driver, WGSName, context);
	}
	
	
	@TestRail(testCaseId = 767)
	@Test(priority=15)
	public void CheckDifferencesForQueues(ITestContext context) throws InterruptedException
	{
		DifferenceOfObjects diff=new DifferenceOfObjects();
		diff.Differences(driver, context);
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=16)
	@TestRail(testCaseId = 77)
	public static void ShowQueueStatusForMultipleQueues(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//First queue depth
		String FirstDepth=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
		System.out.println("First queue depth is :" +FirstDepth);
		
		//Second queue depth
		String SecondDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[6]/div/span")).getText();
		System.out.println("Second queue depth is: " +SecondDepth);
		
		//Select show queue status option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Queues Status")).click();
		Thread.sleep(6000);
		
		//Store result depth
		String ResultFirst=driver.findElement(By.xpath("//tr[3]/td[3]")).getText();
		System.out.println("Reslut first :" +ResultFirst);
		
		//Store result depth
		String ResultSecond=driver.findElement(By.xpath("//tr[5]/td[3]")).getText();
		System.out.println("Reslut Second :" +ResultSecond);
		
		//Verification condition
		if((FirstDepth.equalsIgnoreCase(ResultFirst) && SecondDepth.equalsIgnoreCase(ResultSecond)) || (FirstDepth.equalsIgnoreCase(ResultSecond) && SecondDepth.equalsIgnoreCase(ResultFirst)))
		{
			System.out.println("Show Multiple Queue Status page is opened");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Show Multiple Queue Status page is working fine");
		}
		else
		{
			System.out.println("Show Multiple Queue Status page is opened");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to open multiple Queue Status page");
			driver.findElement(By.cssSelector(".close-button")).click();
			driver.findElement(By.xpath("Queue Status page opening is failed")).click();
		}
		
		//Close the Queue status page
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(1000);
	}
	
	
	@Parameters({"QueueDescription"})
	@TestRail(testCaseId = 81)
	@Test(priority=17)
	public void AddQueueFromPlusIcon(String QueueDescription, ITestContext context) throws InterruptedException
	{
		try
		{
		//Click on + icon
		driver.findElement(By.xpath("//img[@title='Add Queue']")).click();
		
		//Select WGS
		/*
		 * Select WGS=new Select(driver.findElement(By.xpath(
		 * "//app-mod-select-object-path-for-create/div/div/ng-select/div/span")));
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
				if(mdi.getText().equals(DestinationManager))
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
		
        //Click on Select path button
		driver.findElement(By.xpath("//button[contains(.,'Select path')]")).click();
		Thread.sleep(2000);
		
		//Create Queue Window
		driver.findElement(By.name("name")).sendKeys(Q_QueueName);
		driver.findElement(By.name("description")).sendKeys(QueueDescription);
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
		Thread.sleep(MediumSleep);         
				
		try
		{
			driver.findElement(By.id("yes")).click();
			driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).click();
		}
		catch (Exception e)
		{
			System.out.println("No Exception occured");
			
		}
		
		//Change the Settings We need to check show empty queues for verification
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(3000);
		boolean empty=driver.findElement(By.id("empty-queues")).isSelected();
		if(empty)
		{
			System.out.println("Empty queues check box is already selected");
		}
		else
		{
			driver.findElement(By.id("empty-queues")).click();
		}
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(MediumSleep);
		
		//Serach with empty queue name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Q_QueueName);
		Thread.sleep(3000);
		
		//Store the first queue name into string
		String Firstqueue=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println(Firstqueue);
		
		//Edit the search
		for(int k=0; k<=Q_QueueName.length(); k++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		Thread.sleep(1000);
		
		if(Firstqueue.equalsIgnoreCase(Q_QueueName))
		{
			System.out.println("Queue is added successfully from icon");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Queue is added successfully using add icon");
		}
		else
		{
			System.out.println("Queue is added not added");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Faile to add Queues using add icon");
			driver.findElement(By.id("Add Queue failed")).click();
		}
		
		
		/*// Changing the Settings 
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//div[2]/div/div/div[2]/button")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(1000);	*/
		}
		
		catch (Exception e)
		{
			System.out.println("Exception occured while creating queue from the Icon");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Got exception while adding queue using add icon, check details: "+ e.getMessage());
			if(driver.findElement(By.cssSelector(".btn-danger")).isDisplayed())
			{
				driver.findElement(By.cssSelector(".btn-danger")).click();
				
			}
			
		}
		Thread.sleep(2000);		
	}
	
	@TestRail(testCaseId = 768)
	@Parameters({"Dashboardname", "CopyAsNameForMultiple", "ObjectDescriptionForMultiples"})
	@Test(priority=18)
	public void CopyAsForMultipleQueues(String Dashboardname, String CopyAsNameForMultiple, String ObjectDescriptionForMultiples, ITestContext context) throws InterruptedException
	{
		try
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
				
		/*driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.cssSelector(".checkbox:nth-child(2) > input")).click();
		driver.findElement(By.cssSelector(".btn-group:nth-child(3) > .btn")).click();
		Thread.sleep(2000);*/
		
		String[] Managers = new String[10];
		for(int i=0; i<10; i++)
		{
			int k=i+1;
			//Get the Queue manager names
		    Managers[i]=driver.findElement(By.xpath("//datatable-row-wrapper["+ k +"]/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
			System.out.println("Managers are: " +Managers[i]);
			
			String FirstMr=Managers[0];
			//System.out.println("initial manager name is: " +FirstMr);
			if(i>0)
			{
				if(FirstMr.equalsIgnoreCase(Managers[i]))
				{
					System.out.println("managers are matched");
					
				}
				else
				{
					System.out.println("Index values is: " +i);
					driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
					driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper["+ k +"]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
					Thread.sleep(4000);
					Actions CopyMousehover=new Actions(driver);
					CopyMousehover.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
					driver.findElement(By.linkText("Copy As...")).click();
					Thread.sleep(3000);
					
					//Object Details
					driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).clear();
					driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).sendKeys(CopyAsNameForMultiple);
					driver.findElement(By.xpath("//div[2]/div[2]/input")).sendKeys(ObjectDescriptionForMultiples);
					driver.findElement(By.cssSelector(".btn-primary")).click();
					Thread.sleep(MediumSleep);
					
					try
					{
						driver.findElement(By.id("yes")).click();
						Thread.sleep(4000);
						driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
						Thread.sleep(4000);
					}
					catch (Exception e)
					{
						System.out.println("No Exception occured");
					}
					Thread.sleep(2000);
					
					//Refresh viewlet
					for(int z=0; z<=2; z++)
					{
						driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
						Thread.sleep(6000);
					}
					
					//Search with empty queue name
					driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyAsNameForMultiple);
					Thread.sleep(2000);
										
					//Store the Queue name after deleting the Queue
					String ViewletData=driver.findElement(By.xpath("//datatable-body")).getText();
					System.out.println("Viewlet data is: " +ViewletData);
					
					for(int m=0; m<=CopyAsNameForMultiple.length(); m++)
					{
						driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
					}
					Thread.sleep(2000);
					
					if(ViewletData.contains(CopyAsNameForMultiple))
					{
						System.out.println("Copy as option is working fine for multiples");
						context.setAttribute("Status", 1);
						context.setAttribute("Comment", "Copy as working");
					}
					else
					{
						System.out.println("Copy as option is not working for multiples");
						context.setAttribute("Status", 5);
						context.setAttribute("Comment", "Multi Copy as failed");
						driver.findElement(By.xpath("Multi Copy As failed")).click();
					}
					break;
					
				}
			
			}
		}
		}
		
		catch (Exception e)
		{
			System.out.println("Copy as option is not working for multiples");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Multi Copy as failed");
			driver.findElement(By.xpath("Multi Copy As failed")).click();
		}	
	}
	
	@TestRail(testCaseId = 972)
	@Parameters({"Dashboardname", "CopyAsNameForMultiple", "ReameForMultiple"})
	@Test(priority=19, dependsOnMethods= {"CopyAsForMultipleQueues"})
	public void RenameForMultipleQueues(String Dashboardname, String CopyAsNameForMultiple, String ReameForMultiple, ITestContext context) throws InterruptedException
	{
		try
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
			//Search with empty queue name
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyAsNameForMultiple);
			Thread.sleep(2000);
					
		String[] Managers = new String[10];
		for(int i=0; i<10; i++)
		{
			int k=i+1;
			//Get the Queue manager names
		    Managers[i]=driver.findElement(By.xpath("//datatable-row-wrapper["+ k +"]/datatable-body-row/div[2]/datatable-body-cell[5]/div/span")).getText();
			System.out.println("Managers are: " +Managers[i]);
			
			String FirstMr=Managers[0];
			//System.out.println("initial manager name is: " +FirstMr);
			if(i>0)
			{
				if(FirstMr.equalsIgnoreCase(Managers[i]))
				{
					System.out.println("managers are matched");
					
				}
				else
				{
					System.out.println("Index values is: " +i);
					driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
					driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper["+ k +"]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
					Thread.sleep(4000);
					Actions CopyMousehover=new Actions(driver);
					CopyMousehover.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
					driver.findElement(By.linkText("Rename")).click();
					Thread.sleep(3000);
					
					//Object Details
					driver.findElement(By.xpath("//app-mod-viewlet-object-rename/div/div/div[2]/input")).clear();
					driver.findElement(By.xpath("//app-mod-viewlet-object-rename/div/div/div[2]/input")).sendKeys(ReameForMultiple);
					
					driver.findElement(By.cssSelector(".btn-primary")).click();
					Thread.sleep(MediumSleep);
					driver.findElement(By.id("accept-true")).click();
					Thread.sleep(MediumSleep);
					
					try
					{
						driver.findElement(By.id("yes")).click();
						Thread.sleep(4000);
						driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
						Thread.sleep(4000);
					}
					catch (Exception e)
					{
						System.out.println("No Exception occured");
					}
					Thread.sleep(2000);
					
					for(int m=0; m<=CopyAsNameForMultiple.length(); m++)
					{
						driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
					}
					Thread.sleep(2000);
					
					//Refresh viewlet
					for(int z=0; z<=2; z++)
					{
						driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
						Thread.sleep(6000);
					}
					
					//Search with empty queue name
					driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ReameForMultiple);
					Thread.sleep(2000);
					
										
					//Store the Queue name after deleting the Queue
					String ViewletData=driver.findElement(By.xpath("//datatable-body")).getText();
					System.out.println("Viewlet data is: " +ViewletData);
					
					for(int m=0; m<=ReameForMultiple.length(); m++)
					{
						driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
					}
					Thread.sleep(2000);
					
					if(ViewletData.contains(ReameForMultiple))
					{
						System.out.println("Rename option is working fine for multiples");
						context.setAttribute("Status", 1);
						context.setAttribute("Comment", "Rename working");
					}
					else
					{
						System.out.println("Rename option is not working for multiples");
						context.setAttribute("Status", 5);
						context.setAttribute("Comment", "Multi Rename failed");
						driver.findElement(By.xpath("Multi Rename failed")).click();
					}
					break;
					
				}
			
			}
		}
		}
		
		catch (Exception e)
		{
			System.out.println("Rename option is not working for multiples");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Multi Rename failed");
			driver.findElement(By.xpath("Multi Rename failed")).click();
		}	
	}
	
	@TestRail(testCaseId = 769)
	@Parameters({"Dashboardname", "ReameForMultiple"}) 
	@Test(priority=20, dependsOnMethods= {"RenameForMultipleQueues"})
	public void DeleteMultipleQueues(String Dashboardname, String ReameForMultiple, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with empty queue name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ReameForMultiple);
		Thread.sleep(3000);
				
		//Select Commands option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions DeleteMousehover=new Actions(driver);
		DeleteMousehover.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Delete Queue")).click();
		Thread.sleep(3000);
		
		//Delete option
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		for(int i=0; i<=2; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);
		}
				
		try
		{
		if(driver.findElement(By.xpath("//app-mod-errors-display/div/button")).isDisplayed())
		{
			driver.findElement(By.xpath("//app-mod-errors-display/div/button")).click();
			
			//Click on Cancel button
			driver.findElement(By.xpath("//div[3]/button")).click();
		}
		
		else
		{
			//Store the Queue name after deleting the Queue
			String ViewletData=driver.findElement(By.xpath("//datatable-body")).getText();
			System.out.println(ViewletData);
			
			//Edit the search
			for(int k=0; k<=ReameForMultiple.length(); k++)
			{
				driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
			}
			Thread.sleep(1000);
			
			if(ViewletData.contains(ReameForMultiple))
			{
				System.out.println("Queue is not deleted");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to delete queue");
				driver.findElement(By.xpath("Queue Delete failed")).click();
			}
			else
			{
				
				System.out.println("Queue is deleted Successfully");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Queue deleted Successfully");
			}
			Thread.sleep(1000);
		}
		}
		catch (Exception e) 
		{
			//Store the Queue name after deleting the Queue
			String ViewletData=driver.findElement(By.xpath("//datatable-body")).getText();
			System.out.println(ViewletData);
			
			//Edit the search
			for(int k=0; k<=ReameForMultiple.length(); k++)
			{
				driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
			}
			Thread.sleep(1000);
			
			if(ViewletData.contains(ReameForMultiple))
			{
				System.out.println("Queue is not deleted");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to delete queue");
				driver.findElement(By.xpath("Queue Delete failed")).click();
			}
			else
			{
				
				System.out.println("Queue is deleted Successfully");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Queue deleted Successfully");
			}
			Thread.sleep(1000);
			
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Message deleted"); 
        }	
		
	}
	
	@Parameters({"Dashboardname", "TestDescription"})
	@TestRail(testCaseId = 78)
	@Test(priority=21)
	public void MultipleQueueProperties(String Dashboardname, String TestDescription, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//Give description
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys(TestDescription);
		
		//close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//Open the properties of first queue
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//Save the Connection URL value into string
		String FirstQueuedata=driver.findElement(By.id("description")).getAttribute("value");
		System.out.println("First queue property is: " +FirstQueuedata);
		
		//close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che2=new ClearSelectionofCheckbox();
		che2.Deselectcheckbox(Dashboardname, driver);
		
		//Open the properties of Second queue
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(4000);
		
		//Save the Connection URL value into string
		String SecondQueuedata=driver.findElement(By.id("description")).getAttribute("value");
		System.out.println("Second queue property data is: " +SecondQueuedata);
		
		//close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//Verification
		if(FirstQueuedata.equals(TestDescription) && SecondQueuedata.equals(TestDescription))
		{
			System.out.println("multiple properties wotrking fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Multiple properties wotrking fine");
		}
		else
		{
			System.out.println("multiple properties are not wotrking");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to show multiple properties");
			driver.findElement(By.id("multiple properties failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId = 79)
	@Test(priority=22, dependsOnMethods= {"AddToFavoriteViewlet"})
	public static void AddToFavoriteForMultipleQueues(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Store Queue names                
		String Queue2=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		String Queue3=driver.findElement(By.xpath("//datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		//Select Queue Events option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(3000);
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(5000);
		
		//Select the favorite viewlet name
		driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div/ng-select/div")).click();
		Thread.sleep(MediumSleep); 
		
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
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Multiple Queues are added to Favorite viewlet");
		}
		else
		{
			System.out.println("Multiple Queues are not added to Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Faile to add Multiple Queues to Favorite viewlet");
			driver.findElement(By.xpath("Multiple queues to fav failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId = 80)
	@Test(priority=23)
	public static void SearchFilter(ITestContext context) throws InterruptedException
	{
		//Store the search data into string
		String SearchData=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		
		//Enter the data into search field
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(SearchData);
		Thread.sleep(2000);
		
		//Store the vielet data into string after searching 
		String Viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
		//System.out.println(Viewletdata);
		
		//Verification
	    if(Viewletdata.toUpperCase().contains(SearchData.toUpperCase()))
	    {
	       System.out.println("Search is working fine");
	       context.setAttribute("Status",1);
			context.setAttribute("Comment", "Queue search is working fine");
	    }
	    else
	    {
	       System.out.println("Search is not working fine");
	       context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to find queue using search filter");
	       driver.findElement(By.xpath("Search is failed")).click();
	    }
	    Thread.sleep(2000);
	   
	}
	
	
	@Parameters({"Dashboardname"})
	@Test(priority=30)
	public static void Logout(String Dashboardname) throws Exception
	{
		// Changing the Settings 
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click();
		Thread.sleep(4000);
		driver.findElement(By.id("accept-true")).click();
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(3000);
		
		LogoutForAll lo=new LogoutForAll();
		lo.LogoutMethod(driver, Dashboardname);
	}
	
	public void Allowget(String Dashboardname) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Select Commands option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions DeleteMousehover=new Actions(driver);
		DeleteMousehover.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Allow Get")).click();
		Thread.sleep(MediumSleep);
		
		//Click on accept button
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(LowSleep);
		
		
	}
	
	public void Allowput(String Dashboardname) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Select Commands option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions DeleteMousehover=new Actions(driver);
		DeleteMousehover.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Allow Put")).click();
		Thread.sleep(MediumSleep);
		
		//Click on accept button
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(LowSleep);
		
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
