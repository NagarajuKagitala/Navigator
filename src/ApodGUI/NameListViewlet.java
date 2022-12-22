package ApodGUI;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
public class NameListViewlet 
{
	static WebDriver driver;
	
	static String Screenshotpath;
	static String WGSName;
	static String Manager1;
	static String Manager2;
	static String Dnode;
	static String FinalListenerName="";
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
		Manager1 =Settings.getManager1();
		Manager2 =Settings.getManager2();
		Dnode =Settings.getDnode();
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
		Thread.sleep(LowSleep);
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		
		
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
	@TestRail(testCaseId=168)
	@Parameters({"ViewletName", "ViewletValue"})
	public static void AddNameListViewlet(String ViewletName, int ViewletValue, ITestContext context) throws InterruptedException
	{
		Viewlets obj=new Viewlets();
		obj.IBMMQViewlet(driver, ViewletValue, ViewletName, WGSName, Node_Hostname);
		Thread.sleep(LowSleep);
		
		//System.out.println(driver.getPageSource());
		if(driver.getPageSource().contains(ViewletName))
		{
			System.out.println("NameList Viewlet is created");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Naming viewlet is created successfully");
		}
		else
		{
			System.out.println("NameList viewlet is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create Naming viewlet");
			driver.findElement(By.xpath("Not created")).click();
		}
		
	}
	
