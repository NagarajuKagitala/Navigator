package ApodGUI;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
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
public class ChannelViewlet 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String WGSName;
	static String Dnode;
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
		Dnode =Settings.getDnode();
		Manager2 =Settings.getManager2();
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
		
		// ---- Creating Channel Viewlet ----		
		Viewlets obj=new Viewlets();
		obj.IBMMQViewlet(driver, ViewletValue, ViewletName, WGSName, Node_Hostname);
		
		//Select the inactive checkbox
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);

		// Show Inactive channels check box
		WebElement Checkbox = driver.findElement(By.id("inactive-channels"));
		if (Checkbox.isSelected()) {
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
			Thread.sleep(LowSleep);
		} else {
			Checkbox.click();
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		Thread.sleep(MediumSleep);
	
	}
	
	@Parameters({"Dashboardname", "schemaName", "Attributes"})
	@TestRail(testCaseId = 100)
	@Test(priority=28)
	public static void ShowObjectAttributes(String Dashboardname, String schemaName, String Attributes, ITestContext context) throws InterruptedException
	{		
		try {
		ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.ChannelAttributes(Dashboardname, driver, schemaName, Attributes, WGSName);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Show object attributes working fine");
		
		}catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got exception while showing object attributes, check details: "+  e.getMessage());
			driver.findElement(By.id("Object attributes failed")).click();
		}
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=2)
	@TestRail(testCaseId = 772)
	public static void ShowChannelStatus(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Select channel status option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Show Channels Status")).click();
		Thread.sleep(LowSleep);
				
		//Channel status
		String ChannelStatus=driver.findElement(By.xpath("//datatable-body-cell[7]/div/span")).getText();
		System.out.println(ChannelStatus);
		
		//Popup page channel status
		String PopupStatus=driver.findElement(By.cssSelector(".dark-row > td:nth-child(4)")).getText();
		System.out.println(PopupStatus);
		
		//Verification of channel status
		if(ChannelStatus.equalsIgnoreCase("Inactive"))
		{
			if(PopupStatus.equalsIgnoreCase("MQRCCF_CHL_STATUS_NOT_FOUND"))
			{
				System.out.println("Inactive Channel status is verified");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Channel status is verified");
			}
			else
			{
				System.out.println("Channel status is not verified");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to verify channel status");
				//Close Channel status popup page
				driver.findElement(By.cssSelector(".close-button")).click();
				Thread.sleep(1000);
				driver.findElement(By.cssSelector("Channel status failed")).click();
			}
		}
		else if(ChannelStatus.equals(PopupStatus))
		{
			System.out.println("Channel status is verified");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Channel status is verified");
		}
		else
		{
			System.out.println("Channel status is not verified");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to verify channel status");
			//Close Channel status popup page
			driver.findElement(By.cssSelector(".close-button")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.cssSelector("Channel status failed")).click();
		}
		
		//Close Channel status popup page
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(MediumSleep);
	}
	
	@TestRail(testCaseId=758)
	@Parameters({"Dashboardname", "ChannelType", "ChannelName", "ConnectionName"})
	@Test(priority=3)
	public void CreateChannelFromIcon(String Dashboardname, String ChannelType, String ChannelName, String ConnectionName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Click on + icon
		driver.findElement(By.xpath("//img[@title='Add Channel']")).click();
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
				if(mdi.getText().equals(Manager2))
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
		
		//Select the channel type
		/*
		 * Select channel=new Select(driver.findElement(By.xpath(
		 * "//app-mod-select-object-path-for-create/div/div[2]/select")));
		 * channel.selectByVisibleText(ChannelType); Thread.sleep(2000);
		 */
        
        driver.findElement(By.xpath("//div[2]/ng-select[2]/div/span")).click();
        Thread.sleep(LowSleep);
        try 
		{
        	WebElement ChannelAuthManager=driver.findElement(By.className("ng-dropdown-panel")).findElement(By.className("ng-dropdown-panel-items"));
			List<WebElement> mdivs=ChannelAuthManager.findElements(By.tagName("div"));
			System.out.println(mdivs.size());	
			
			for (WebElement mdi : mdivs)
			{
				if(mdi.getText().equals(ChannelType))
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
        Thread.sleep(2000);
        
        
        //Click on Select path button
		driver.findElement(By.id("select-path")).click();
		Thread.sleep(LowSleep);
		
		//Channel name
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(ChannelName);
		Thread.sleep(LowSleep);
		
		//Connection name
		driver.findElement(By.id("connectionName")).clear();
		driver.findElement(By.id("connectionName")).sendKeys(ConnectionName);
		Thread.sleep(LowSleep);
		
		//Click on Transmission queue
		driver.findElement(By.cssSelector("#generalObjName input")).click();
		Thread.sleep(LowSleep);
		
		List <WebElement> tq=driver.findElement(By.className("ng-dropdown-panel-items")).findElements(By.className("ng-option"));
		System.out.println("List of elements are: " +tq.size());
		
		for(WebElement ss : tq)
		{
			//System.out.println("Radio button id:" + tq.get(ss).getAttribute("id"));
			WebElement value=ss.findElement(By.tagName("div"));
			System.out.println("Text is: " +value.getText());
			if(value.getText().equalsIgnoreCase("SYSTEM.CLUSTER.TRANSMIT.QUEUE"))
			{
				value.click();
				break;
			}
			
		}
		
		//Click on Ok button
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		Thread.sleep(HighSleep);
				
		try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.xpath("//div[2]/div/div/div[3]/button")).click();
			Thread.sleep(LowSleep);
		}
		catch(Exception e)
		{
			System.out.println("No error messages");
		}
		
		//discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
    	
		//Select the inactive checkbox
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);

		// Show Inactive channels check box
		WebElement Checkbox = driver.findElement(By.id("inactive-channels"));
		if (Checkbox.isSelected()) {
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		} else {
			Checkbox.click();
			driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		}
		Thread.sleep(MediumSleep);
		
		//Search with Added channel name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ChannelName);
		Thread.sleep(LowSleep);
		
		//Get the viewlet data into string
		String Viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
		//System.out.println("Channel data is: " +Viewletdata);
		
		//Remove the search data
		for(int i=0; i<=ChannelName.length(); i++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		
		//verification
		if(Viewletdata.contains(ChannelName))
		{
			System.out.println("Channel is added");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Channel is created successfully");
		}
		else
		{
			System.out.println("Channel is not added");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to create channel");
			driver.findElement(By.id("Channel adding failed")).click();
		}
	}
	
	@Test(priority=4)
	@TestRail(testCaseId = 102)
	public static void StartChannelFromCommands(ITestContext context) throws InterruptedException
	{
		//Select the Start option 
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Commands=new Actions(driver);
		Commands.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li")).click();
		Thread.sleep(5000);
		
		//Click on yes button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		//Store the channel status into string
		String Status=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[7]/div/span")).getText();
		
		//Verification
		if(Status.equalsIgnoreCase("Running") || Status.equalsIgnoreCase("Starting"))
		{
			System.out.println("Channel is started");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Channel started and running");
		}
		else
		{
			System.out.println("Channel is not started");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Channel failed to start");
			driver.findElement(By.xpath("Channle start failed")).click();
		}
		Thread.sleep(1000);	
	}
	
	@Test(priority=5)
	@TestRail(testCaseId = 103)
	public void StopChannelFromCommands(ITestContext context) throws InterruptedException
	{
		//Select the Stop option 
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Commands=new Actions(driver);
		Commands.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li[2]")).click();
		Thread.sleep(5000);
		
		//Click on yes button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		//Store the channel status into string
		String Status=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[7]/div/span")).getText();
		
		//Verification
		if(Status.equalsIgnoreCase("Stopping") || Status.equalsIgnoreCase("Stopped"))
		{
			System.out.println("Channel is stopped");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Channel stopped ");
		}
		else
		{
			System.out.println("Channel is not statoped");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to stop channel");
			driver.findElement(By.xpath("Channel stop failed")).click();
		}
		Thread.sleep(1000);	
	}
	
	@Test(priority=6)
	@TestRail(testCaseId = 104)
	public void PingChannelFromCommands(ITestContext context) throws InterruptedException
	{
		//Select the Ping channel option 
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Commands=new Actions(driver);
		Commands.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li[3]")).click();
		Thread.sleep(2000);
		
		//click on Ping option
		driver.findElement(By.xpath("//div[4]/button")).click();
		Thread.sleep(2000);
		
		try
		{
			if(driver.findElement(By.cssSelector(".confirm-btn")).isDisplayed())
			driver.findElement(By.cssSelector(".confirm-btn")).click();
			if(driver.findElement(By.cssSelector(".btn-primary")).isDisplayed())
			driver.findElement(By.cssSelector(".btn-primary")).click();
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Ping channel from command working fine");
		}
		catch (Exception e)
		{
			System.out.println("Ping channel exception occured");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception while pinging channel using command");
			if(driver.findElement(By.cssSelector(".btn-primary")).isDisplayed())
			driver.findElement(By.cssSelector(".btn-primary")).click();
		}
		Thread.sleep(1000);
	}
	
	@Test(priority=7)
	@TestRail(testCaseId = 105)
	public void ResolveChannelFromCommands(ITestContext context) throws InterruptedException
	{
		try {
		//Select the Resolve channel option 
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Commands=new Actions(driver);
		Commands.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li[4]")).click();
		Thread.sleep(2000);
		
		//Click on close button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Resolve channel from command is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception while resolving channel using command, check details: "+ e.getMessage());
		}
		
	}
	
	@Test(priority=8)
	@TestRail(testCaseId = 106)
	public void ResetChannelFromCommands(ITestContext context) throws InterruptedException
	{
		try {
		//Select the Resolve channel option 
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Commands=new Actions(driver);
		Commands.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]"))).perform();
		driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[4]/ul/li[5]")).click();
		Thread.sleep(2000);
		
		//Click on close button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		
		Alert al=driver.switchTo().alert();
		System.out.println(al.getText());
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Channel reset command is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception while resetting channel using command, check details: "+ e.getMessage());
		}
		
	}
	
	@Parameters({"Dashboardname", "CopyObjectName", "ChannelName", "CopyAsConnection"})
	@TestRail(testCaseId = 973)
	@Test(priority=9, dependsOnMethods= {"CreateChannelFromIcon"})
	public void CopyAsFromCommands(String Dashboardname, String CopyObjectName, String ChannelName, String CopyAsConnection, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with the added process name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ChannelName);
    	Thread.sleep(LowSleep);
		
		//Select Copy as From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Copy As...")).click();
    	Thread.sleep(LowSleep);
    	
    	//Give the object name
    	driver.findElement(By.id("name")).sendKeys(CopyObjectName);
    	Thread.sleep(LowSleep);
    	
    	//Give connection name
    	driver.findElement(By.id("connectionName")).clear();
    	driver.findElement(By.id("connectionName")).sendKeys(CopyAsConnection);
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
		catch(Exception e)
		{
			System.out.println("No error messages");
		}
    	
    	//discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
    	
    	//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(LowSleep);
    	}
    	
    	//Combining the strings 
    	String CopyasChannel=ChannelName+CopyObjectName;
    	System.out.println(CopyasChannel);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	//Edit the search field data
    	for(int j=0; j<=ChannelName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(LowSleep);
    	
    	//Verification condition
    	if(Subviewlet.contains(CopyasChannel))
    	{
    		System.out.println("Channel is copied");
    		context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "CopyAs command is working fine");
    	}
    	else
    	{
    		System.out.println("Channel is not copied");
    		context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "CopyAs command is nt working properly");
    		driver.findElement(By.xpath("Channel failed to copy")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "RenameChannel", "ChannelName", "CopyObjectName"})
	@TestRail(testCaseId = 974)
	@Test(priority=10, dependsOnMethods= {"CopyAsFromCommands"})
	public void RenameFromCommands(String Dashboardname, String RenameChannel, String ChannelName, String CopyObjectName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Combining the strings 
    	String CopyasChannelName=ChannelName+CopyObjectName;
    	System.out.println(CopyasChannelName);
    	
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyasChannelName);
    	Thread.sleep(LowSleep); 
    	
		//Select Rename From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Rename")).click();
    	Thread.sleep(LowSleep);
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//app-mod-viewlet-object-rename/div/div/div[2]/input")).sendKeys(RenameChannel);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	
    	try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(LowSleep);
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(LowSleep);
		}
		catch(Exception e)
		{
			System.out.println("No error messages");
		}
    	    	
    	//Edit the search field data
    	for(int j=0; j<=CopyasChannelName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(LowSleep);	
    	
    	//discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
    	
    	//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(LowSleep);
    	}
    	
    	//Search with renamed name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameChannel);
    	Thread.sleep(LowSleep); 
    	
    	//Store the Subscription name into string
    	String ModifiedName=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println("Viewlet data after Rename: " +ModifiedName);
    	
    	//Edit the new name
		for(int j=0; j<=RenameChannel.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(LowSleep);
    	
    	//Verification condition
    	if(ModifiedName.contains(RenameChannel))
    	{
    		System.out.println("The Channel is renamed");
    		context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Rename command is working fine");
    	}
    	else
    	{
    		System.out.println("The Channel rename is failed");
    		context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Channel rename command failed");
    		driver.findElement(By.xpath("Rename for Channel is failed")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@TestRail(testCaseId = 948)
	@Parameters({"Dashboardname", "ChannelName"})
	@Test(priority=11, dependsOnMethods= {"CreateChannelFromIcon"})
	public void DeleteChannel(String Dashboardname, String ChannelName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with Added channel name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(ChannelName);
		Thread.sleep(LowSleep);
		
		//Click on checkbox and choose delete option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions Commands=new Actions(driver);
		Commands.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Delete Channel")).click();
		Thread.sleep(LowSleep);
		
		//Click on Confirmation ok button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
		
		for(int i=0; i<=2; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(LowSleep);
		}
		
		
		//Get the viewlet data into string
		String Viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
		//System.out.println("Channel data is: " +Viewletdata);
		
		/*
		 * //Restore default settings
		 * driver.findElement(By.cssSelector(".fa-cog")).click();
		 * driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click
		 * (); Thread.sleep(4000);
		 * driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		 * Thread.sleep(3000);
		 */
		
		//Remove the search data
		for(int i=0; i<=ChannelName.length(); i++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		
		//verification
		if(!Viewletdata.contains(ChannelName))
		{
			System.out.println("Channel is Deleted");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Channel is Deleted successfully");
		}
		else
		{
			System.out.println("Channel is not Deleted");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to Delete channel");
			driver.findElement(By.id("Channel delete failed")).click();
		}
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 996)
	@Test(priority=12)
	public void MQSCSnapshot(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Save the Auth info name into string
		String Channelname=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println("Object name is: " +Channelname);
		
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
    	if(SnapshotData.contains(Channelname))
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
			driver.findElement(By.id("Channel snapshot failed")).click();
    	}
	}
	
	
	@TestRail(testCaseId = 997)
	@Test(priority=13, dependsOnMethods= {"MQSCSnapshot"})
	public void SaveChannelSnapshot(ITestContext context)
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
	@Test(priority=14)
	@TestRail(testCaseId = 107)
	public static void Properties(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select channel properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(HighSleep);
		
		//Store the editable function in to a string
		boolean FieldNamevalue=driver.findElement(By.id("name")).isEnabled();
		System.out.println(FieldNamevalue);
		
		//Verification of name field is editable or not
		if(FieldNamevalue == false)
		{
			System.out.println("Channel Name field is UnEditable");
			 driver.findElement(By.cssSelector(".btn-primary")).click();
			 context.setAttribute("Status", 1);
			 context.setAttribute("Comment", "Channel Name field is UnEditable");
			 Thread.sleep(MediumSleep);
		}
		else
		{
			System.out.println("Channel Name field is Editable");
			context.setAttribute("Status", 5);
			 context.setAttribute("Comment", "Channel Name field is Editable");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(MediumSleep);
			driver.findElement(By.xpath("Channel name edit function Failed")).click();
			
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=15)
	@TestRail(testCaseId = 108)
	public static void Events(String Dashboardname, ITestContext context) throws InterruptedException
	{
		AllEvents obj=new AllEvents();
		obj.Events(Dashboardname, driver, context);		
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId = 109)
	@Test(priority=16)
	public static void AddToFavorites(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//CLick on Viewlet and choose favorite viewlet crate check box
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
		
		//Submit
		driver.findElement(By.cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue")).click();
		Thread.sleep(MediumSleep);
		
		//Store String value
		String channelName=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("window.scrollBy(0,-350)", "");
	       
		//Select Add to Favorites option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(MediumSleep);
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
		
		//Get the data from favorite viewlet
		String Favoritedata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verify the channel added to Favorite viewlet
		if(Favoritedata.contains(channelName))
		{
			System.out.println("Channel is added to the favorite viewlet");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Channel added to favorite viewlet");
		}
		else
		{
			System.out.println("Channel is added to the favorite viewlet");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add Channel to favorite viewlet");
			driver.findElement(By.xpath("Channel condition failed")).click();
		}
		Thread.sleep(1000);
	}
	
	
	@Test(priority=17)
	@TestRail(testCaseId = 110)
	public void ComapareChannels(ITestContext context) throws InterruptedException
	{
		
		CompareObjects com=new CompareObjects();
		com.CompareforChannel(driver, WGSName, context);
	}
	
	
	@TestRail(testCaseId = 773)
	@Test(priority=18)
	public void CheckDifferencesForChannels(ITestContext context) throws InterruptedException
	{
		DifferenceOfObjects diff=new DifferenceOfObjects();
		diff.DifferencesforChannel(driver, context);
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=19)
	@TestRail(testCaseId = 111)
	public static void ShowChannelStatusForMultiple(String Dashboardname, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		Searchwithtype();
		
		//Store the Channel status into string              
		String ChannelStatus1=driver.findElement(By.xpath("//datatable-body-cell[7]/div/span")).getText();
		System.out.println("Channel1 Status from viewlet: " +ChannelStatus1);
		
		//Store second channel status into string
		String ChannelStatus2=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[7]/div/span")).getText();
		System.out.println("Channel2 Status from viewlet: " +ChannelStatus2);              
		
		//Select Two channels and choose show channel status
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Show Channels Status")).click();
		Thread.sleep(MediumSleep);
		
		//Show multiple channels status page
		String SecondChannelstatus=driver.findElement(By.xpath("//tr[5]/td[4]")).getText();
		System.out.println("Channel1 Status from popup: " +SecondChannelstatus);
		
		//Show second channel status
		String FirstchannelStatus=driver.findElement(By.xpath("//tr[3]/td[4]")).getText();
		System.out.println("Channel2 Status from popup: " +FirstchannelStatus);
		
		//Verification
		if(ChannelStatus1.equalsIgnoreCase("Inactive") && ChannelStatus2.equalsIgnoreCase("Inactive"))
		{
			
			if(FirstchannelStatus.equalsIgnoreCase("MQRCCF_CHL_STATUS_NOT_FOUND") && SecondChannelstatus.equalsIgnoreCase("MQRCCF_CHL_STATUS_NOT_FOUND"))
			{
				System.out.println("Inactive Channel status is verified");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Channel status is verified");
			}
			else
			{
				System.out.println("Channel status is not verified");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to verify channel status");
				//Close Channel status popup page
				driver.findElement(By.cssSelector(".close-button")).click();
				Thread.sleep(1000);
				driver.findElement(By.cssSelector("Channel status failed")).click();
			}
		}
		
		else if(ChannelStatus1.equalsIgnoreCase("Inactive") && !ChannelStatus2.equalsIgnoreCase("Inactive"))
		{
			
			if(FirstchannelStatus.equalsIgnoreCase("MQRCCF_CHL_STATUS_NOT_FOUND") && SecondChannelstatus.equalsIgnoreCase(ChannelStatus2))
			{
				System.out.println("Inactive Channel status is verified");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Channel status is verified");
			}
			else
			{
				System.out.println("Channel status is not verified");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to verify channel status");
				//Close Channel status popup page
				driver.findElement(By.cssSelector(".close-button")).click();
				Thread.sleep(1000);
				driver.findElement(By.cssSelector("Channel status failed")).click();
			}
		}
		
		else if(!ChannelStatus1.equalsIgnoreCase("Inactive") && ChannelStatus2.equalsIgnoreCase("Inactive"))
		{
			
			if(FirstchannelStatus.equalsIgnoreCase(ChannelStatus1) && SecondChannelstatus.equalsIgnoreCase("MQRCCF_CHL_STATUS_NOT_FOUND"))
			{
				System.out.println("Inactive Channel status is verified");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Channel status is verified");
			}
			else
			{
				System.out.println("Channel status is not verified");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to verify channel status");
				//Close Channel status popup page
				driver.findElement(By.cssSelector(".close-button")).click();
				Thread.sleep(1000);
				driver.findElement(By.cssSelector("Channel status failed")).click();
			}
		}
		else if(!ChannelStatus1.equals("Inactive") && !ChannelStatus2.equals("Inactive"))
		{
			
			if(FirstchannelStatus.equalsIgnoreCase(ChannelStatus1) && SecondChannelstatus.equalsIgnoreCase(ChannelStatus2))
			{
				System.out.println("Inactive Channel status is verified");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Channel status is verified");
			}
			else
			{
				System.out.println("Channel status is not verified");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to verify channel status");
				//Close Channel status popup page
				driver.findElement(By.cssSelector(".close-button")).click();
				Thread.sleep(1000);
				driver.findElement(By.cssSelector("Channel status failed")).click();
			}
		}
		else
		{
			System.out.println("All conditions failed");
		}
		
		//Close the popup window
		driver.findElement(By.cssSelector(".close-button")).click();
		Thread.sleep(MediumSleep);	
		
	}
	
	@Test(priority=20)
	@TestRail(testCaseId=112)
	public static void StartMultipleChannelsFromCommands(ITestContext context) throws InterruptedException
	{
		//Start multiple channels
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		
		Actions MousehoverstartCommands=new Actions(driver);
		MousehoverstartCommands.moveToElement(driver.findElement(By.cssSelector(".vertical-nav > .ng-star-inserted:nth-child(3)"))).perform();
		driver.findElement(By.cssSelector(".sub-menu > .ng-star-inserted:nth-child(1)")).click();
		Thread.sleep(4000);
		
		//Close the popup
		driver.findElement(By.xpath("//div[3]/button")).click();
		
		//Store the channels status into strings
		String Status1=driver.findElement(By.xpath("//datatable-body-cell[7]/div/span")).getText();
		String Status2=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[7]/div/span")).getText();
		
		if(Status1.equalsIgnoreCase("") && Status2.equalsIgnoreCase(""))
		{
			System.out.println("Multiple channels are Started");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Multiple channel started successfully");
		}
		else
		{
			System.out.println("Multiple channels are not Started");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to start multiple channel");
			driver.findElement(By.xpath("Multi channels Starting failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Test(priority=21)
	@TestRail(testCaseId=113)
	public void StopMultipleChannelsFromCommands(ITestContext context) throws InterruptedException
	{
		//Stop multiple Channels
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		
		Actions MousehoverStopCommands=new Actions(driver);
		MousehoverStopCommands.moveToElement(driver.findElement(By.cssSelector(".vertical-nav > .ng-star-inserted:nth-child(3)"))).perform();
		driver.findElement(By.cssSelector(".sub-menu > .ng-star-inserted:nth-child(2)")).click();
		Thread.sleep(4000);
				
		//Close the popup
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(1000);
		
		//Store the channels status into strings
		String Status1=driver.findElement(By.xpath("//datatable-body-cell[7]/div/span")).getText();
		String Status2=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[7]/div/span")).getText();
		
		if(Status1.equalsIgnoreCase("") && Status2.equalsIgnoreCase(""))
		{
			System.out.println("Multiple channels are Stopped");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Multiple channel stopped successfully");
		}
		else
		{
			System.out.println("Multiple channels are not Stopped");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to stop multiple channel");
			driver.findElement(By.xpath("Multi channels Stopping failed")).click();
		}
		Thread.sleep(1000);
	}
	
	
	@Parameters({"Dashboardname", "CopyAsNameForMultiple"})
	@TestRail(testCaseId = 1030)
	@Test(priority=22)
	public void CopyAsMultipleChannels(String Dashboardname, String CopyAsNameForMultiple, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		Searchwithtype();
		
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
					Thread.sleep(LowSleep);
					Actions CopyMousehover=new Actions(driver);
					CopyMousehover.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
					driver.findElement(By.linkText("Copy As...")).click();
					Thread.sleep(LowSleep);
					
					//Object Details
					driver.findElement(By.id("name")).clear();
					driver.findElement(By.id("name")).sendKeys(CopyAsNameForMultiple);
					Thread.sleep(LowSleep);
					
					driver.findElement(By.cssSelector(".btn-primary")).click();
					Thread.sleep(MediumSleep);
					
					try
					{
						driver.findElement(By.id("yes")).click();
						Thread.sleep(LowSleep);
						driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
						Thread.sleep(LowSleep);
					}
					catch(Exception e)
					{
						System.out.println("No error messages");
					}
					
					//Refresh viewlet
					for(int z=0; z<=2; z++)
					{
						driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
						Thread.sleep(LowSleep);
					}
					
					//Search with empty queue name
					driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
					driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyAsNameForMultiple);
					Thread.sleep(LowSleep);
										
					//Store the Queue name after deleting the Queue
					String ViewletData=driver.findElement(By.xpath("//datatable-body")).getText();
					System.out.println("Viewlet data is: " +ViewletData);
					
					for(int m=0; m<=CopyAsNameForMultiple.length(); m++)
					{
						driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
					}
					Thread.sleep(LowSleep);
					
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
	
	
	@Parameters({"Dashboardname", "CopyAsNameForMultiple", "RenameMultipleChannels"})
	@TestRail(testCaseId = 1031)
	@Test(priority=23)
	public void RenameMultipleChannels(String Dashboardname, String CopyAsNameForMultiple, String RenameMultipleChannels, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		//Searchwithtype();
		
		//Search with that name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyAsNameForMultiple);
    	Thread.sleep(LowSleep); 
    	
		//Select Rename From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Rename")).click();
    	Thread.sleep(LowSleep);
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//app-mod-viewlet-object-rename/div/div/div[2]/input")).sendKeys(RenameMultipleChannels);
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	
    	try
		{
			driver.findElement(By.id("yes")).click();
			Thread.sleep(3000);
			driver.findElement(By.xpath("//button[contains(.,'Cancel')]")).click();
			Thread.sleep(3000);
		}
		catch(Exception e)
		{
			System.out.println("No error messages");
		}
    	    	
    	//Edit the search field data
    	for(int j=0; j<=CopyAsNameForMultiple.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(LowSleep);	
    	
    	//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(LowSleep);
    	}
    	
    	//Search with renamed name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameMultipleChannels);
    	Thread.sleep(LowSleep); 
    	
    	//Store the Subscription name into string
    	String ModifiedName=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println("Viewlet data after Rename: " +ModifiedName);
    	
    	//Edit the new name
		for(int j=0; j<=RenameMultipleChannels.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(LowSleep);
    	
    	//Verification condition
    	if(ModifiedName.contains(RenameMultipleChannels))
    	{
    		System.out.println("The multiple Channels are renamed");
    		context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Multiple Rename command is working fine");
    	}
    	else
    	{
    		System.out.println("The Channels rename are failed");
    		context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Multiple Channel rename command failed");
    		driver.findElement(By.xpath("Rename for Multiple Channels are failed")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "RenameMultipleChannels"})
	@TestRail(testCaseId = 1032)
	@Test(priority=24)
	public void DeleteMultipleChannels(String Dashboardname, String RenameMultipleChannels, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with Added channel name
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameMultipleChannels);
		Thread.sleep(LowSleep);
		
		//Click on checkbox and choose delete option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		Actions Commands=new Actions(driver);
		Commands.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Delete Channel")).click();
		Thread.sleep(LowSleep);
		
		//Click on Confirmation ok button
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		for(int i=0; i<=2; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(LowSleep);
		}
		
		
		//Get the viewlet data into string
		String Viewletdata=driver.findElement(By.xpath("//datatable-body")).getText();
		//System.out.println("Channel data is: " +Viewletdata);
		
		/*
		 * //Restore default settings
		 * driver.findElement(By.cssSelector(".fa-cog")).click();
		 * driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click
		 * (); Thread.sleep(4000);
		 * driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		 * Thread.sleep(3000);
		 */
		
		//Remove the search data
		for(int i=0; i<=RenameMultipleChannels.length(); i++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
		
		//verification
		if(!Viewletdata.contains(RenameMultipleChannels))
		{
			System.out.println("Channel is Deleted");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Channel is Deleted successfully");
		}
		else
		{
			System.out.println("Channel is not Deleted");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to Delete channel");
			driver.findElement(By.id("Channel delete failed")).click();
		}
		
	}
	
	@Parameters({"Dashboardname", "ChannelDescription", "ChannelConnectionName"})
	@TestRail(testCaseId=114)
	@Test(priority=25)
	public void MultipleProperties(String Dashboardname, String ChannelDescription, String ChannelConnectionName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		Searchwithtype();
		
		//Select Two channels and choose properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(LowSleep);
		
		//Enter the Description
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys(ChannelDescription);
		Thread.sleep(LowSleep);
		
		//Enter the Connection name
		//driver.findElement(By.id("connectionName")).clear();
		//driver.findElement(By.id("connectionName")).sendKeys(ChannelConnectionName);
		
		//Close the Popup
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		Searchwithtype();
		
		//Open the properties of first channel
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(LowSleep);
		
		String FirstChannelDescription=driver.findElement(By.id("description")).getAttribute("value");
		//String FirstChannelConnection=driver.findElement(By.id("connectionName")).getAttribute("value");
		
		//Close the Popup
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che2=new ClearSelectionofCheckbox();
		che2.Deselectcheckbox(Dashboardname, driver);
		
		Searchwithtype();
		
		//Open the properties of second channel
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(LowSleep);
		
		String SecondChannelDescription=driver.findElement(By.id("description")).getAttribute("value");
		//String SecondChannelConnection=driver.findElement(By.id("connectionName")).getAttribute("value");
		
		//Close the Popup
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//Verification
		if(FirstChannelDescription.equals(ChannelDescription) && SecondChannelDescription.equals(ChannelDescription))
        {
        	System.out.println("Multiple channel properties are verified");
        	context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Multiple channel properties verified successfully");
        }
        else
        {
        	System.out.println("Multiple channel properties are not verified");
        	context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to verify multiple channel properties");
        	driver.findElement(By.xpath("Multiple properties failed")).click();
        }
        Thread.sleep(2000);
		
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=115)
	@Test(priority=26, dependsOnMethods= {"AddToFavorites"})
	public static void AddToFavoriteForMultipleChannels(String Dashboardname, String FavoriteViewletName,ITestContext context) throws InterruptedException
	{		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		Searchwithtype();
		
		//Storage of channel names    
		String channelname1=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("Second channel:" +channelname1);
		String channelname2=driver.findElement(By.xpath("//datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println("Third channel:" +channelname2);
		
		//Select Addto favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
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
		
		//Get the data from favorite viewlet
		String Favoritedata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verify the channel is added to Favorite viewlet
		if(Favoritedata.contains(channelname1) && Favoritedata.contains(channelname2))
		{
			System.out.println("Multiple channels are added into the Favorite Viewlet");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Multiple channel added to favorite viewlet");
		}
		else
		{
			System.out.println("Multiple channels are not added into the Favorite Viewlet");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add multiple channel to favorite viewlet");
			driver.findElement(By.xpath("favorite viewlet condition failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=30)
	public static void Logout(String Dashboardname) throws InterruptedException
	{
		//Restore default settings
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(MediumSleep);
		
		LogoutForAll lo=new LogoutForAll();
		lo.LogoutMethod(driver, Dashboardname);
	}
	
	public static void Searchwithtype() throws InterruptedException
	{
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys("Sender");
		Thread.sleep(LowSleep);
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
