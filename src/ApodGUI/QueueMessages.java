package ApodGUI;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
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

import Common.ClearSelectionofCheckbox;
import Common.Dashboard;
import Common.Discoverfull;
import Common.LogoutForAll;
import Common.Viewlets;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class QueueMessages 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String DownloadPath;
	static String WGSName;
	static String UploadFilepath;
	static String UploadLargeFile;
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
		UploadFilepath =Settings.getUploadFilepath();
		UploadLargeFile =Settings.getUploadLargeFile();
		Node_Hostname =Settings.getNode_Hostname();
		Low =Settings.getLow();
		Medium =Settings.getMedium();
		High =Settings.getHigh();
		LowSleep=Integer.parseInt(Low);
		MediumSleep =Integer.parseInt(Medium);
		HighSleep=Integer.parseInt(High);
		
	}
	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname", "MessageData", "ViewletValue", "ViewletName"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname, String MessageData, int ViewletValue, String ViewletName) throws Exception
	{
		Settings.read();
		String URL = Settings.getSettingURL();
		String uname=Settings.getNav_Username();
		String password=Settings.getNav_Password();
		
		String filepath=System.getProperty("user.dir") + "\\" + DownloadPath;
				
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
		
		//Enter Url
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
		Thread.sleep(LowSleep);
		driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
		Thread.sleep(LowSleep);
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
		
		//Create local queue viewlet
		Viewlets obj=new Viewlets();
		obj.IBMMQViewlet(driver, ViewletValue, ViewletName, WGSName, Node_Hostname);
		
		//Go to edit viewlet
		driver.findElement(By.id("dropdownMenuButton")).click();
		driver.findElement(By.linkText("Edit viewlet")).click();
		Thread.sleep(LowSleep);
		
		//Update result limit
		driver.findElement(By.xpath("//input[@type='number']")).clear();
		driver.findElement(By.xpath("//input[@type='number']")).sendKeys("1000");
		Thread.sleep(LowSleep);
		
		//Click on Apply changes
		driver.findElement(By.xpath("//button[contains(.,'Apply changes')]")).click(); 
		Thread.sleep(MediumSleep);
		
		//Restoring the Default Settings
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.id("accept-true")).click();
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(HighSleep);
		
		
		//put the messages into empty queues for testing
	   for(int m=1; m<=5; m++) 
		  { 
			  //Select the put new message option
			  driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper["+ m +"]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
				Actions MessagesMousehour=new Actions(driver);
				MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
				driver.findElement(By.linkText("Put New Message")).click();
				Thread.sleep(LowSleep);
		  
		  //Select the number of messages
		  driver.findElement(By.name("generalNumberOfMsgs")).click();
		  driver.findElement(By.name("generalNumberOfMsgs")).clear();
		  driver.findElement(By.name("generalNumberOfMsgs")).sendKeys("4");
		  
		  //Put a message data //driver.findElement(By.id("encoding-text-9")).click();
		  driver.findElement(By.xpath("//textarea")).sendKeys(MessageData);
		  driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		  Thread.sleep(2000);
		  
		  try 
		  { 
			  driver.findElement(By.id("yes")).click();
			  driver.findElement(By.cssSelector(".btn-danger")).click();
			  Thread.sleep(2000);
		  } 
		  catch (Exception e) 
		  {
		  System.out.println("No Exception"); 
		  } 
		  
		  //clear selection
			driver.findElement(By.xpath("//datatable-header-cell[2]/div/i")).click();
			Thread.sleep(LowSleep);
		  }
		 
		
		//Restoring the Default Settings
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click(); 
		Thread.sleep(LowSleep);
		driver.findElement(By.id("accept-true")).click();
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
	    Thread.sleep(MediumSleep);
	}
	
	@Parameters({"Dashboardname", "MessageData"})
	@TestRail(testCaseId = 82)
	@Test(priority=1)
	public static void PutNewMessageIntoQueue(String Dashboardname, String MessageData, ITestContext context) throws InterruptedException
	{
		//Find the queue current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Select put new message Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Put New Message")).click();
		Thread.sleep(LowSleep);
				
		//Message data
		//driver.findElement(By.id("encoding-text-9")).click();
		driver.findElement(By.xpath("//textarea")).sendKeys(MessageData);
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		Thread.sleep(HighSleep);
		
		//Click on Refresh button
	    for(int i=0; i<=3; i++)
	    {
	    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
	    	Thread.sleep(LowSleep);
	    }
		
		//verification of message
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		int Final=result1-1;
		System.out.println(Final);
				
		//Message increment condition
		if(Final==result)
		{
			System.out.println("The new message was successfully added into the Queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Successfully added new message to queue");
		}
		else
		{
			System.out.println("The new message was not added into the Queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add new message to queue");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=2)
	@TestRail(testCaseId = 83)
	public static void LoadFromFileUsingYesbutton(String Dashboardname, ITestContext context) throws InterruptedException, AWTException
	{
		//Find the queue current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Select Load from file Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Load From File...")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.id("accept-true")).click();
		Thread.sleep(LowSleep);
		
		//Loading the file into queue by using robot class
		String filepath=System.getProperty("user.dir") + "\\" + UploadFilepath;
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
	    Thread.sleep(HighSleep);
	    
	    //discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
	    
	    for(int i=0; i<=4; i++)
		 {
			 driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			 Thread.sleep(MediumSleep);
		 }
	    	    
	    //store the queue depth after loading file
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		System.out.println(result1);
				
		//Message increment condition
		if(result1==result)
		{
			System.out.println("The file is not uploaded successfully");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "File upload failed");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
		else
		{
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "File uploaded successfully");
			System.out.println("The file is uploaded successfully");
		}
		Thread.sleep(1000); 
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 770)
	@Test(priority=3)
	public void LoadFromFileUsingConfigurebuttonWithSingleMessage(String Dashboardname, ITestContext context) throws InterruptedException, AWTException
	{
		//Find the queue current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Select Load from file Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Load From File...")).click();
		Thread.sleep(LowSleep);
		
		//Click on Configure
		driver.findElement(By.xpath("//button[contains(.,'Configure')]")).click();
		Thread.sleep(LowSleep);
		
		//Select single option
		Select dd=new Select(driver.findElement(By.xpath("//div[3]/div/div/select")));
		dd.selectByVisibleText("Create Single Message");
		Thread.sleep(LowSleep);
		
		//Use once button
		driver.findElement(By.xpath("//button[contains(.,'Use Once')]")).click();
		Thread.sleep(LowSleep);
		
		//Loading the file into queue by using robot class
		String filepath=System.getProperty("user.dir") + "\\" + UploadLargeFile;
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
	    Thread.sleep(HighSleep);
	    
	    //discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
	    
	    for(int i=0; i<=3; i++)
		 {
			 driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			 Thread.sleep(MediumSleep);
		 }
	    	    
	    //store the queue depth after loading file
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		int Final=result1 - 1;
		System.out.println(Final);
				
		//Message increment condition
		if(Final==result)
		{
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "File uploaded successfully");
			System.out.println("The file is uploaded successfully");
		}
		else
		{
			System.out.println("The file is not uploaded successfully");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "File upload failed");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 771)
	@Test(priority=4)
	public void LoadFromFileUsingConfigurebuttonWithMultipleMessages(String Dashboardname, ITestContext context) throws InterruptedException, AWTException
	{
		//Find the queue current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Select Load from file Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Load From File...")).click();
		Thread.sleep(LowSleep);
		
		//Click on Configure
		driver.findElement(By.xpath("//button[contains(.,'Configure')]")).click();
		Thread.sleep(LowSleep);
		
		//Select single option
		Select dd=new Select(driver.findElement(By.xpath("//div[3]/div/div/select")));
		dd.selectByVisibleText("Create Multiple Messages");
		Thread.sleep(LowSleep);
		
		//Use once button
		driver.findElement(By.xpath("//button[contains(.,'Use Once')]")).click();
		Thread.sleep(LowSleep);
		
		//Loading the file into queue by using robot class
		String filepath=System.getProperty("user.dir") + "\\" + UploadLargeFile;
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
	    Thread.sleep(6000);
	    
	    //discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
	    
	    //Click on Refresh button
	    for(int i=0; i<=3; i++)
	    {
	    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
	    	Thread.sleep(MediumSleep);
	    }
	    
	    //store the queue depth after loading file
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		System.out.println(result1);
				
		//Message increment condition
		if(result1 > result+1)
		{
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "File uploaded successfully");
			System.out.println("The file is uploaded successfully");
		}
		else
		{
			System.out.println("The file is not uploaded successfully");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "File upload failed");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
		Thread.sleep(1000);	
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=5)
	@TestRail(testCaseId = 84)
	public static void ExportAllMessages(String Dashboardname, ITestContext context) throws InterruptedException
	{
		try {
			//Clearing selection of object
			ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
			che.Deselectcheckbox(Dashboardname,driver);
			
		//Export All Messages As MMF
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions MMFMousehour=new Actions(driver);
		MMFMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		MMFMousehour.moveToElement(driver.findElement(By.linkText("Export All Messages..."))).perform();
		driver.findElement(By.linkText("As .MMF")).click();
		Thread.sleep(LowSleep);                                      
		
		//Click on Yes button
		driver.findElement(By.cssSelector(".btn-group > .ng-star-inserted")).click();
		Thread.sleep(MediumSleep);
		
		//Clearing selection of object
		ClearSelectionofCheckbox che1=new ClearSelectionofCheckbox();
		che1.Deselectcheckbox(Dashboardname,driver);
		
		//Export All Messages as TXT
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions TXTMousehour=new Actions(driver);
		TXTMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		TXTMousehour.moveToElement(driver.findElement(By.linkText("Export All Messages..."))).perform();
		driver.findElement(By.linkText("As .TXT")).click();
		Thread.sleep(LowSleep);
		
		//Click on Yes button
		driver.findElement(By.cssSelector(".btn-group > .ng-star-inserted")).click();
		Thread.sleep(MediumSleep);
		
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Messages exported successfully");
		}catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got exception while exporting messages, check details: "+ e.getMessage());
		}
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=6)
	@TestRail(testCaseId = 85)
	public static void CopyAllMessagesFromOneQueueToAnotherQueue(String Dashboardname, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
				
		//Get the Manager name of first one
		String Managername=driver.findElement(By.xpath("//datatable-body-cell[5]/div/span")).getText();
		
		//Search with that manager
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Managername);
		Thread.sleep(LowSleep);
		
		//Find the queue current depth
		String depthbeforeCopy=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
		int resultCopy = Integer.parseInt(depthbeforeCopy);
		System.out.println("Initial depth is: " +resultCopy);
		
		//Store depth of the target queue 
		String TargetQueueDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[6]/div/span")).getText();
		int TargetCopy=Integer.parseInt(TargetQueueDepth);
		
		int FinalResultBefore=resultCopy+TargetCopy;
		System.out.println("Required Target queue depth is: " +FinalResultBefore);
		
		//Second Queue name
		String SecondQueueName=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println(SecondQueueName);
		
		//Copy All Messages
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions CopyAllMessagesMousehour=new Actions(driver);
		CopyAllMessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Copy All")).click();
		Thread.sleep(LowSleep);
		
		//Search with target queue name 
		driver.findElement(By.xpath("//div[2]/div/div/div/input")).sendKeys(SecondQueueName);
		Thread.sleep(LowSleep);
		driver.findElement(By.cssSelector(".viewlet-cell-checkbox > input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
    	
    	//Search with that manager
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Managername);
		Thread.sleep(LowSleep);
		
		 for(int i=0; i<=2; i++)
		 {
			 driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			 Thread.sleep(LowSleep);
		 }
				
		//Getting the Second Queue depth after copying the all messages
		String FinalDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[6]/div/span")).getText();
		int FinalResultAfterCopy=Integer.parseInt(FinalDepth);
		System.out.println("Final Depth of target queue is: " +FinalResultAfterCopy);
		
		//Verification
		if(FinalResultBefore==FinalResultAfterCopy)
		{
			System.out.println("All messages are copied to destination queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages copied successfully");
		}
		else
		{
			System.out.println("All messages are not copied to destination queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to copy messages from one queue to another");
			driver.findElement(By.xpath("Copy messages is Failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=7)
	@TestRail(testCaseId = 86)
	public static void MoveAllMessagesFromOneQueueToAnotherQueue(String Dashboardname, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//uncheck the show empty queues checkbox
		  driver.findElement(By.cssSelector(".fa-cog")).click();
		  Thread.sleep(LowSleep);
		  
		  boolean emptyqueues=driver.findElement(By.id("empty-queues")).isSelected();
		  
		  if(emptyqueues)
		  {
			  driver.findElement(By.id("empty-queues")).click();
		  }
		   
		  Thread.sleep(4000);
		  driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		  Thread.sleep(MediumSleep);
		
		//Get the Manager name of first one
		String Managername=driver.findElement(By.xpath("//datatable-body-cell[5]/div/span")).getText();
		
		//Search with that manager
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Managername);
		Thread.sleep(LowSleep);
		
		//Find the queue current depth
		String depthbeforeMove=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
		int resultCopy = Integer.parseInt(depthbeforeMove);
		System.out.println("Initial Queue depth: " +resultCopy);
		
		//Store depth of the target queue
		String TargetQueueDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[6]/div/span")).getText();
		int TargetCopy=Integer.parseInt(TargetQueueDepth);
		
		int FinalResultBeforeMove=resultCopy+TargetCopy;
		System.out.println("Target Queue initial depth: " +FinalResultBeforeMove);
		
		//Second Queue name
		String DestinationQueueName=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println(DestinationQueueName);
		
		//Move All Messages
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions MoveAllMessagesMousehour=new Actions(driver);
		MoveAllMessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Move All")).click();
		Thread.sleep(LowSleep);
			
		//Search with the target queue name
		driver.findElement(By.xpath("//div[2]/div/div/div/input")).sendKeys(DestinationQueueName);
		Thread.sleep(LowSleep);
		driver.findElement(By.cssSelector(".viewlet-cell-checkbox > input")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(MediumSleep);
		
		//discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
    	
    	//Search with that manager
		driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Managername);
		Thread.sleep(LowSleep);
		
		//Refresh the viewlet
		for(int i=0; i<=5; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(LowSleep);
		}
		
				
		//Second Queue depth after moving the all messages
		String FinalDepth=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
		int FinalResultAfterMove=Integer.parseInt(FinalDepth);
		System.out.println("Queue depth after moving: " +FinalResultAfterMove);
		
		for(int i=0; i<=Managername.length(); i++)
		{
			driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys(Keys.BACK_SPACE);
		}
				
		//Verification
		if(FinalResultBeforeMove==FinalResultAfterMove)
		{
			System.out.println("All messages are moved to destination queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages moved successfully");
		}
		else
		{
			System.out.println("All messages are not moved to destination queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to move messages from one queue to another");
			driver.findElement(By.xpath("Move messages is Failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=8)
	@TestRail(testCaseId = 87)
	public static void DeleteAllMessagesFromQueue(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		Thread.sleep(3000);
		/*
		 * // Changing the Settings
		 * driver.findElement(By.cssSelector(".fa-cog")).click();
		 * driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click
		 * (); Thread.sleep(4000);
		 * driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		 * Thread.sleep(3000);
		 */
				
		//Queue Name before deleting messages
		String Queuename=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println(Queuename);
				
		//Get the manager name
		String Managername=driver.findElement(By.xpath("//datatable-body-cell[5]/div/span")).getText();
		System.out.println("Manager name is: " +Managername);
		
		//Select Delete All option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions DeleteAllMessagesMousehour=new Actions(driver);
		DeleteAllMessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Delete All")).click();
		Thread.sleep(LowSleep);
		
		//Click on Yes button for deleting the queue
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
		 
		//Queue name after deleting the messages
		String QueuenameAfter=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println(QueuenameAfter);
		
		
		//Get the manager name
		String ManagernameAfter=driver.findElement(By.xpath("//datatable-body-cell[5]/div/span")).getText();
		System.out.println("Manager name is: " +ManagernameAfter);
		
		//verification condition
		if(Queuename.equalsIgnoreCase(QueuenameAfter))
		{
			if(Managername.equalsIgnoreCase(ManagernameAfter))
			{
				System.out.println("Messages are not deleted from the queue");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to delete messages");
				driver.findElement(By.xpath("Deleting messages failed")).click();
			}
			else
			{
				System.out.println("Messages are deleted from the queue");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Messages deleted successfully");
			}
		}
		else
		{
			System.out.println("Messages are deleted from the queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages deleted successfully");
		}
		Thread.sleep(1000);
		
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=9)
	@TestRail(testCaseId = 88)
	public static void ClearAllMessagesFromQueue(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		/*
		 * // Changing the Settings
		 * driver.findElement(By.cssSelector(".fa-cog")).click();
		 * driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click
		 * (); Thread.sleep(4000);
		 * driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		 * Thread.sleep(3000);
		 */
				
		//Queue Name before deleting messages
		String Queuename=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println(Queuename);
				
		//Get the manager name
		String Managername=driver.findElement(By.xpath("//datatable-body-cell[5]/div/span")).getText();
		System.out.println("Manager name is: " +Managername);
		
		//Select Clear All option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions DeleteAllMessagesMousehour=new Actions(driver);
		DeleteAllMessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Clear All")).click();
		Thread.sleep(LowSleep);
		
		//Click on Yes for clearing the Messages from the queue
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
		//Queue name after deleting the messages
		String QueuenameAfter=driver.findElement(By.xpath("//datatable-body-cell[4]/div/span")).getText();
		System.out.println(QueuenameAfter);
		
		//Get the manager name
		String ManagernameAfter=driver.findElement(By.xpath("//datatable-body-cell[5]/div/span")).getText();
		System.out.println("Manager name is: " +ManagernameAfter);
		
		//verification condition
		if(Queuename.equalsIgnoreCase(QueuenameAfter))
		{
			if(Managername.equalsIgnoreCase(ManagernameAfter))
			{
				System.out.println("Messages are not Cleared from the queue");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to clear messages");
				driver.findElement(By.xpath("Clear messages failed")).click();
			}
			else
			{
				System.out.println("Messages are Cleared from the queue");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Messages cleared successfully");
			}
		}
		else
		{
			System.out.println("Messages are Cleared from the queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages cleared successfully");
		}
		Thread.sleep(1000);
		
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 550)
	@Test(priority=10)
	public void PutMessageUsingJsonFile(String Dashboardname, ITestContext context) throws InterruptedException, AWTException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
				
		//Find the queue current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Select put new message Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Put New Message")).click();
		Thread.sleep(LowSleep);
		
		//click on Attache file option
		driver.findElement(By.xpath("//input[3]")).click();
		Thread.sleep(LowSleep);
		
		//Loading the file into queue by using robot class
		String filepath=System.getProperty("user.dir") + "\\" + "Screenshots\\Staticimages\\File.json";
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
				
		driver.findElement(By.id("save-message")).click();
		Thread.sleep(MediumSleep);
		
		//discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
    	
		//Click on Refresh button
	    for(int i=0; i<=2; i++)
	    {
	    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
	    	Thread.sleep(LowSleep);
	    }
				 
		//verification of message
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		//int Final=result1-1;
		//System.out.println(Final);
				
		//Message increment condition
		if(!(result1==result))
		{
			System.out.println("The Json file is added to the Queue like a message");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Successfully uploaded the json file to queue");
		}
		else
		{
			System.out.println("The Json file is not added to the Queue like a message");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add new message to queue");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"Dashboardname"})
	@TestRail(testCaseId = 551)
	@Test(priority=11)
	public void PutMessageUsingXMLFile(String Dashboardname, ITestContext context) throws InterruptedException, AWTException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Find the queue current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Select put new message Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Put New Message")).click();
		Thread.sleep(LowSleep);
		
		//click on Attache file option
		driver.findElement(By.xpath("//input[3]")).click();
		Thread.sleep(LowSleep);
		
		//Loading the file into queue by using robot class
		String filepath=System.getProperty("user.dir") + "\\" + "Screenshots\\Staticimages\\FileX.xml";
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
				
		driver.findElement(By.id("save-message")).click();
		Thread.sleep(MediumSleep);
		
		//discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
		
		//Click on Refresh button
	    for(int i=0; i<=3; i++)
	    {
	    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
	    	Thread.sleep(LowSleep);
	    }
		
		//verification of message
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		//int Final=result1-1;
		//System.out.println(Final);
				
		//Message increment condition
		if(!(result1==result))
		{
			System.out.println("The xml file is added to the Queue like a message");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Successfully uploaded the xml file to queue");
		}
		else
		{
			System.out.println("The xml file is not added to the Queue like a message");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add new message to queue");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId = 552)
	@Parameters({"Dashboardname", "HexMessageData"})
	@Test(priority=12)
	public static void PutHexMessageIntoQueue(String Dashboardname, String HexMessageData, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname,driver);
		
		//Find the queue current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println("Before putting hex message: " +result);
		
		//Select put new message Option
		driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input")).click();
		Actions MessagesMousehour=new Actions(driver);
		MessagesMousehour.moveToElement(driver.findElement(By.linkText("Messages"))).perform();
		driver.findElement(By.linkText("Put New Message")).click();
		Thread.sleep(LowSleep);
		
		//Message data
		//driver.findElement(By.xpath("//textarea")).click();
		driver.findElement(By.xpath("//textarea")).sendKeys(HexMessageData);
		Thread.sleep(LowSleep);
		
		//Clickon HEX button
		driver.findElement(By.xpath("//button[contains(.,'Hex')]")).click();
		Thread.sleep(LowSleep);
				
		//Close the popup window 
		driver.findElement(By.id("save-message")).click();
		Thread.sleep(MediumSleep);
		
		//discover full
    	Discoverfull dis=new Discoverfull();
    	dis.NodeDiscoverfull(Dashboardname, Node_Hostname, driver);
		
		//Click on Refresh button
	    for(int i=0; i<=3; i++)
	    {
	    	driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
	    	Thread.sleep(LowSleep);
	    }
		
		
		//verification of message
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell[6]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		int Final=result1-1;
		System.out.println("After putting hex message: " + Final);
				
		//Message increment condition
		if(Final==result)
		{
			System.out.println("The new message was successfully added into the Queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "The new message was successfully added to Queue");
		}
		else
		{
			System.out.println("The new message was not added into the Queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add new message to Queue");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=20)
	public static void Logout(String Dashboardname) throws Exception
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