	@Parameters({"Dashboardname", "SchemaName"})
	@TestRail(testCaseId=169)
	@Test(priority=29)
	public void ShowObjectAttributesForNameList(String Dashboardname, String SchemaName, ITestContext context) throws InterruptedException
	{
		try {
		ObjectsVerificationForAllViewlets obj=new ObjectsVerificationForAllViewlets();
		obj.ObjectAttributesVerificationForNameList(Dashboardname, driver, SchemaName, WGSName);
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "Show object attributes for naming viewlet is working fine");
		}
		catch(Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Got an exception while Show object attributes for naming viewlet, check details:  "+ e.getMessage());
			driver.findElement(By.id("Attributes failed")).click();
		}
	}
	
	@Parameters({"Dashboardname", "NameListNameFromOptions", "NameListDescriptionFromOptions", "NameListNamesFromOptions"})
	@TestRail(testCaseId = 975)
	@Test(priority=2)
	public void CreateNameList(String Dashboardname, String NameListNameFromOptions, String NameListDescriptionFromOptions, String NameListNamesFromOptions,ITestContext context) throws InterruptedException
	{	
		try
		{
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname, driver);	
			
		//Select create process option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Create Namelist")).click();
		Thread.sleep(LowSleep);
		
		//Give the process name
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(NameListNameFromOptions);
		Thread.sleep(LowSleep);
				
		//Enter the description
		driver.findElement(By.id("description")).sendKeys(NameListDescriptionFromOptions);
		Thread.sleep(LowSleep);
		
		//Enter the application id
		//driver.findElement(By.id("namelistNames")).sendKeys(NameListNamesFromOptions);
		Thread.sleep(2000);
		
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
		
		//Search with renamed name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(NameListNameFromOptions);
    	Thread.sleep(LowSleep);
		
		//Store the process viewlet data into string
		String NameListdata=driver.findElement(By.xpath("//datatable-body")).getText();
		System.out.println(NameListdata);
		
		//Edit the new name
		for(int j=0; j<=NameListNameFromOptions.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
		Thread.sleep(LowSleep);
		
    	//Verification 
		if(NameListdata.contains(NameListNameFromOptions))
		{
			System.out.println("Name list is created from the options");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Name list is created from the options");
		}
		else
		{
			System.out.println("Name list is not created from the options");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Failed to create name list");
			//driver.findElement(By.cssSelector(".btn-danger")).click();
			driver.findElement(By.xpath("Name list creation failed")).click();
		}
		}
		
		catch (Exception e)
		{
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Got an exception while creating name list name, check details: "+ e.getMessage());
			//Click on Close button
			driver.findElement(By.xpath("//div[3]/button")).click();
			Thread.sleep(LowSleep);
			
			System.out.println("Unable to create the name list from options");
			
		}
	}
	
	@Parameters({"Dashboardname", "CopyObjectName", "NameListNameFromOptions"})
	@TestRail(testCaseId = 976)
	@Test(priority=3, dependsOnMethods= {"CreateNameList"})
	public void CopyAsFromCommands(String Dashboardname, String CopyObjectName, String NameListNameFromOptions, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with the added process name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(NameListNameFromOptions);
    	Thread.sleep(LowSleep);
    			
		//Select Copy as From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
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
    	    	
    	//Refresh the viewlet
    	for(int i=0; i<=2; i++)
    	{
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(LowSleep);
    	}
    	
    	//Combining the strings 
    	String CopyasNameList=NameListNameFromOptions+CopyObjectName;
    	System.out.println(CopyasNameList);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	//Edit the search field data
    	for(int j=0; j<=NameListNameFromOptions.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(LowSleep);
    	
    	//Verification condition
    	if(Subviewlet.contains(CopyasNameList))
    	{
    		System.out.println("Name list is copied");
    		context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "CopyAs command is working fine");
    	}
    	else
    	{
    		System.out.println("Name list is not copied");
    		context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "CopyAs command is nt working properly");
    		driver.findElement(By.xpath("Name list failed to copy")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "RenameNameList", "NameListNameFromOptions", "CopyObjectName"})
	@TestRail(testCaseId = 977)
	@Test(priority=4, dependsOnMethods= {"CopyAsFromCommands"})
	public void RenameFromCommands(String Dashboardname, String RenameNameList, String NameListNameFromOptions, String CopyObjectName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
    	ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
    	che.Deselectcheckbox(Dashboardname, driver);
    	
		//Combining the strings 
    	String CopyasNameListName=NameListNameFromOptions+CopyObjectName;
    	System.out.println(CopyasNameListName);
    	
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(CopyasNameListName);
    	Thread.sleep(LowSleep); 
    	    			
		//Select Rename From commands
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Rename")).click();
    	Thread.sleep(LowSleep);
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//app-mod-viewlet-object-rename/div/div/div[2]/input")).sendKeys(RenameNameList);
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
    	for(int j=0; j<=CopyasNameListName.length(); j++)
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
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameNameList);
    	Thread.sleep(LowSleep);
    	
    	//Store the Subscription name into string
    	String ModifiedName=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println("Viewlet data after Rename: " +ModifiedName);
    	
    	//Edit the new name
		for(int j=0; j<=RenameNameList.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
		Thread.sleep(LowSleep);
    	
    	//Verification condition
    	if(ModifiedName.contains(RenameNameList))
    	{
    		System.out.println("The Name list is renamed");
    		context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Rename command is working fine");
    	}
    	else
    	{
    		System.out.println("The Name list rename is failed");
    		context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Name list rename command failed");
    		driver.findElement(By.xpath("Rename for Name list is failed")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "RenameNameList"})
	@TestRail(testCaseId = 978)
	@Test(priority=5, dependsOnMethods= {"RenameFromCommands"})
	public void DeleteFromCommands(String Dashboardname, String RenameNameList,ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with renamed name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameNameList);
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
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println("Viewlet data after deleting: " +Subviewlet);
    	
    	//Search with the new name
		for(int j=0; j<=RenameNameList.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
		Thread.sleep(LowSleep);
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(RenameNameList))
    	{
    		System.out.println("Name list is not deleted");
    		context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Failed to delete Name list");
    		driver.findElement(By.xpath("Name list delete failed")).click();
    	}
    	else
    	{
    		System.out.println("Name list is deleted");
    		context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Name list delete command is working fine");
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 1003)
	@Test(priority=6)
	public void MQSCSnapshot(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Save the Auth info name into string
		String Namelistname=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println("Object name is: " +Namelistname);
				
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
    	if(SnapshotData.contains(Namelistname))
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
			driver.findElement(By.id("Name list snapshot failed")).click();
    	}
	}
	
	@TestRail(testCaseId = 1004)
	@Test(priority=7, dependsOnMethods= {"MQSCSnapshot"})
	public void SaveNamelistSnapshot(ITestContext context)
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
	@TestRail(testCaseId=979)
	public void NameListProperties(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//click on checkbox and choose properties
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
		
		//storing the name field status into boolean
		boolean NameField=driver.findElement(By.id("name")).isEnabled();
		System.out.println(NameField);
		
		//Verification Condition
		if(NameField == false)
		{
			System.out.println("The Name list name field is Disabled");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "The Name list name field is Disabled");
			driver.findElement(By.cssSelector(".btn-primary")).click();
		}
		else
		{
			System.out.println("The Name list name field is Enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "The Name list name field is Enabled");
			driver.findElement(By.cssSelector(".btn-primary")).click();
			Thread.sleep(4000);
			driver.findElement(By.xpath("Name list field is disabled")).click();
			
		}
		Thread.sleep(4000);
		
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=9)
	@TestRail(testCaseId=170)
	public static void NameListEvents(String Dashboardname, ITestContext context) throws InterruptedException
	{
		AllEvents obj=new AllEvents();
		obj.Events(Dashboardname, driver, context);
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=171)
	@Test(priority=10)
	public static void AddToFavoriteViewlet(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{		
		//Store the NamelistName into string
		String Namelist=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
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
		
		//Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(LowSleep);
		
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

		//Select the favorite viewlet name
		/*
		 * Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		 * fav.selectByVisibleText(FavoriteViewletName); Thread.sleep(1000);
		 * driver.findElement(By.
		 * cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue"
		 * )).click(); Thread.sleep(1000);
		 */
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification of Name list added to favorite viewlet
		if(Favdata.contains(Namelist))
		{
			System.out.println("Name list name is added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Naming List is added successfully to favorite viewlet");
		}
		else
		{
			System.out.println("Name list name is not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add Naming List to favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	
	@Test(priority=11)
	@TestRail(testCaseId=172)
	public static void CompareNameLists(ITestContext context) throws InterruptedException
	{
		CompareObjects com=new CompareObjects();
		com.Compare(driver, WGSName, context);
	}
	
	
	@TestRail(testCaseId = 777)
	@Test(priority=12)
	public void CheckDifferencesForNameList(ITestContext context) throws InterruptedException
	{
		DifferenceOfObjects diff=new DifferenceOfObjects();
		diff.Differences(driver, context);
	}
	
	@Parameters({"Dashboardname", "NameListFromICon", "DescriptionFromIcon"})
	@TestRail(testCaseId=980)
	@Test(priority=13)
	public void CreateNameListFromPlusIcon(String Dashboardname, String NameListFromICon, String DescriptionFromIcon, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
				
		String[] Managers= {Manager1, Manager2};
		for(int m=0; m<=1; m++)
		{
		//Click on + icon present in the viewlet
		driver.findElement(By.xpath("//img[@title='Add Namelist']")).click();
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
		
		//Create page
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(NameListFromICon);
		Thread.sleep(LowSleep);
		
		//Description
		driver.findElement(By.id("description")).sendKeys(DescriptionFromIcon);
		Thread.sleep(LowSleep);
		
		//Click on OK button
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
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
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(NameListFromICon);
		Thread.sleep(LowSleep);
		
		//Store the viewlet data into string
		String NameListdata=driver.findElement(By.xpath("//datatable-body")).getText();
		
		//Edit the search field data
    	for(int j=0; j<=NameListFromICon.length(); j++)
    	{
    	
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	Thread.sleep(4000);
		
    	//Verification condition
		if(NameListdata.contains(NameListFromICon))
		{
			System.out.println("Name list is created successfully from plus ICon");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Name list is created successfully using add Icon");
		}
		else
		{
			System.out.println("Name list is not created from Plus Icon");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Failed to create Name list using add Icon");
			driver.findElement(By.xpath("Name list create Failed")).click();
		}
		Thread.sleep(1000);
	}
	}
	
	@Parameters({"Dashboardname", "CopyObjectNameForMUltiple", "NameListFromICon"})
	@TestRail(testCaseId=981)
	@Test(priority=14, dependsOnMethods= {"CreateNameListFromPlusIcon"})
	public void CopyAsFromCommandsForMultiple(String Dashboardname, String CopyObjectNameForMUltiple, String NameListFromICon, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(NameListFromICon);
    	Thread.sleep(LowSleep);
    	    	
		//Select Copy as From commands
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Copy As...")).click();
    	Thread.sleep(LowSleep);
    	
    	//Get the existing name
    	String ExistingNameList=driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).getAttribute("value");
    	System.out.println("Existing name list name is: " +ExistingNameList);
    	
    	String ExistingNameList1=driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).getAttribute("value");
    	System.out.println("Existing name list name is: " +ExistingNameList1);
    	
    	driver.findElement(By.xpath("//app-mod-viewlet-object-copy-as/div/div[2]/div/input")).sendKeys(CopyObjectNameForMUltiple);
    	Thread.sleep(LowSleep);
    	
    	//click on ok button
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
    	
    	for(int j=0; j<=NameListFromICon.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	FinalListenerName=ExistingNameList+CopyObjectNameForMUltiple;
    	
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
    		System.out.println("Multiple Name lists are copied");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple Name lists are copied using CopyAs command");
    	}
    	else
    	{
    		System.out.println("Multiple Name lists are not copied");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to copy Multiple Name lists using CopyAs command");
    		driver.findElement(By.xpath("Namelists failed to copy")).click();
    	}
    	Thread.sleep(1000);	
	}
	
	@Parameters({"Dashboardname", "RenameNameListForMultiple", "CopyObjectNameForMUltiple"})
	@TestRail(testCaseId=982)
	@Test(priority=15, dependsOnMethods= {"CopyAsFromCommandsForMultiple"})
	public void RenameFromCommandsForMultiple(String Dashboardname, String RenameNameListForMultiple, String CopyObjectNameForMUltiple, ITestContext context) throws InterruptedException
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
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Rename")).click();
    	Thread.sleep(LowSleep);
		
    	//Send the New name into field
    	driver.findElement(By.xpath("//app-mod-viewlet-object-rename/div/div/div[2]/input")).sendKeys(RenameNameListForMultiple);
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
    	Thread.sleep(LowSleep);
    	
    	//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameNameListForMultiple);
    	Thread.sleep(LowSleep); 	
    	
    	//Store the Subscription name into string
    	String ModifiedName=driver.findElement(By.xpath("//datatable-body")).getText();
    	System.out.println(ModifiedName);
    	
    	for(int j=0; j<=RenameNameListForMultiple.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification condition
    	if(ModifiedName.contains(RenameNameListForMultiple))
    	{
    		System.out.println("Multiple Name lists are renamed");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple Name lists are renamed using rename command");
    	}
    	else
    	{
    		System.out.println("Multiple Name lists rename is failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to rename Multiple name lists using rename command");
    		driver.findElement(By.xpath("Rename for name lists is failed")).click();
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "RenameNameListForMultiple"})
	@TestRail(testCaseId=983)
	@Test(priority=16, dependsOnMethods= {"RenameFromCommandsForMultiple"})
	public void DeleteFromCommandsForMultiple(String Dashboardname, String RenameNameListForMultiple, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Search with that name
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    	driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(RenameNameListForMultiple);
    	Thread.sleep(LowSleep);
    	    	
		//Select Delete From commands
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Thread.sleep(LowSleep);
    	driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
    	Actions Mousehovercopy=new Actions(driver);
    	Mousehovercopy.moveToElement(driver.findElement(By.linkText("Commands"))).perform();
    	driver.findElement(By.linkText("Delete")).click();
    	Thread.sleep(LowSleep);
		
    	//Click on Yes
    	driver.findElement(By.cssSelector(".btn-primary")).click();
    	Thread.sleep(MediumSleep);
    	
    	//Refresh the viewlet
    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
    	Thread.sleep(LowSleep);
    	
    	//Store the viewlet data into string
    	String Subviewlet=driver.findElement(By.xpath("//datatable-body")).getText();
    	//System.out.println(Subviewlet);
    	
    	for(int j=0; j<=RenameNameListForMultiple.length(); j++)
    	{
    		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
    	}
    	
    	//Verification of Subscription delete
    	if(Subviewlet.contains(RenameNameListForMultiple))
    	{
    		System.out.println("Multiple Name lists are not deleted");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to delete Multiple Name lists using delete command");
    		driver.findElement(By.xpath("Multiple Name lists are failed")).click();
    	}
    	else
    	{
    		System.out.println("Name list is deleted");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple Name lists are deleted using delete command");
    	}
    	Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname", "NameListMultipleDescription"})
	@TestRail(testCaseId=984)
	@Test(priority=17)
	public void NameListMultipleProperties(String Dashboardname, String NameListMultipleDescription, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select Two Listeners and choose Add to favorite viewlet option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
		
		//give the description
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys(NameListMultipleDescription);
		Thread.sleep(LowSleep);
	
		//Close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(HighSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		
		//Open the first listener properties page
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
		
		//Store the First listener description into string
		String FirstDescription=driver.findElement(By.id("description")).getAttribute("value");
		
		//Close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(HighSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che2=new ClearSelectionofCheckbox();
		che2.Deselectcheckbox(Dashboardname, driver);
		
		//Open the second listener name
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(MediumSleep);
		
		//Store the second listener description into string
		String SecondDescription=driver.findElement(By.id("description")).getAttribute("value");
		
		//Close the properties page
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(HighSleep);
				
		//Verification
		if(FirstDescription.equals(NameListMultipleDescription) && SecondDescription.equals(NameListMultipleDescription))
		{
			System.out.println("Multiple Name list properties verified");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Multiple Name list properties are verified successfully");
		}
		else
		{
			System.out.println("Multiple Name list properties not verified");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to verify Multiple Name list properties");
			driver.findElement(By.id("Multiple properties failed")).click();
		}
		Thread.sleep(1000);		
	}
	
	@Parameters({"Dashboardname", "FavoriteViewletName"})
	@TestRail(testCaseId=173)
	@Test(priority=18, dependsOnMethods= {"AddToFavoriteViewlet"})
	public static void AddToFavoriteForMultipleNameLists(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{		
		//Store the Name list names into strings
		String Namelist2=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		String Namelist3=driver.findElement(By.xpath("//div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
				
		//Select Add to favorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[3]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(LowSleep);
		
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
		
		//Select the favorite viewlet name
		/*
		 * Select fav=new Select(driver.findElement(By.cssSelector(".fav-select")));
		 * fav.selectByVisibleText(FavoriteViewletName); Thread.sleep(1000);
		 * driver.findElement(By.
		 * cssSelector("div.g-block-bottom-buttons.buttons-block > button.g-button-blue"
		 * )).click(); Thread.sleep(1000);
		 */
		
		//Storing the Favorite Viewlet data
		String Favdata=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		
		//Verification of name lists added to favorite viewlet
		if(Favdata.contains(Namelist2) && Favdata.contains(Namelist3))
		{
			System.out.println("Multiple NameList names are added to the Favorite viewlet");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Adding Multiple NameList names to the Favorite viewlet is working fine");
		}
		else
		{
			System.out.println("Multiple Namelist names are not added to the Favorite viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to add Multiple NameList names to the Favorite viewlet");
			driver.findElement(By.xpath("Favorite condition failed")).click();
		}
		Thread.sleep(1000);
				
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=30)
	public static void Logout(String Dashboardname) throws InterruptedException
	{
		LogoutForAll lo=new LogoutForAll();
		lo.LogoutMethod(driver, Dashboardname);
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
