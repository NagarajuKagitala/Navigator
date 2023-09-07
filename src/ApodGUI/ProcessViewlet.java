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
public class ProcessViewlet
{
	String FinalProcess="";
	static WebDriver driver;
	static String WGS_INDEX;
	static String Screenshotpath;
	static String WGSName;
	static String Dnode;
	static String DestinationManager;
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
		WGS_INDEX =Settings.getWGS_INDEX();
		Screenshotpath =Settings.getScreenshotPath();
		WGSName =Settings.getWGSNAME();
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
	
	@Test(priority=1)
	@TestRail(testCaseId = 116)
	@Parameters({"ViewletName", "ViewletValue"})
	public static void AddProcessViewlet(String ViewletName, int ViewletValue, ITestContext context) throws InterruptedException
	{
		Viewlets obj=new Viewlets();
		obj.IBMMQViewlet(driver, ViewletValue, ViewletName, WGSName, Node_Hostname);
		
		//Verification of process viewlet
		if(driver.getPageSource().contains(ViewletName))
		{
			System.out.println("Process Viewlet is created");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Process viewlet is created successfully");
		}
		else
		{
			System.out.println("Process viewlet is not created");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to create process viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
		
	}
	
	@Parameters({"NewProcessName", "ProcessDescription", "ApplicationId"})
	@TestRail(testCaseId = 117)
	@Test(priority=2)
	public void CreateProcessFromPlusIcon(String NewProcessName, String ProcessDescription, String ApplicationId, ITestContext context) throws InterruptedException
	{
		try
		{
		//Click on + Icon for creating the process
		driver.findElement(By.xpath("//img[@title='Add Process']")).click();
		Thread.sleep(LowSleep);
		
		//Select WGS
		/*
		 * Select WGS=new Select(driver.findElement(By.xpath("//select")));
		 * WGS.selectByVisibleText(WGSName); Thread.sleep(3000);
		 */
		
		/*driver.findElement(By.xpath("//app-mod-select-object-path-for-create/div/div/ng-select/div/span")).click();
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
		Thread.sleep(2000);*/
		
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
		
		//Click on Select path
        driver.findElement(By.xpath("//button[contains(.,'Select path')]")).click();
        Thread.sleep(LowSleep);
		
		//Enter the process name
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(NewProcessName);
		Thread.sleep(LowSleep);
		
		//Enter the description
		driver.findElement(By.id("description")).sendKeys(ProcessDescription);
		Thread.sleep(LowSleep);
		
		//Enter the application id
		driver.findElement(By.id("applicationId")).sendKeys(ApplicationId);
		Thread.sleep(LowSleep);
		
		//Click on Submit the process
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
		
		//Store the process viewlet data into string
		String Processdata=driver.findElement(By.xpath("//datatable-body")).getText();
		
		//Verification 
		
		if(Processdata.contains(NewProcessName))
		{
			System.out.println("process is created from the Icon option");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Process viewlet is created successfully using add icon");
		}
		else
		{
			System.out.println("process is not created from the Icon option");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to create process viewlet using add icon");
			//driver.findElement(By.cssSelector(".btn-danger")).click();
			driver.findElement(By.xpath("Process creation failed")).click();
		}
		}
		
		catch (Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception while creating the process, check details: "+ e.getMessage());
			System.out.println("Unable to create the Process");
		}
		
	}
	
	@Parameters({"Dashboardname", "schemaName"})
	@TestRail(testCaseId = 118)
	@Test(priority=29)
	public static void ShowObjectAttributesForProcess(String Dashboardname, String schemaName, ITestContext context) throws InterruptedException
	{
		try {
		ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.ObjectAttributesVerification(Dashboardname, driver, schemaName, WGSName);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Show Object Attribute page is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to show object attribute page, check details: "+ e.getMessage());
			driver.findElement(By.id("Attributes failed")).click();
		}
	}
	
	@Parameters({"Dashboardname", "CopyObjectName", "NewProcessName"})
	@TestRail(testCaseId = 119)
	@Test(priority=3, dependsOnMethods= {"CreateProcessFromPlusIcon"})
	public void CopyAsFromCommands(String Dashboardname, String CopyObjectName, String NewProcessName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with the added process name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(NewProcessName);
    	Thread.sleep(LowSleep);
    	
    			
		//Select Copy as From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Copy As...")).click();
    	Thread.sleep(LowSleep);
    	
    	//Give the object name   
    	driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).sendKeys(CopyObjectName);
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
    	String CopyasProcessName=NewProcessName+CopyObjectName;
    	System.out.println(CopyasProcessName);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	//Edit the search field data
    	for(int j=0; j<=NewProcessName.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
    	
    	//Verification condition
    	if(Subviewlet.contains(CopyasProcessName))
    	{
    		System.out.println("Process is copied");
    		context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "CopyAs command is working fine");
    	}
    	else
    	{
    		System.out.println("Process is not copied");
    		context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "CopyAs command is nt working properly");
    		driver.findElement(By.xpath("Process failed to copy")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "RenameProcess", "NewProcessName", "CopyObjectName"})
	@TestRail(testCaseId = 120)
	@Test(priority=4, dependsOnMethods= {"CopyAsFromCommands"})
	public void RenameFromCommands(String Dashboardname, String RenameProcess, String NewProcessName, String CopyObjectName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Combining the strings 
    	String CopyasProcessName=NewProcessName+CopyObjectName;
    	System.out.println(CopyasProcessName);
    	
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyasProcessName);
    	Thread.sleep(LowSleep); 
    	    	
