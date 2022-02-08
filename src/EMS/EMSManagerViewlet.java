package EMS;


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
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
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

import Common.ClearSelectionofCheckbox;
import Common.Dashboard;
import Common.LogoutForAll;
import Common.Viewlets;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class EMSManagerViewlet 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String DownloadPath;
	static String EMS_WGSNAME;
	static String UserName;
	static String UniqueEMSQueueName;
	static String UniqueEMSTopicName;
	static String EMSNode;
	static String Low;
	static String Medium;
	static String High;
	static int LowSleep;
	static int MediumSleep;
	static int HighSleep;
	static String UploadEMSApplyscripts;
	
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
		DownloadPath =Settings.getDownloadPath();
		EMS_WGSNAME =Settings.getEMS_WGSNAME();
		UserName =Settings.getUserName();
		UniqueEMSQueueName =Settings.getUniqueEMSQueueName();
		UniqueEMSTopicName =Settings.getUniqueEMSTopicName();
		EMSNode =Settings.getEMSNode();
		Low =Settings.getLow();
		Medium =Settings.getMedium();
		High =Settings.getHigh();
		LowSleep=Integer.parseInt(Low);
		MediumSleep =Integer.parseInt(Medium);
		HighSleep=Integer.parseInt(High);
		UploadEMSApplyscripts=Settings.getUploadEMSApplyscripts();
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
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		
		//Login page
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(10000);
		
		//Click on Dahboard icon
		Dashboard ob=new Dashboard();
		ob.DeleteExistDashboard(driver, Dashboardname);
		
		//Click on Create button
		//driver.findElement(By.xpath("//app-side-dashboard-menu/div/div/div[2]/div[2]")).click();				
		driver.findElement(By.cssSelector("div.block-with-border")).click();
		Thread.sleep(4000);
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
			
		//Click on Submit button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(6000);
		
		// ---- Creating Manager Viewlet ----		
		Viewlets obj=new Viewlets();
		obj.EMSViewlet(driver, ViewletValue, ViewletName, EMS_WGSNAME, EMSNode);
		
	}
	
	@Parameters({"Dashboardname", "SchemaName"})
	@TestRail(testCaseId=281)
	@Test(priority=24)
	public static void ShowObjectAttributes(String Dashboardname, String SchemaName, ITestContext context) throws InterruptedException
	{
		try {
		//Objects Verification
		EMSObjects obj=new EMSObjects();
		obj.ObjectAttributesVerificationforManager(Dashboardname, driver, SchemaName, EMS_WGSNAME);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Show Object Attributes option is working fine");
		}
		catch (Exception e) {
			// TODO: handle exception
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Show Object Attributes option is not working prperly, check details: "+ e.getMessage());
			driver.findElement(By.id("Attributes failed")).click();
		}
	}
	
		
	@Parameters({"Dashboardname", "Query", "Queue", "Queuename"})
	@TestRail(testCaseId = 785)
	@Test(priority=2)
	public static void EMSMQSCConsoleCommandOption(String Dashboardname, String Query, String Queue, String Queuename, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the Console option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("EMS Scripts"))).perform();
		driver.findElement(By.linkText("Console...")).click();
		Thread.sleep(6000);
		
		//Create a queue using query 
		driver.findElement(By.xpath("//app-mod-mqsc-console/div/div[2]/div/div/input")).sendKeys(Queue + Queuename);
		driver.findElement(By.xpath("//button[contains(.,'Submit')]")).click();
		Thread.sleep(6000);
		
		//Click on Clear button
		driver.findElement(By.xpath("//button[contains(.,'Clear')]")).click();
		Thread.sleep(3000);
		
		//Enter the Query and Click on Submit
		driver.findElement(By.xpath("//app-mod-mqsc-console/div/div[2]/div/div/input")).clear();
		driver.findElement(By.xpath("//app-mod-mqsc-console/div/div[2]/div/div/input")).sendKeys(Query);
		driver.findElement(By.xpath("//button[contains(.,'Submit')]")).click();
		Thread.sleep(6000);
		
		//Store the Console output into string
		String ConsoleOutput=driver.findElement(By.xpath("//textarea")).getAttribute("value");
		System.out.println("Responce data is: " +ConsoleOutput);
				
		if(ConsoleOutput.contains(Queuename))
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
			driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
			driver.findElement(By.id("Console Query failed")).click();
		}
		Thread.sleep(1000);				
	}
	
	@TestRail(testCaseId = 786)
	@Test(priority=3, dependsOnMethods= {"EMSMQSCConsoleCommandOption"})
	public void SaveMQSCConsoleResponceData(ITestContext context) throws InterruptedException
	{
		
		try
		{
			driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
			Thread.sleep(4000);
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
	@TestRail(testCaseId = 787)
	@Test(priority=4, dependsOnMethods= {"EMSMQSCConsoleCommandOption"})
	public void ClearMQSCConsoleResponceData(ITestContext context) throws InterruptedException
	{
		
		//Clear data by using clear button 
		driver.findElement(By.xpath("//button[contains(.,'Clear')]")).click();
		Thread.sleep(4000);
		
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
		Thread.sleep(1000);	
	}
	
	@Parameters({"Dashboardname", "exist", "newqueue"})
	@TestRail(testCaseId = 1072)
	@Test(priority=5)
	public static void ApplyEMSScript(String Dashboardname, String exist, String newqueue, ITestContext context) throws InterruptedException, AWTException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the Console option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("EMS Scripts"))).perform();
		driver.findElement(By.linkText("Apply script...")).click();
		Thread.sleep(3000);
		
		//Click on load from file
		driver.findElement(By.xpath("//button[contains(.,'Load from file')]")).click();
		Thread.sleep(2000);
		
		//Loading the file into queue by using robot class
		String filepath=System.getProperty("user.dir") + "\\" + UploadEMSApplyscripts;
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
	    Thread.sleep(6000);
	    
	    //Store the result into string
	    String Result=driver.findElement(By.xpath("//div[2]/div[2]/div/textarea")).getAttribute("value");
	   // System.out.println("mmf script results: " +Result);
	    
	    if(Result.contains(exist) || Result.contains(newqueue))
	    {
	    	System.err.println("ems script file is executed successfully");
	    	context.setAttribute("Status", 1);
			context.setAttribute("Comment", "ems script is executed");
	    }
	    else
	    {
	    	System.err.println("ems script is not executed");
	    	context.setAttribute("Status", 5);
			context.setAttribute("Comment", "ems script is not executed");
			
			//close the script console popup
		    driver.findElement(By.cssSelector(".btn-danger")).click();
		    Thread.sleep(3000);
			driver.findElement(By.id("ems applyscript failed")).click();
	    }
	}
	
	
	@TestRail(testCaseId = 1073)
	@Test(priority=6, dependsOnMethods= {"ApplyEMSScript"})
	public static void SaveEMSScriptResults(ITestContext context) throws InterruptedException
	{
		try
		{
			//Click on Save button
			driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
			Thread.sleep(6000);
			
			System.err.println("ems apply script data is saved");
	    	context.setAttribute("Status", 1);
			context.setAttribute("Comment", "ems apply script is saved");
		}
		catch(Exception e)
		{
			System.err.println("ems apply script is not saved");
	    	context.setAttribute("Status", 5);
			context.setAttribute("Comment", "ems apply script is not saved");
			driver.findElement(By.id("ems apply script save failed")).click();
		}
		
	}
	
	
	@TestRail(testCaseId = 1074)
	@Test(priority=7, dependsOnMethods= {"ApplyEMSScript"})
	public static void ClearEMSScriptResults(ITestContext context) throws InterruptedException
	{
		//Click on clear button
		driver.findElement(By.xpath("//button[contains(.,'Clear')]")).click();
		Thread.sleep(3000);  
		
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
	    Thread.sleep(3000);
	}
	
	@TestRail(testCaseId=819)
	@Parameters({"Dashboardname", "GroupName", "GoupDescription"})
	@Test(priority=8)
	public void CreateUserGroup(String Dashboardname, String GroupName, String GoupDescription, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the User Group option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Manage"))).perform();
		driver.findElement(By.linkText("User Groups")).click();
		Thread.sleep(6000);
		
		//Click on Create button
		driver.findElement(By.cssSelector(".fl-right:nth-child(2) > .g-button-blue")).click();
		Thread.sleep(3000);
		
		//Group name
		driver.findElement(By.name("emsprincname")).sendKeys(GroupName);
		driver.findElement(By.name("emsprincedescription")).sendKeys(GoupDescription);
		
		/*
		 * //Select User name driver.findElement(By.xpath("//td[contains(.,'"+ UserName
		 * +"')]")).click();
		 * 
		 * //Add user driver.findElement(By.xpath("//div[3]/div[2]/button[2]")).click();
		 */
		
		//Click on save button
		driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
		Thread.sleep(8000);
		
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
		
		//Search with the Group name
		driver.findElement(By.xpath("//app-m-ems-server-principals/div/div/div/div/div/input")).sendKeys(GroupName);
		
		//Store the groups into string
		String Groups=driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body")).getText();
		System.out.println("Groups are: " +Groups);
		
		for(int k=0; k<=GroupName.length(); k++)
		{
			driver.findElement(By.xpath("//app-m-ems-server-principals/div/div/div/div/div/input")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Verification
		if(Groups.contains(GroupName))
		{
			System.out.println("Group is created successfully");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "EMS Group is added");
		}
		else
		{
			System.out.println("Group is not created");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "EMS group not added");
    		
    		//Close the popup
    		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
    		Thread.sleep(4000);
    		driver.findElement(By.id("Group creation failed")).click();
		}
		Thread.sleep(4000);
		
		//Close the popup
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
		Thread.sleep(4000);
	}
	
	@TestRail(testCaseId=820)
	@Parameters({"Dashboardname", "GroupName", "UpdateGoupDescription"})
	@Test(priority=9, dependsOnMethods= {"CreateUserGroup"})
	public void EditUserGroup(String Dashboardname, String GroupName, String UpdateGoupDescription, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the User Group option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Manage"))).perform();
		driver.findElement(By.linkText("User Groups")).click();
		Thread.sleep(6000);
		
		//Search with the Group name
		driver.findElement(By.xpath("//app-m-ems-server-principals/div/div/div/div/div/input")).sendKeys(GroupName);
		Thread.sleep(3000);
		
		//Select group name
		//driver.findElement(By.xpath("//span[contains(.,'"+ GroupName +"')]")).click();
		driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell/div")).click();
		Thread.sleep(6000);
		
		
		//click on edit button
		driver.findElement(By.xpath("//button[contains(.,'Edit...')]")).click();
		Thread.sleep(4000);
		
		//update description
		driver.findElement(By.name("emsprincedescription")).clear();
		driver.findElement(By.name("emsprincedescription")).sendKeys(UpdateGoupDescription);
		
		//Click on Apply button
		driver.findElement(By.xpath("//button[contains(.,'Apply')]")).click();
		Thread.sleep(6000);
		
		for(int k=0; k<=GroupName.length(); k++)
		{
			driver.findElement(By.xpath("//app-m-ems-server-principals/div/div/div/div/div/input")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Store the groups into string
		String Groupsdata=driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body")).getText();
		System.out.println("Groups are: " +Groupsdata);
		
		//Verification condition
		if(Groupsdata.contains(UpdateGoupDescription))
		{
			System.out.println("User group updated");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "User Group is updated");
		}
		else
		{
			System.out.println("User group not edited");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "User group is not updated");
    		
    		//Close the popup
    		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
    		Thread.sleep(4000);
    		driver.findElement(By.id("User Group update failed")).click();
		}
		
		//Close the popup
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
		Thread.sleep(4000);
	}
	
	@TestRail(testCaseId=821)
	@Parameters({"Dashboardname", "GroupName"})
	@Test(priority=10, dependsOnMethods= {"CreateUserGroup", "EditUserGroup"})
	public void DeleteUserGroup(String Dashboardname, String GroupName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the User Group option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Manage"))).perform();
		driver.findElement(By.linkText("User Groups")).click();
		Thread.sleep(6000);
		
		//Search with the Group name
		driver.findElement(By.xpath("//app-m-ems-server-principals/div/div/div/div/div/input")).sendKeys(GroupName);
		
		//Select group name
		//driver.findElement(By.xpath("//span[contains(.,'"+ GroupName +"')]")).click();
		driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell/div")).click();
		
		//Select group name
		//driver.findElement(By.xpath("//span[contains(.,'"+ GroupName +"')]")).click();
		
		//Click on Delete button
		driver.findElement(By.xpath("//span[3]/button/span")).click();
		
		//Click on Confirmation
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(4000);
		
		//Store the groups into string
		String Groupsdata=driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body")).getText();
		System.out.println("Groups are: " +Groupsdata);
		
		//Verification condition
		if(Groupsdata.contains(GroupName))
		{
			System.out.println("User group not deleted");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "EMS user group not deleted");
    		
    		//Close the popup
    		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
    		Thread.sleep(4000);
    		driver.findElement(By.id("Group delete failed")).click();
		}
		else
		{
			System.out.println("User group deleted");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "User Group is deleted");
		}
		
		//Close the popup
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
		Thread.sleep(4000);
	}
	
	@TestRail(testCaseId=822)
	@Parameters({"Dashboardname", "Username", "UserPassword", "UserDescription"})
	@Test(priority=11)
	public void CreateUser(String Dashboardname, String Username, String UserPassword, String UserDescription, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the User Group option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Manage"))).perform();
		driver.findElement(By.linkText("Users")).click();
		Thread.sleep(6000);
				
		//Click on Create button
		driver.findElement(By.xpath("//span[2]/button")).click();
		
		//Give username
		driver.findElement(By.name("emsprincname")).clear();
		driver.findElement(By.name("emsprincname")).sendKeys(Username);
		
		//Give password
		driver.findElement(By.name("emsprincepassword2")).clear();
		driver.findElement(By.name("emsprincepassword2")).sendKeys(UserPassword);
		
		//description
		driver.findElement(By.name("emsprincedescription")).sendKeys(UserDescription);
		
		//click on save button
		driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
		Thread.sleep(4000);
		
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
		
		//Search with the Username
		driver.findElement(By.cssSelector(".block-filter:nth-child(1) .main-filter-by-input")).sendKeys(Username);
		
		//Store the Users data into string           
		String UsersData=driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body")).getText();
		System.out.println("Users data is: " +UsersData);
		
		for(int k=0; k<=Username.length(); k++)
		{
			driver.findElement(By.cssSelector(".block-filter:nth-child(1) .main-filter-by-input")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Verification
		if(UsersData.contains(Username))
		{
			System.out.println("User is created successfully");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "User is created");
		}
		else
		{
			System.out.println("User is not created");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "User is not created");
			
			//close the popup page
    		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
			driver.findElement(By.id("Create user failed")).click();
		}
		
		//close the popup page
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
	}
	
	@TestRail(testCaseId=823)
	@Parameters({"Dashboardname", "Username", "UserPassword", "UpdateUserDescription"})
	@Test(priority=12, dependsOnMethods= {"CreateUser"})
	public void EditUser(String Dashboardname, String Username, String UserPassword, String UpdateUserDescription, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the User Group option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Manage"))).perform();
		driver.findElement(By.linkText("Users")).click();
		Thread.sleep(4000);
		
		//Search with the Username
		driver.findElement(By.cssSelector(".block-filter:nth-child(1) .main-filter-by-input")).sendKeys(Username);
		Thread.sleep(4000);
		
		//click on User
		//driver.findElement(By.xpath("//span[contains(.,'"+ Username +"')]")).click();
		driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell/div")).click();
		Thread.sleep(6000);
		
		
		//Click on Edit button
		driver.findElement(By.xpath("//button[contains(.,'Edit...')]")).click();
		Thread.sleep(6000);
		
		//Update description
		driver.findElement(By.name("emsprincedescription")).clear();
		driver.findElement(By.name("emsprincedescription")).sendKeys(UpdateUserDescription);
		Thread.sleep(4000);
		
		//Give password
		driver.findElement(By.name("emsprincepassword2")).clear();
		driver.findElement(By.name("emsprincepassword2")).sendKeys(UserPassword);
		Thread.sleep(4000);
		
		//Click on Apply button
		driver.findElement(By.xpath("//button[contains(.,'Apply')]")).click();
		Thread.sleep(4000);
		
		for(int k=0; k<=Username.length(); k++)
		{
			driver.findElement(By.cssSelector(".block-filter:nth-child(1) .main-filter-by-input")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Store the Users data into string
		String UsersData=driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body")).getText();
		System.out.println("Users data is: " +UsersData);
				
		//Verification
		if(UsersData.contains(UpdateUserDescription))
		{
			System.out.println("User is updated");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "User is updated");
		}
		else
		{
			System.out.println("User is not updated");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "User is not updated");
			
			//close the popup page
    		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
			driver.findElement(By.id("User update failed")).click();
		}
		
		//close the popup page
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
	}
	
	@TestRail(testCaseId=824)
	@Parameters({"Dashboardname", "Username"})
	@Test(priority=13, dependsOnMethods= {"CreateUser", "EditUser"})
	public void DeleteUser(String Dashboardname, String Username, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the User Group option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Manage"))).perform();
		driver.findElement(By.linkText("Users")).click();
		Thread.sleep(4000);
		
		//Search with the Username
		driver.findElement(By.cssSelector(".block-filter:nth-child(1) .main-filter-by-input")).sendKeys(Username);
		
		//click on User
		//driver.findElement(By.xpath("//span[contains(.,'"+ Username +"')]")).click();
		driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell/div")).click();
		
		
		//click on User
		//driver.findElement(By.xpath("//span[contains(.,'"+ Username +"')]")).click();
		
		//Click on delete button and confirmation button
		driver.findElement(By.xpath("//span[3]/button")).click();
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(6000);
		
		//Store the Users data into string
		String UsersData=driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body")).getText();
		System.out.println("Users data is: " +UsersData);
				
		//Verification
		if(UsersData.contains(Username))
		{
			System.out.println("User is not deleted");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "User is not deleted");
			
			//close the popup page
    		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
			driver.findElement(By.id("User delete failed")).click();
		}
		else
		{
			System.out.println("User is deleted");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "User is deleted");
		}
		
		//close the popup page
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
	}
	
	@TestRail(testCaseId=825)
	@Parameters({"Dashboardname", "NewUser", "Newpassword", "Description", "Type"})
	@Test(priority=14)
	public void AddQueueACLToUser(String Dashboardname, String NewUser, String Newpassword, String Description, String Type, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		CreateUser(NewUser, Newpassword, Description);
		
		//Search with the name
		driver.findElement(By.cssSelector(".block-filter:nth-child(1) .main-filter-by-input")).sendKeys(NewUser);
		
		//Click on user
		driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell/div")).click();
		
		//click on User
		//driver.findElement(By.xpath("//span[contains(.,'"+ NewUser +"')]")).click();
		
		//Click on Edit button
		driver.findElement(By.xpath("//button[contains(.,'Edit...')]")).click();
		
		//Select Type
		Select type=new Select(driver.findElement(By.name("typeSelector")));
		type.selectByVisibleText(Type);
		Thread.sleep(3000);
		
		//Search with queue name
		driver.findElement(By.name("permFilter")).sendKeys(UniqueEMSQueueName);
		
		//Give View permissions
		boolean Queue=driver.findElement(By.xpath("//datatable-body-cell[5]/div/input")).isSelected();
		System.out.println("Viewcheckbox :" +Queue);
		
		if(Queue)
		{
			System.out.println("View permissions given");
		}
		else
		{
			driver.findElement(By.xpath("//datatable-body-cell[5]/div/input")).click();
		}
		Thread.sleep(3000);
		
		//Click on Apply button
		driver.findElement(By.xpath("//button[contains(.,'Apply')]")).click();
		Thread.sleep(6000);
		
		//click on Acl's tab
		driver.findElement(By.linkText("ACLs")).click();
		Thread.sleep(4000);
		
		//Search with the name
		driver.findElement(By.cssSelector(".block-filter:nth-child(1) .main-filter-by-input")).sendKeys(NewUser);
		Thread.sleep(3000);
		
		//Store the data into string
		String ACLSData=driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body")).getText();
		System.out.println("ACL's data is: " +ACLSData);
		
		if(ACLSData.contains(UniqueEMSQueueName))
		{
			System.out.println("Queue is added to the ACL's");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Queue added to ACL");
		}
		else
		{
			System.out.println("Queue is not added to the ACL's");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Queue is not added to ACL");
    		
    		//close the popup page
    		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
    		driver.findElement(By.id("Queue is not added to ACL")).click();
		}
		
		//close the popup page
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
	}
	
	@TestRail(testCaseId=826)
	@Parameters({"Dashboardname", "NewUser", "Type1"})
	@Test(priority=15, dependsOnMethods= {"AddQueueACLToUser"})
	public void AddTopicACLToUser(String Dashboardname, String NewUser, String Type1, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the User Group option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Manage"))).perform();
		driver.findElement(By.linkText("Users")).click();
		Thread.sleep(4000);
		
		//Search with the name
		//driver.findElement(By.cssSelector(".ng-touched")).sendKeys(NewUser);
		driver.findElement(By.cssSelector(".block-filter:nth-child(1) .main-filter-by-input")).clear();
		driver.findElement(By.cssSelector(".block-filter:nth-child(1) .main-filter-by-input")).sendKeys(NewUser);
		
		//Click on user
		driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell/div")).click();
		
		
		//click on User
		//driver.findElement(By.xpath("//span[contains(.,'"+ NewUser +"')]")).click();
		
		//Click on Edit button
		driver.findElement(By.xpath("//button[contains(.,'Edit...')]")).click();
		
		//Select Type
		Select type=new Select(driver.findElement(By.name("typeSelector")));
		type.selectByVisibleText(Type1);
		Thread.sleep(3000);
		
		//Search with queue name
		driver.findElement(By.name("permFilter")).sendKeys(UniqueEMSTopicName);
		
		//Give View permissions
		boolean TopicView=driver.findElement(By.xpath("//datatable-body-cell[6]/div/input")).isSelected();
		System.out.println("Viewcheckbox :" +TopicView);
		
		if(TopicView)
		{
			System.out.println("View permissions given");
		}
		else
		{
			driver.findElement(By.xpath("//datatable-body-cell[6]/div/input")).click();
		}
		Thread.sleep(3000);
		
		//Click on Apply button
		driver.findElement(By.xpath("//button[contains(.,'Apply')]")).click();
		Thread.sleep(6000);
		
		//click on Acl's tab
		driver.findElement(By.linkText("ACLs")).click();
		Thread.sleep(4000);
		
		//Search with the name
		driver.findElement(By.cssSelector(".block-filter:nth-child(1) .main-filter-by-input")).sendKeys(NewUser);
		Thread.sleep(3000);
		
		//Store the data into string
		String ACLSData=driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body")).getText();
		System.out.println("ACL's data is: " +ACLSData);
		
		if(ACLSData.contains(UniqueEMSTopicName))
		{
			System.out.println("Topic is added to the ACL's");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Topic added to ACL");
		}
		else
		{
			System.out.println("Topic is not added to the ACL's");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Topic is not added to ACL");
    		
    		//close the popup page
    		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
    		driver.findElement(By.id("Topic is not added to ACL")).click();
		}
		
		//close the popup page
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();	
	}
	
	@TestRail(testCaseId=827)
	@Parameters({"Dashboardname", "NewUser"})
	@Test(priority=16, dependsOnMethods= {"AddQueueACLToUser"})
	public void ResetButtonInACLs(String Dashboardname, String NewUser, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select the User Group option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Manage"))).perform();
		driver.findElement(By.linkText("ACLs")).click();
		Thread.sleep(4000);
		
		//Search with the name
		driver.findElement(By.cssSelector(".block-filter:nth-child(1) .main-filter-by-input")).sendKeys(NewUser);
		Thread.sleep(3000);
		
		//Select create check box
		driver.findElement(By.xpath("//datatable-body-cell[13]/div/input")).click();
		
		//Click on Reset button
		driver.findElement(By.xpath("//button[contains(.,'Reset')]")).click();
		Thread.sleep(4000);
		
		//Click on Reset Button
		boolean check=driver.findElement(By.xpath("//datatable-body-cell[13]/div/input")).isSelected();
		System.out.println("Check box status is: " +check);
		
		if(check==false)
		{
			System.out.println("Reset button is working fine");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Reset button working");
		}
		else
		{
			System.out.println("Reset button is not working");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Reset button is not working");
    		driver.findElement(By.id("Reset button failed")).click();
		}
	}
	
	@TestRail(testCaseId=828)
	@Parameters({"Dashboardname", "NewUser"})
	@Test(priority=17, dependsOnMethods= {"AddQueueACLToUser", "ResetButtonInACLs"})
	public void SaveButtonInACLs(String Dashboardname, String NewUser, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select create check box
		driver.findElement(By.xpath("//datatable-body-cell[14]/div/input")).click();
		
		//Click on Save button
		driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
		Thread.sleep(6000);
		
		//Click on Reset Button
		boolean check=driver.findElement(By.xpath("//datatable-body-cell[14]/div/input")).isSelected();
		System.out.println("Check box status is: " +check);
		
		if(check)
		{
			System.out.println("Save button is working fine");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "Save button working");
		}
		else
		{
			System.out.println("Save button is not working");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "Save button is not working");
    		
    		//close the popup page
    		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
    		DeleteUser(NewUser);
    		driver.findElement(By.id("Save button failed")).click();
		}
		
		//close the popup page
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
		
		DeleteUser(NewUser);
	}
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId=829)
	@Test(priority=18)
	public void EMSManagerProperties(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Select Properties option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Properties...")).click();
		Thread.sleep(8000);
		
		//Store the Name field status into boolean
		boolean NameField=driver.findElement(By.id("name")).isEnabled();
		System.out.println(NameField);
		
		//Verification Condition
		if(NameField == false)
		{
			System.out.println("The EMS Server name is Disabled");
			context.setAttribute("Status", 1);
    		context.setAttribute("Comment", "EMS Server name is disabled");
			driver.findElement(By.cssSelector(".btn-danger")).click();
			Thread.sleep(6000);
			
		}
		else
		{
			System.out.println("The EMS Server name is Enable");
			context.setAttribute("Status", 5);
    		context.setAttribute("Comment", "The EMS Server name is Enable");
			driver.findElement(By.cssSelector(".btn-danger")).click();
			Thread.sleep(6000);
			driver.findElement(By.xpath("EMS Server name field is Enable")).click();
		}
		
	}
	
	@Parameters({"Dashboardname","FavoriteViewletName"})
	@TestRail(testCaseId=282)
	@Test(priority=19)
	public static void AddToFavorites(String Dashboardname, String FavoriteViewletName, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Create favorite Viewlet
		driver.findElement(By.xpath("//button[3]")).click();
		driver.findElement(By.id("fav")).click();
		driver.findElement(By.xpath("//app-mod-select-viewlet-type/div/div[2]/button[2]")).click();
		
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
		driver.findElement(By.xpath("//app-modal-add-viewlet-favorite/div/div[2]/button[2]")).click();
		Thread.sleep(2000);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname, driver);
		Thread.sleep(2000);
		
		//node names data storage  
		String NodeName=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println("Node name is: " +NodeName);
		
		//----------- Add Manager to favorite viewlet -----------------
		//Select Add tofavorite option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		driver.findElement(By.linkText("Add to favorites...")).click();
		Thread.sleep(2000);
		
		
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
		 * driver.findElement(By.cssSelector(".g-block-bottom-buttons > .g-button-blue")
		 * ).click(); Thread.sleep(2000);
		 */
		
		//Favorite viewlet data storing
		String Fav1=driver.findElement(By.xpath("//div[2]/app-viewlet/div/ngx-datatable/div/datatable-body")).getText();
		System.out.println("Fav viewlet data is: " +Fav1);
		
		//Verification condition
		if(Fav1.contains(NodeName))
		{
			System.out.println("EMS Manager is added to Favorite viewlet");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "EMS Manager is added to Favorite viewlet");
		}
		else
		{
			System.out.println("EMS Manager is not added to Favorite viewlet");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add EMS Manager to Favorite viewlet");
			driver.findElement(By.id("Add EMS Manager to favorite failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=26)
	public void Logout(String Dashboardname) throws Exception
	{		
		LogoutForAll lo=new LogoutForAll();
		lo.LogoutMethod(driver, Dashboardname);
	}
	
	public void DeleteUser(String NewUser) throws InterruptedException
	{
		//Select the User Group option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Manage"))).perform();
		driver.findElement(By.linkText("Users")).click();
		Thread.sleep(4000);
		
		//Search with the Username
		driver.findElement(By.xpath("//app-m-ems-server-principals/div/div/div/div/div/input")).sendKeys(NewUser);
		Thread.sleep(3000);
		
		//click on User
		//driver.findElement(By.xpath("//span[contains(.,'"+ Username +"')]")).click();
		driver.findElement(By.xpath("//div[2]/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper/datatable-body-row/div[2]/datatable-body-cell/div")).click();
		Thread.sleep(3000); 
		
		//click on User
		//driver.findElement(By.xpath("//span[contains(.,'"+ Username +"')]")).click();
		
		//Click on delete button and confirmation button
		driver.findElement(By.xpath("//button[contains(.,'Delete')]")).click();
		Thread.sleep(3000);
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(6000);
		
		//close the popup page
		driver.findElement(By.xpath("//button[contains(.,'Close')]")).click();
		Thread.sleep(4000);
	}
	
	public void CreateUser(String NewUser, String Newpassword, String Description) throws InterruptedException
	{
		//Select the User Group option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
		Actions MousehoverIncremental=new Actions(driver);
		MousehoverIncremental.moveToElement(driver.findElement(By.linkText("Manage"))).perform();
		driver.findElement(By.linkText("Users")).click();
		Thread.sleep(6000);
				
		//Click on Create button
		driver.findElement(By.xpath("//span[2]/button")).click();
		
		//Give username
		driver.findElement(By.name("emsprincname")).clear();
		driver.findElement(By.name("emsprincname")).sendKeys(NewUser);
		
		//Give password
		driver.findElement(By.name("emsprincepassword2")).clear();
		driver.findElement(By.name("emsprincepassword2")).sendKeys(Newpassword);
		
		//description
		driver.findElement(By.name("emsprincedescription")).sendKeys(Description);
		
		//click on save button
		driver.findElement(By.xpath("//div[3]/button[2]")).click();
		Thread.sleep(4000);
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
