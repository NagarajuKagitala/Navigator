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
import Common.Discoverfull;
import Common.LogoutForAll;
import Common.Viewlets;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class SubscriptionViewlet {
	
	String FinalSubscription="";
	static String Final1="";
	static String Final2="";
	static WebDriver driver;
	
	static String Screenshotpath;
	static String WGSName;
	static String DWGS;
	static String Dnode;
	static String DestinationManager;
	static String DestinationQueue;
	static String DWGSIcon;
	static String DestinationTopicName;
	static String Manager1;
	static String Manager1Queuename;
	static String Manager2Queuename;
	static String Manager2;
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
		DWGS =Settings.getDWGS();
		Dnode =Settings.getDnode();
		DestinationManager =Settings.getDestinationManager();
		DestinationQueue =Settings.getDestinationQueue();
		DWGSIcon =Settings.getDWGSIcon();
		DestinationTopicName =Settings.getDestinationTopicName();
		Manager1 =Settings.getManager1();
		Manager1Queuename =Settings.getManager1Queuename();
		Manager2 =Settings.getManager2();
		Manager2Queuename =Settings.getManager2Queuename();
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
		Thread.sleep(LowSleep);
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		Thread.sleep(LowSleep);
		
		
		/*driver.findElement(By.id("createInitialViewlets")).click();
		//Work group server selection
		Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
		Thread.sleep(2000);
		dd.selectByIndex(Integer.parseInt(WGS_INDEX));*/
		
		/*//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(MediumSleep);
	}
	
	@Test(priority=1)
	@TestRail(testCaseId=186)
    @Parameters({"ViewletName", "ViewletValue"})
	public static void AddSubscriptionViewlet(String ViewletName, int ViewletValue, ITestContext context) throws InterruptedException
	{
		Viewlets obj=new Viewlets();
		obj.IBMMQViewlet(driver, ViewletValue, ViewletName, WGSName, Node_Hostname);
		
		//Subscription viewlet verification condition
		if(driver.getPageSource().contains(ViewletName))
		{
			System.out.println("Subscription Viewlet is created");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Subscription viewlet is created successfully");
		}
		else
		{
			System.out.println("Subscription viewlet is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create subscription viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
			
    }
	
	@Parameters({"Dashboardname", "SubscriptionAttributes", "schemaName", "AddSubscriptionNameFromIcon"})
	@TestRail(testCaseId=188)
	@Test(priority=24)
	public static void ShowObjectAttributesForSubscription(String Dashboardname, String SubscriptionAttributes, String schemaName, String AddSubscriptionNameFromIcon, ITestContext context) throws InterruptedException
	{
		try {
					
		ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.SubscriptionObjectAttributesVerification(Dashboardname, driver, schemaName, AddSubscriptionNameFromIcon, WGSName);
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "Show object attributes for subscription is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to show object attributes for subscription viewlet");
			driver.findElement(By.id("Attributes failed")).click();
		}
	}
	
	@TestRail(testCaseId=1157)
	@Test(priority=23)
	public void ShowSubscriptionStatus(ITestContext context) throws InterruptedException
	{
		//Select Show topic status option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Subscription Status")).click();
		Thread.sleep(MediumSleep);
		
		//Get the status data into string
		String StatusData=driver.findElement(By.xpath("//th[3]")).getText();
		System.out.println("Status data is: " +StatusData);
		
		if(StatusData.contains("Subscription ID"))
    	{
    		System.out.println("Subscription status is working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Subscription status is working fine");
		
    	}
    	else
    	{
    		System.out.println("Subscription status is not working");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Subscription status failed");
			driver.findElement(By.cssSelector(".close-button")).click();
		
    		driver.findElement(By.xpath("Subscription status failed")).click();
    	}
		
		//Close the popup page
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(MediumSleep);
		
	}
	
	@Parameters({"Dashboardname", "AddSubscriptionName", "TopicStringData"})
	@TestRail(testCaseId=187)
	@Test(priority=2)
	public void CreateSubscriptionFromOptions(String Dashboardname, String AddSubscriptionName, String TopicStringData, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Search with given Queue manager
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(DestinationManager);
		Thread.sleep(LowSleep);
		
		//click on checkbox and choose create subscription
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Create Subscription")).click();
		Thread.sleep(MediumSleep);
		
		
		//Give the Subscription name
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(AddSubscriptionName);
		Thread.sleep(LowSleep);
		
		//Select the Topic name from the list
		try 
		{
			driver.findElement(By.id("topicName")).click();
			Thread.sleep(LowSleep);
			List<WebElement> Topic=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Topic.size());	
			for (int i=0; i<Topic.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				//System.out.println("Radio button id:" + Topic.get(i).getAttribute("id"));
				String s=Topic.get(i).getText();
				if(s.equals(DestinationTopicName))
				{
					String id=Topic.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(4000);
		
		//Topic string data
		driver.findElement(By.id("topicString")).sendKeys(TopicStringData);
		Thread.sleep(LowSleep);
		
		//Click on Destination tab
		driver.findElement(By.linkText("Destination")).click();
		Thread.sleep(MediumSleep);
		
		//Select WGS name
		try
		{
			Select DestinationWGS=new Select(driver.findElement(By.id("destinationGMName")));
			DestinationWGS.selectByVisibleText(DWGSIcon);  
		}
		catch (Exception ee)
		{
			Select DestinationWGS1=new Select(driver.findElement(By.xpath("//select")));
			DestinationWGS1.selectByVisibleText(DWGSIcon); 
			Thread.sleep(4000);
		}
		
		//Select WGS name
		/*driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div")).click();
		driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div")).sendKeys("DESKTOP-E1JT2VR");
		//span[@class, 'ng-option-label ng-star-inserted']*/	
	
		try 
		{
			//driver.findElement(By.id("destinationNodeName")).click();
			driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div/span[2]")).click();
			Thread.sleep(LowSleep);
			List<WebElement> Node=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Node.size());	
			for (int i=0; i<Node.size();i++)
			{
				//System.out.println("Radio button text:" + Node.get(i).getText());
				//System.out.println("Radio button id:" + Node.get(i).getAttribute("id"));
				String s=Node.get(i).getText();
				if(s.equals(Dnode))
				{
					String id=Node.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(1000);
		
		//Select Manager value
		try 
		{
			//driver.findElement(By.id("destinationQMName")).click();
			driver.findElement(By.xpath("//ng-select[@id='destinationQMName']/div/span[2]")).click();
			Thread.sleep(LowSleep);
			List<WebElement> Manager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
				//System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				if(s.equals(DestinationManager))
				{
					String id=Manager.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(2000);
		
		//Select Queue name
		try 
		{
			//driver.findElement(By.id("destinationQName")).click();
			driver.findElement(By.xpath("//ng-select[@id='destinationQName']/div/span[2]")).click();
			Thread.sleep(LowSleep);
			List<WebElement> QueueName=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(QueueName.size());	
			for (int i=0; i<QueueName.size();i++)
			{
				//System.out.println("Radio button text:" + QueueName.get(i).getText());
				//System.out.println("Radio button id:" + QueueName.get(i).getAttribute("id"));
				String s=QueueName.get(i).getText();
				//System.out.println(s);
				if(s.equals(DestinationQueue))
				{
					String id=QueueName.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(2000);
				
		//Click on OK button
		driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(HighSleep);
    	   	
    	try
    	{
    		driver.findElement(By.id("yes")).click();
    		Thread.sleep(LowSleep);
    		driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
    		Thread.sleep(LowSleep);
    	}
    	catch (Exception e)
    	{
    		System.out.println("Error message not displayed");
    	}
    	Thread.sleep(2000);
    	
    	for(int i=0; i<=DestinationManager.length(); i++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(1000);
    	
    	//Search with the added Subscription name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(AddSubscriptionName);
    	Thread.sleep(LowSleep);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	System.out.println("Subscription body data: " +Subviewlet);
    	
    	//Search with the new name
		for(int j=0; j<=AddSubscriptionName.length(); j++)
    	{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	//Verification
    	if(Subviewlet.contains(AddSubscriptionName))
    	{
    		System.out.println("Subscription is added successfully");
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Subscription added successfully");
    	}
    	else
    	{
    		System.out.println("Subscription is not added to the viewlet");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to add subscription");
    		driver.findElement(By.xpath("Subscription failed")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "CopyObjectName", "AddSubscriptionName"})
	@TestRail(testCaseId=189)
	@Test(priority=3, dependsOnMethods= {"CreateSubscriptionFromOptions"})
	public void CopyAsFromCommands(String Dashboardname, String CopyObjectName, String AddSubscriptionName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Search with the added Subscription name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(AddSubscriptionName);
    	Thread.sleep(LowSleep);
    	
		//Select Copy as From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Copy As...")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Give the object name
    	driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).sendKeys(CopyObjectName);
    	Thread.sleep(LowSleep);
    	driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
    	Thread.sleep(HighSleep);
    	
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
    	
    	 //discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
    	
    	//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(4000);
    	}
    	    	
    	//Edit the search field data
    	for(int j=0; j<=AddSubscriptionName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	    	
    	//Combining the strings 
    	String CopyasSubscriptionName=AddSubscriptionName+CopyObjectName;
    	System.out.println(CopyasSubscriptionName);
    	
    	//Search with the added Subscription name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyasSubscriptionName);
    	Thread.sleep(LowSleep);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println("Viewlet data is: " +Subviewlet);
    	
    	//Verification condition
    	if(Subviewlet.contains(CopyasSubscriptionName))
    	{
    		System.out.println("Subscription is copied");
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Subscription is copied successfully using CopyAs command");
    	}
    	else
    	{
    		System.out.println("Subscription is not copied");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to copy subscription using CopyAs command");
    		
    		//Search with that name
        	driver.findElement(By.xpath("(//input[@type='text'])[4]")).sendKeys(CopyasSubscriptionName);
        	Thread.sleep(2000);
    		driver.findElement(By.xpath("Subscription failed to copy")).click();
    	}
    	Thread.sleep(1000);	
    	
    	
	}
	
	@Parameters({"Dashboardname", "RenameSubscription", "CopyObjectName", "AddSubscriptionName"})
	@TestRail(testCaseId=190)
	@Test(priority=4, dependsOnMethods= {"CopyAsFromCommands"})
	public void RenameFromCommands(String Dashboardname, String RenameSubscription, String CopyObjectName, String AddSubscriptionName, ITestContext context) throws InterruptedException
	{  
		//Combining the strings 
    	String CopyasSubscriptionName1=AddSubscriptionName+CopyObjectName;
    	System.out.println(CopyasSubscriptionName1);
    	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Search with the added Subscription name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyasSubscriptionName1);
    	Thread.sleep(LowSleep);
		
		//Select Rename From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Rename")).click();
    	Thread.sleep(MediumSleep);
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//app-mod-viewlet-object-rename/div/div/div[2]/input")).sendKeys(RenameSubscription);
    	Thread.sleep(LowSleep);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(HighSleep);
    	
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
    	    	    	
    	//Edit the search field data
    	for(int j=0; j<=CopyasSubscriptionName1.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);	
    	
    	 //discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
    	
    	//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(1000);
    	}
    	
    	//Search with renamed name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameSubscription);
    	Thread.sleep(LowSleep);
    	
    	//Store the Subscription name into string
    	String ModifiedName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	System.out.println(ModifiedName);
    	
    	//Verification condition
    	if(ModifiedName.contains(RenameSubscription))
    	{
    		System.out.println("The Subscription is renamed");
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Successfully renamed subscription rename command");
    	}
    	else
    	{
    		System.out.println("The Subscription rename is failed");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to rename subscription");
    		driver.findElement(By.xpath("Rename for subscription is failed")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "RenameSubscription"})
	@TestRail(testCaseId=191)
	@Test(priority=5, dependsOnMethods= {"RenameFromCommands"})
	public void DeleteFromCommands(String Dashboardname, String RenameSubscription,ITestContext context) throws InterruptedException
	{   
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Search with renamed name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameSubscription);
    	Thread.sleep(LowSleep);
		
		//Select Delete From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Delete")).click();
    	Thread.sleep(MediumSleep);
		
    	//Click on Yes
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(HighSleep);
    	    	    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	//Search with the new name
		for(int j=0; j<=RenameSubscription.length(); j++)
    	{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(RenameSubscription))
    	{
    		System.out.println("Subscription is not deleted");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to delete subscription using delete command");
    		driver.findElement(By.xpath("Subscription delete failed")).click();
    	}
    	else
    	{
    		System.out.println("Subscription is deleted");
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Subscription is deleted successfully using delete command");
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 1009)
	@Test(priority=6)
	public void MQSCSnapshot(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Save the Auth info name into string
		String Subscriptionid=driver.findElement(By.xpath("//datatable-body-cell[5]/div/span")).getText();
		System.out.println("Object ID is: " +Subscriptionid);
		
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
    	if(SnapshotData.contains(Subscriptionid))
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
			driver.findElement(By.id("Auth info snapshot failed")).click();
    	}
	}
	
	@TestRail(testCaseId = 1010)
	@Test(priority=7, dependsOnMethods= {"MQSCSnapshot"})
	public void SaveSubscriptionSnapshot(ITestContext context)
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
	
	@Parameters({"Dashboardname", "AddSubscriptionName"})
	@Test(priority=8, dependsOnMethods= {"CreateSubscriptionFromOptions"})
	@TestRail(testCaseId=192)
	public void SubscriptionProperties(String Dashboardname, String AddSubscriptionName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Search with subscription name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(AddSubscriptionName);
		Thread.sleep(LowSleep);
		
		//click on checkbox and choose properties
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(HighSleep);
				
		try
		{
		//storing the name field status into boolean
		boolean NameField=driver.findElement(By.id("name")).isEnabled();
		System.out.println(NameField);
		
		//Verification Condition
		if(NameField == false)
		{
			System.out.println("The Subscription name field is Disabled");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Subscription option is working fine");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(LowSleep);
			
			for(int j=0; j<=AddSubscriptionName.length(); j++)
	    	{
	    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
	    	}
		}
		else
		{
			System.out.println("The Subscription name field is Enabled");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Subscription option is not working properly");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.xpath("Subscription field is disabled")).click();
			
		}
		}
		catch (Exception e)
		{
			System.out.println("Exception Occured");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Exception occured while checking subscription properties, check details: "+ e.getMessage());
			
    		for(int j=0; j<=AddSubscriptionName.length(); j++)
        	{
        		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
        	}
    		
    		driver.findElement(By.id("yes")).click();
    		driver.findElement(By.id("Name field not disabled")).click();
		}
		Thread.sleep(4000);
		
	}
	
	@Parameters({"Dashboardname"})
    @Test(priority=9)
    @TestRail(testCaseId=193)
    public static void SubscriptionEvents(String Dashboardname, ITestContext context) throws InterruptedException 
    {
    	AllEvents obj=new AllEvents();
		obj.Events(Dashboardname, driver, context);
	}
	
    @Parameters({"Dashboardname", "FavoriteViewletName"})
    @TestRail(testCaseId=194)
	@Test(priority=10)
	public static void AddToFavoriteViewlet(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
    	//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
    	//Store the subscription Name into string
		String SubscriptionId=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[6]/div/span")).getText();
		//System.out.println(SubscriptionId);
		
		//Get subscription name
		String SubscriptionName=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println("Subscription name is: " +SubscriptionName);
		
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
		
		//Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		WebElement fav=driver.findElement(By.linkText("Add to favorites..."));
		JavascriptExecutor addfav = (JavascriptExecutor)driver;
		addfav.executeScript("arguments[0].click();", fav);
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
		/*
		 * Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		 * fav.selectByVisibleText(FavoriteViewletName); Thread.sleep(1000);
		 * driver.findElement(By.
		 * cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue"
		 * )).click(); Thread.sleep(1000);
		 */
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		//System.out.println(Favdata);
		
		//Verifiation of subscription added to favorite viewlet
		if(Favdata.contains(SubscriptionId) || Favdata.contains(SubscriptionName))
		{
			System.out.println("Subscription is added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Subscription successfully added to Favorite viewlet");
		}
		else
		{
			System.out.println("Subscription is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add subscription to Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);

	}
	
	
	@Test(priority=11)
	@TestRail(testCaseId=195)
	public static void CompareSubscription(ITestContext context) throws InterruptedException
	{
		
		CompareObjects com=new CompareObjects();
		com.Compare(driver, WGSName, context);
	}
	
	
	@TestRail(testCaseId = 780)
	@Test(priority=12)
	public void CheckDifferencesForSubscriptions(ITestContext context) throws InterruptedException
	{
		DifferenceOfObjects diff=new DifferenceOfObjects();
		diff.Differences(driver, context);
	}
	
	@Parameters({"Dashboardname", "AddSubscriptionNameFromIcon", "TopicStringDataFromICon"})
	@TestRail(testCaseId=201)
	@Test(priority=13)
	public void CreateSubscriptionFromPlusIcon(String Dashboardname, String AddSubscriptionNameFromIcon, String TopicStringDataFromICon, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		String[] Managers= {Manager1, Manager2};
		//System.out.println("Size of managers:" +Managers.length);
		for(int m=0; m<=1; m++)
		{
		//Click on + icon present in the listener viewlet
		driver.findElement(By.xpath("//img[@title='Add Subscription']")).click();
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
        
		//Click on Select path button
        driver.findElement(By.xpath("//button[contains(.,'Select path')]")).click();
        Thread.sleep(LowSleep);
		
		//Give the Subscription name
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(AddSubscriptionNameFromIcon);
		Thread.sleep(LowSleep);
		
		//Select the Topic name from the list
		try 
		{
			driver.findElement(By.id("topicName")).click();
			Thread.sleep(LowSleep);
			List<WebElement> Topic=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Topic.size());	
			for (int i=0; i<Topic.size();i++)
			{
				//System.out.println("Radio button text:" + Topic.get(i).getText());
				//System.out.println("Radio button id:" + Topic.get(i).getAttribute("id"));
				String s=Topic.get(i).getText();
				if(s.equals(DestinationTopicName))
				{
					String id=Topic.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(2000);
		
		//Topic string data
		driver.findElement(By.id("topicString")).sendKeys(TopicStringDataFromICon);
		Thread.sleep(LowSleep);
		
		//Click on Destination tab
		driver.findElement(By.linkText("Destination")).click();
		Thread.sleep(LowSleep);
		
		//Select WGS name
		try
		{
			Select DestinationWGS=new Select(driver.findElement(By.id("destinationGMName")));
			DestinationWGS.selectByVisibleText(DWGSIcon);  
		}
		catch (Exception ee)
		{
			Select DestinationWGS1=new Select(driver.findElement(By.xpath("//select")));
			DestinationWGS1.selectByVisibleText(DWGSIcon); 
			Thread.sleep(4000);
		}
		         
		
		//Select WGS name
		/*driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div")).click();
		driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div")).sendKeys("DESKTOP-E1JT2VR");
		//span[@class, 'ng-option-label ng-star-inserted']*/	
	
		try 
		{
			//driver.findElement(By.id("destinationNodeName")).click();
			driver.findElement(By.xpath("//ng-select[@id='destinationNodeName']/div/span[2]")).click();
			Thread.sleep(LowSleep);
			List<WebElement> Node=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Node.size());	
			for (int i=0; i<Node.size();i++)
			{
				//System.out.println("Radio button text:" + Node.get(i).getText());
				//System.out.println("Radio button id:" + Node.get(i).getAttribute("id"));
				String s=Node.get(i).getText();
				if(s.equals(Dnode))
				{
					String id=Node.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(1000);
		
		//Select Manager value
		try 
		{
			//driver.findElement(By.id("destinationQMName")).click();
			driver.findElement(By.xpath("//ng-select[@id='destinationQMName']/div/span[2]")).click();
			Thread.sleep(LowSleep);
			List<WebElement> Manager=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(Manager.size());	
			for (int i=0; i<Manager.size();i++)
			{
				//System.out.println("Radio button text:" + Manager.get(i).getText());
				//System.out.println("Radio button id:" + Manager.get(i).getAttribute("id"));
				String s=Manager.get(i).getText();
				if(Managers[m].contains(s))
				{
					String id=Manager.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(2000);
		
		//Select Queue name
		try 
		{
			//driver.findElement(By.id("destinationQName")).click();
			driver.findElement(By.xpath("//ng-select[@id='destinationQName']/div/span[2]")).click();
			Thread.sleep(LowSleep);
			List<WebElement> QueueName=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
			//System.out.println(QueueName.size());	
			for (int i=0; i<QueueName.size();i++)
			{
				//System.out.println("Radio button text:" + QueueName.get(i).getText());
				//System.out.println("Radio button id:" + QueueName.get(i).getAttribute("id"));
				String s=QueueName.get(i).getText();
				if(Managers[m].equalsIgnoreCase(Manager1))
				{
				if(s.equals(Manager1Queuename))
				{
					String id=QueueName.get(i).getAttribute("id");
					driver.findElement(By.id(id)).click();
					break;
				}
				}
				else
				{
					if(s.equals(Manager2Queuename))
					{
						String id=QueueName.get(i).getAttribute("id");
						driver.findElement(By.id(id)).click();
						break;
					}
				}
					
			}	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Thread.sleep(2000);
				
		//Click on OK button
		driver.findElement(By.xpath("//div[2]/div/div/div/button")).click();
    	Thread.sleep(HighSleep);
    	    	
    	try
    	{
    		driver.findElement(By.id("yes")).click();
    		Thread.sleep(LowSleep);
    		driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).click();
    		Thread.sleep(LowSleep);
    	}
    	catch (Exception e)
    	{
    		System.out.println("Error popup is occur");
    	}
    	
    	//Search with the added Subscription name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(AddSubscriptionNameFromIcon);
    	Thread.sleep(LowSleep);
	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	//Search with the new name
		for(int j=0; j<=AddSubscriptionNameFromIcon.length(); j++)
    	{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	//Verification
    	if(Subviewlet.contains(AddSubscriptionNameFromIcon))
    	{
    		System.out.println("Subscription is added successfully");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Subscription is created successfully using add Icon");
    	}
    	else
    	{
    		System.out.println("Subscription is not added to the viewlet");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create subscription using add Icon");
    		driver.findElement(By.xpath("Subscription failed")).click();
    	}
    	Thread.sleep(4000);
	}
	}
	
	@Parameters({"Dashboardname", "CopyObjectNameForMUltiple", "AddSubscriptionNameFromIcon"})
	@TestRail(testCaseId=196)
	@Test(priority=14, dependsOnMethods= {"CreateSubscriptionFromPlusIcon"})
	public void CopyAsFromCommandsForMultipleSubscriptions(String Dashboardname, String CopyObjectNameForMUltiple, String AddSubscriptionNameFromIcon, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Search withe Subscription names
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(AddSubscriptionNameFromIcon);
    	Thread.sleep(LowSleep);
    	
		//Select Copy As From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Copy As...")).click();
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
    	
    	//Get the existing subscription name
    	String ExistingSubscription=driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).getAttribute("value");
    	System.out.println("Existing subscription name: " +ExistingSubscription);
    	
    	//Give the object name
    	driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).sendKeys(CopyObjectNameForMUltiple);
    	Thread.sleep(LowSleep);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(HighSleep);
    	    	
    	try
    	{
    		driver.findElement(By.id("yes")).click();
    		Thread.sleep(LowSleep);
    		driver.findElement(By.cssSelector(".btn-danger")).click();
    		Thread.sleep(LowSleep);
    	}
    	catch (Exception e)
    	{
    		System.out.println("No exception occured");
    	}
    	
    	 //discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(LowSleep);
    	
    	FinalSubscription=ExistingSubscription+CopyObjectNameForMUltiple;
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinalSubscription);
    	Thread.sleep(LowSleep);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	for(int j=0; j<=FinalSubscription.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification condition
    	if(Subviewlet.contains(FinalSubscription))
    	{
    		System.out.println("Multiple Subscriptions are copied");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Copying multiple subscriptions is working fine");
    	}
    	else
    	{
    		System.out.println("Multiple Subscriptions are not copied");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to copy multiple subscriptions");
    		driver.findElement(By.xpath("Subscriptions failed to copy")).click();
    	}
    	Thread.sleep(1000);
		
	}
	
	@Parameters({"Dashboardname", "RenameSubscriptionForMultiple"})
	@TestRail(testCaseId=197)
	@Test(priority=15, dependsOnMethods= {"CopyAsFromCommandsForMultipleSubscriptions"})
	public void RenameFromCommandsForMultipleSubscriptions(String Dashboardname, String RenameSubscriptionForMultiple, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinalSubscription);
    	Thread.sleep(LowSleep);
    	
		//Select Rename From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Rename")).click();
    	Thread.sleep(MediumSleep);
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//app-mod-viewlet-object-rename/div/div/div[2]/input")).sendKeys(RenameSubscriptionForMultiple);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(HighSleep);
    	    	
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
    	
    	 //discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(MediumSleep);
    	
    	for(int j=0; j<=FinalSubscription.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameSubscriptionForMultiple);
    	Thread.sleep(LowSleep);
    	
    	//Store the Subscription name into string
    	String ModifiedName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	System.out.println(ModifiedName);
    	
    	for(int j=0; j<=RenameSubscriptionForMultiple.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification condition
    	if(ModifiedName.contains(RenameSubscriptionForMultiple))
    	{
    		System.out.println("Multiple Subscriptions ares renamed");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Renaming multiple subscriptions is working fine");
    	}
    	else
    	{
    		System.out.println("Multiple Subscriptions rename is failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to rename multiple subscriptions");
    		driver.findElement(By.xpath("Rename for Subscriptions is failed")).click();
    	}
    	Thread.sleep(1000);
		
	}
	
	@Parameters({"Dashboardname", "RenameSubscriptionForMultiple"})
	@TestRail(testCaseId=198)
	@Test(priority=16, dependsOnMethods= {"RenameFromCommandsForMultipleSubscriptions"})
	public void DeleteFromCommandsForMultipleSubscriptions(String Dashboardname, String RenameSubscriptionForMultiple, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameSubscriptionForMultiple);
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
    	Thread.sleep(HighSleep);
    	    	
    	/*//clear the search data
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	Thread.sleep(1000);*/
    	
    	 //discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	for(int j=0; j<=RenameSubscriptionForMultiple.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(RenameSubscriptionForMultiple))
    	{
    		System.out.println("Multiple Subscriptions is not deleted");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to delete multiple subscriptions");
    		driver.findElement(By.xpath("Multiple Subscriptions are failed")).click();
    	}
    	else
    	{
    		System.out.println("Subscriptions is deleted");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Deleting multiple subscriptions is working fine");
    	}
    	Thread.sleep(1000);
		
	}
	
	@Parameters({"Dashboardname", "SearchdataforMultipleProperties"})
	@Test(priority=17)
	@TestRail(testCaseId=199)
	public void MultipleSubscriptionProperties(String Dashboardname, String SearchdataforMultipleProperties, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Search with subscription name
		//driver.findElement(By.xpath("(//input[@type='text'])[2]")).clear();
		//driver.findElement(By.xpath("(//input[@type='text'])[2]")).sendKeys(SearchdataforMultipleProperties);
		//Thread.sleep(2000);
		
		//click on checkbox and choose properties
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
		
		WebElement ele=driver.findElement(By.id("topicString"));
		Actions a=new Actions(driver);
		a.moveToElement(ele).perform();
	
		//List<WebElement> data= (List<WebElement>) driver.findElement(By.tagName("app-text-input-tooltip")).findElements(By.xpath("//table//tr"));
		//String Tooltipdata=driver.findElement(By.tagName("app-text-input-tooltip")).getText();
		String Tooltipdata=driver.findElement(By.tagName("ngb-tooltip-window")).getText();
		System.out.println("Multiple Properties data:" +Tooltipdata);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(HighSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname,driver);
		
		//Search with subscription name
		//driver.findElement(By.xpath("(//input[@type='text'])[2]")).clear();
		//driver.findElement(By.xpath("(//input[@type='text'])[2]")).sendKeys(SearchdataforMultipleProperties);
		//Thread.sleep(2000);
				
		//click on checkbox and choose properties of first subscription
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
		
		String FistSubscription=driver.findElement(By.id("topicString")).getAttribute("value");
		
		FistSubscription=FistSubscription.trim().replaceAll(" +", " ");
		System.out.println("First subscription:" +FistSubscription);
		
		//Clsoe the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(HighSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che2=new ClearSelectionofCheckbox();
		che2.Deselectcheckbox(Dashboardname,driver);
		
		//Search with subscription name
		//driver.findElement(By.xpath("(//input[@type='text'])[2]")).clear();
		//driver.findElement(By.xpath("(//input[@type='text'])[2]")).sendKeys(SearchdataforMultipleProperties);
		//Thread.sleep(2000);
		
		//click on checkbox and choose properties of first subscription
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
		
		String SecondSubscription=driver.findElement(By.id("topicString")).getAttribute("value");
		
		SecondSubscription=SecondSubscription.trim().replaceAll(" +", " ");
		System.out.println("Second subscription:" +SecondSubscription);
		
		//Clsoe the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(HighSleep);
		
		for(int j=0; j<=SearchdataforMultipleProperties.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
		
		if(Tooltipdata.contains(FistSubscription) && Tooltipdata.contains(SecondSubscription))
		{
			System.out.println("Subscription multiple properties verified");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple subscription properties are verified successfully");
		}
		else
		{
			System.out.println("Subscription multiple properties not verified");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Verification failed for multiple subscription properties");
			driver.findElement(By.id("Multiple properties failed")).click();
		}
		Thread.sleep(1000);	
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=200)
	@Test(priority=18, dependsOnMethods= {"AddToFavoriteViewlet"})
	public static void AddToFavoriteForMultipleSubscription(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);		
    	
		//Store the Subscription ids into string
		String SubscriptionId2=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[6]/div/span")).getText();
		//System.out.println(SubscriptionId2);
		String SubscriptionId3=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[6]/div/span")).getText();
		//System.out.println(SubscriptionId3);
		
		//Subscription names 
		String SubscriptionName2=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("Subscription name 2: " +SubscriptionName2);
		String SubscriptionName3=driver.findElement(By.xpath("//datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("Subscription name 3: " +SubscriptionName3);
		
		//Select compare option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		WebElement fav=driver.findElement(By.linkText("Add to favorites..."));
		JavascriptExecutor addfav = (JavascriptExecutor)driver;
		addfav.executeScript("arguments[0].click();", fav);
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
		/*
		 * Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		 * fav.selectByVisibleText(FavoriteViewletName); Thread.sleep(1000);
		 * driver.findElement(By.
		 * cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue"
		 * )).click(); Thread.sleep(1000);
		 */
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println(Favdata);
		
		//Get the subscripton name from fav viewlet
		String SubscriptionName2fromfav=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("Subscription name2 from fav viewlet : " +SubscriptionName2fromfav);
		
		//Get the subscripton name from fav viewlet
		String SubscriptionName3fromfav=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("Subscription name3 from fav viewlet : " +SubscriptionName3fromfav);
		
		//Verification of subscriptions added to favorite viewlet
		if(Favdata.contains(SubscriptionId2) && Favdata.contains(SubscriptionId3) || SubscriptionName2fromfav.contains(SubscriptionName2) && SubscriptionName3fromfav.contains(SubscriptionName3))
		{
			System.out.println("Multiple Subscriptions are added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple subscription are added successfully to favorite viewlet");
		}
		else
		{
			System.out.println("Multiple Subscriptions are not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add multiple subscription to favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
	}
	

	@Parameters({"Dashboardname"})
	@Test(priority=25)
	public void Logout(String Dashboardname) throws InterruptedException 
	{
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