		//Select Rename From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Rename")).click();
    	Thread.sleep(LowSleep);
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//app-mod-viewlet-object-rename/div/div/div[2]/input")).sendKeys(RenameProcess);
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
    	    	
    	//Edit the search field data
    	for(int j=0; j<=CopyasProcessName.length(); j++)
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
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameProcess);
    	Thread.sleep(LowSleep); 
    	
    	//Store the Subscription name into string
    	String ModifiedName=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println("Viewlet data after Rename: " +ModifiedName);
    	
    	//Edit the new name
		for(int j=0; j<=RenameProcess.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
		Thread.sleep(LowSleep);
    	
    	//Verification condition
    	if(ModifiedName.contains(RenameProcess))
    	{
    		System.out.println("The Process is renamed");
    		context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Rename command is working fine");
    	}
    	else
    	{
    		System.out.println("The Process rename is failed");
    		context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Process rename command failed");
    		driver.findElement(By.xpath("Rename for Process is failed")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "RenameProcess"})
	@TestRail(testCaseId = 121)
	@Test(priority=5, dependsOnMethods= {"RenameFromCommands"})
	public void DeleteFromCommands(String Dashboardname, String RenameProcess,ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with renamed name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameProcess);
    	Thread.sleep(LowSleep);   	
    	    	
		//Select Delete From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Delete")).click();
    	Thread.sleep(LowSleep);
		
    	//Click on Yes
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Search with the new name
		for(int j=0; j<=RenameProcess.length(); j++)
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
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println("Viewlet data after deleting: " +Subviewlet);
    	
    	
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(RenameProcess))
    	{
    		System.out.println("Process is not deleted");
    		context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Failed to delete process");
    		driver.findElement(By.xpath("Process delete failed")).click();
    	}
    	else
    	{
    		System.out.println("Process is deleted");
    		context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Process delete command is working fine");
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 995)
	@Test(priority=6)
	public void MQSCSnapshot(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Save the Auth info name into string
		String Processname=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println("Object name is: " +Processname);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select MQSCSnapshot option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("MQSC"))).perform();
    	driver.findElement(By.linkText("Snapshot...")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Get the snapshot data and store into string
    	String SnapshotData=driver.findElement(By.xpath("//textarea")).getText();
    	System.out.println("Snapshot data is: " +SnapshotData);
    	
    	//verification
    	if(SnapshotData.contains(Processname))
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
			driver.findElement(By.id("Process snapshot failed")).click();
    	}
	}
	
	@TestRail(testCaseId = 998)
	@Test(priority=7, dependsOnMethods= {"MQSCSnapshot"})
	public void SaveProcessSnapshot(ITestContext context)
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
	@Test(priority=8)
	@TestRail(testCaseId = 123)
	public void Properties(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select Properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
		
		//Store the Name field status into boolean
		boolean NameField=driver.findElement(By.id("name")).isEnabled();
		System.out.println(NameField);
		
		//Verification Condition
		if(NameField == false)
		{
			System.out.println("The Process name is Disabled");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Process name is disabled");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);
			
		}
		else
		{
			System.out.println("The Process name is Enable");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "The Process name is Enable");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(6000);
			driver.findElement(By.xpath("Process name field is Enable")).click();
		}
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=9)
	@TestRail(testCaseId = 124)
	public static void ProcessEvents(String Dashboardname, ITestContext context) throws InterruptedException
	{
		AllEvents obj=new AllEvents();
		obj.Events(Dashboardname, driver, context);
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId = 125)
	@Test(priority=10)
	public static void AddToFavoriteViewlet(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{		
		//Store process name into String 
		String ProcessName=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
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
		//driver.findElement(By.xpath("//app-modal-add-viewlet-favorite/div/div/div[2]/div/ng-select/div/span")).click();
		Thread.sleep(LowSleep);
		
		//Deselect WGS 
		driver.findElement(By.xpath("//span[2]/i")).click();
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
		
		//Select Add to Favorites option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(LowSleep);
		WebElement fav=driver.findElement(By.linkText("Add to favorites..."));
		JavascriptExecutor addfav = (JavascriptExecutor)driver;
		addfav.executeScript("arguments[0].click();", fav);
		Thread.sleep(LowSleep);
		
		//Select favorite viewlet
		driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div/ng-select/div/span")).click();
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
		
		//Verification of process added to the favorite viewlet
		if(Favdata.contains(ProcessName))
		{
			System.out.println("Process name is added to the Favorite viewlet");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Process name is added successfully to the Favorite viewlet");
		}
		else
		{
			System.out.println("Process name is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to add process name to the Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(2000);
	}
	
	@Parameters({"Dashboardname", "ProcessNameFromOptions", "ProcessDescriptionFromOptions", "ApplicationIdFromOptions"})
	@TestRail(testCaseId = 122)
	@Test(priority=11)
	public void CreateProcess(String Dashboardname, String ProcessNameFromOptions, String ProcessDescriptionFromOptions, String ApplicationIdFromOptions,ITestContext context) throws InterruptedException
	{	
		try
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);
			
		//Select create process option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.linkText("Create Process")).click();
		Thread.sleep(LowSleep);
		
		//Give the process name
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(ProcessNameFromOptions);
		Thread.sleep(LowSleep);
		
		/*//Select Topic name
		Select Topic=new Select(driver.findElement(By.xpath("//ng-select/div")));
		Topic.selectByIndex(0);
		
		//Topic String
		driver.findElement(By.id("topicString")).sendKeys(TopicString);
		
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(2000);*/ 
		
		//Enter the description
		driver.findElement(By.id("description")).sendKeys(ProcessDescriptionFromOptions);
		Thread.sleep(LowSleep);
		
		//Enter the application id
		driver.findElement(By.id("applicationId")).sendKeys(ApplicationIdFromOptions);
		Thread.sleep(LowSleep);
		
		//Click on Submit the process
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
		
		//Store the process viewlet data into string
		String Processdata=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println(Processdata);
		//Verification 
		
		if(Processdata.contains(ProcessNameFromOptions))
		{
			System.out.println("process is created from the options");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Process is created from the options");
		}
		else
		{
			System.out.println("process is not created from the options");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Failed to create process");
			//driver.findElement(By.cssSelector(".btn-danger")).click();
			driver.findElement(By.xpath("Process creation failed")).click();
		}
		}
		
		catch (Exception e)
		{
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Got an exception while creating process name, check details: "+ e.getMessage());
			//Click on Close button
			driver.findElement(By.xpath("//div[3]/button")).click();
			
			System.out.println("Unable to create the Process from options");
			
		}
	}
	
	
	@Test(priority=12)
	@TestRail(testCaseId = 126)
	public static void CompareProcessNames(ITestContext context) throws InterruptedException
	{
		CompareObjects com=new CompareObjects();
		com.Compare(driver, WGSName, context);
	}
	
	
	@TestRail(testCaseId = 774)
	@Test(priority=13)
	public void CheckDifferencesForProcess(ITestContext context) throws InterruptedException
	{
		DifferenceOfObjects diff=new DifferenceOfObjects();
		diff.Differences(driver, context);
	}
	
	@Parameters({"Dashboardname", "CopyObjectNameForMultiple"})
	@TestRail(testCaseId = 127)
	@Test(priority=14)
	public void CopyAsFromCommandsForMultipleProcess(String Dashboardname, String CopyObjectNameForMultiple, ITestContext context) throws InterruptedException
	{
		try
		{
				
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
					System.out.println("processes are matched");
					
				}
				else
				{
					//Clearing selection of object
					ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
					che.Deselectcheckbox(Dashboardname, driver);
					
					//Select the multiple processes and choose Add to favorite viewlet option
					driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
					driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper["+ k +"]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
					Actions Mousehovercopy=new Actions(driver);
					Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
			    	driver.findElement(By.linkText("Copy As...")).click();
			    	Thread.sleep(LowSleep);
					
					//Get the Existing process name
					String ExistingProcess=driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).getAttribute("value");
					System.out.println("Existing process name is: " +ExistingProcess); 
					
					//Give the object name
			    	driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).sendKeys(CopyObjectNameForMultiple);
			    	driver.findElement(By.cssSelector(".btn-primary")).click();
			    	Thread.sleep(MediumSleep);
			    	
			    	FinalProcess=ExistingProcess+CopyObjectNameForMultiple;
			    	
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
			    	Thread.sleep(LowSleep);
			    	
			    	//Search with that name
			    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
			    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinalProcess);
			    	Thread.sleep(LowSleep);
			    	
			    	//Store the viewlet data into string
			    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
			    	//System.out.println(Subviewlet);
			    	
			    	for(int j=0; j<=FinalProcess.length(); j++)
			    	{
			    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
			    	}
			    	
			    	//Verification condition
			    	if(Subviewlet.contains(FinalProcess))
			    	{
			    		System.out.println("Multiple Process are copied");
			    		context.setAttribute("Status",1);
			    		context.setAttribute("Comment", "Copying multiple process names working fine");
			    	}
			    	else
			    	{
			    		System.out.println("Multiple Process are not copied");
			    		context.setAttribute("Status",5);
			    		context.setAttribute("Comment", "Failed to copy multiple process names");
			    		driver.findElement(By.xpath("Multiple Process failed to copy")).click();
			    	}
			    	Thread.sleep(1000);
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
	
	@Parameters({"Dashboardname", "RenameProcessForMultiple"})
	@TestRail(testCaseId = 128)
	@Test(priority=15, dependsOnMethods= {"CopyAsFromCommandsForMultipleProcess"})
	public void RenameFromCommandsForMultipleProcess(String Dashboardname, String RenameProcessForMultiple, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		Thread.sleep(4000);
		
		//Search
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(FinalProcess);
    	Thread.sleep(LowSleep);
    	    	
		//Select the multiple processes and choose Add to favorite viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions Mousehovercopy=new Actions(driver);
		Thread.sleep(LowSleep);
		Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Rename")).click();
		Thread.sleep(LowSleep);
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//app-mod-viewlet-object-rename/div/div/div[2]/input")).sendKeys(RenameProcessForMultiple);
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
    	
    	for(int j=0; j<=FinalProcess.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(LowSleep);
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameProcessForMultiple);
    	Thread.sleep(LowSleep);
    	    	
    	//Store the Subscription name into string
    	String ModifiedName=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println(ModifiedName);
    	
    	for(int j=0; j<RenameProcessForMultiple.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification condition
    	if(ModifiedName.contains(RenameProcessForMultiple))
    	{
    		System.out.println("The multiple Process are renamed");
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Renaming multiple process names working fine");
    	}
    	else
    	{
    		System.out.println("The Multiple Process rename are failed");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to rename multiple process names");
    		driver.findElement(By.xpath("Rename for Multiple Processes are failed")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "RenameProcessForMultiple"})
	@TestRail(testCaseId = 129)
	@Test(priority=16, dependsOnMethods= {"RenameFromCommandsForMultipleProcess"})
	public void DeleteFromCommandsForMultipleProcess(String Dashboardname, String RenameProcessForMultiple, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Send the New name into field
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameProcessForMultiple);
    	Thread.sleep(LowSleep);
    	    	
    	//Select the multiple processes and choose Add to favorite viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions Mousehovercopy=new Actions(driver);
		Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
		driver.findElement(By.linkText("Delete")).click();
		Thread.sleep(LowSleep);
		
    	//Click on Yes
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	
    	for(int j=0; j<=RenameProcessForMultiple.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(LowSleep);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(RenameProcessForMultiple))
    	{
    		System.out.println("Process is not deleted");
    		context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to delete multiple process names using command");
    		driver.findElement(By.xpath("Process delete failed")).click();
    	}
    	else
    	{
    		context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Deleting multiple process names using command is working fine");
    		System.out.println("Process is deleted");
    	}
    	Thread.sleep(1000);
    	
    	/*//Clear the search data
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();*/
    	
	}
	
	
	@Parameters({"Dashboardname", "MultipleDescription", "AppID"})
	@TestRail(testCaseId = 130)
	@Test(priority=17)
	public void MultipleProperties(String Dashboardname, String MultipleDescription, String AppID, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the multiple processes and choose Add to favorite viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
		
		//Give the description for multiple process
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys(MultipleDescription);
		Thread.sleep(LowSleep);
		
		//Give the application id for multiple process
		driver.findElement(By.id("applicationId")).clear();
		driver.findElement(By.id("applicationId")).sendKeys(AppID);
		Thread.sleep(LowSleep);
		
		//click on OK
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//Open the properties for First process
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
		
		//Get the description and application id for First process
		String FirstDescription=driver.findElement(By.id("description")).getAttribute("value");
		String FirstApplicationID=driver.findElement(By.id("applicationId")).getAttribute("value");
		
		//close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che2=new ClearSelectionofCheckbox();
		che2.Deselectcheckbox(Dashboardname, driver);
		
		//Open the properties for First process
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
				
		//Get the description and application id for First process
		String SecondDescription=driver.findElement(By.id("description")).getAttribute("value");
		String SecondApplicationID=driver.findElement(By.id("applicationId")).getAttribute("value");
		
		//close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//Verification 
		if(FirstDescription.equals(MultipleDescription) && FirstApplicationID.equals(AppID) && SecondDescription.equals(MultipleDescription) && SecondApplicationID.equals(AppID))
		{
			System.out.println("Properites are Updated for multiple process");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Updating multiple process names is working fine");
		}
		else
		{
			System.out.println("Properites are not Updated for multiple process");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to update multiple process names");
			driver.findElement(By.xpath("Properties updation failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId = 131)
	@Test(priority=18, dependsOnMethods= {"AddToFavoriteViewlet"})
	public static void AddToFavoriteForMultipleProcess(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{		
		//Store the process Names into strings
		String ProcessName2=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println(ProcessName2);                
		String ProcessName3=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println(ProcessName3);  
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);              
		
		//Select the multiple processes and choose Add to favorite viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Thread.sleep(LowSleep);
		WebElement fav=driver.findElement(By.linkText("Add to favorites..."));
		JavascriptExecutor addfav = (JavascriptExecutor)driver;
		addfav.executeScript("arguments[0].click();", fav);
		Thread.sleep(LowSleep);
		
        //Select favorite viewlet
		driver.findElement(By.xpath("//app-mod-add-to-favorite-viewlet/div/div/ng-select/div/span")).click();
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
		
		//Verify the multiple processes added to favorite viewlet
		if(Favdata.contains(ProcessName2) && Favdata.contains(ProcessName3))
		{
			System.out.println("Multiple Process names are added to the Favorite viewlet");
			context.setAttribute("Status",1);
    		context.setAttribute("Comment", "Multiple process names are added to Favorite viewlet");
		}
		else
		{
			System.out.println("Multiple Process names are not added to the Favorite viewlet");
			context.setAttribute("Status",5);
    		context.setAttribute("Comment", "Failed to add multiple process names to Favorite viewlet");
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

