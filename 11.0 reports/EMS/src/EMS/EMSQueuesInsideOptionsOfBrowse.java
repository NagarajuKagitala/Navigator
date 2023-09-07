package EMS;

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
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
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
public class EMSQueuesInsideOptionsOfBrowse
{
	static WebDriver driver;
	static String Screenshotpath;
	static String DownloadPath;
	static String WGSName;
	static String UploadFilepath;
	static String EMS_WGSNAME;
	static String EMSNode;
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
		EMS_WGSNAME =Settings.getEMS_WGSNAME();
		EMSNode =Settings.getEMSNode();
		Low =Settings.getLow();
		Medium =Settings.getMedium();
		High =Settings.getHigh();
		LowSleep=Integer.parseInt(Low);
		MediumSleep =Integer.parseInt(Medium);
		HighSleep=Integer.parseInt(High);
	}
	
	@Parameters({"sDriver", "sDriverpath", "Dashboardname", "Node", "Queuemanager", "MessageData", "ViewletValue", "ViewletName"})
	@Test
	public static void Login(String sDriver, String sDriverpath, String Dashboardname, String Node, String Queuemanager, String MessageData, int ViewletValue, String ViewletName) throws Exception
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
		else if(sDriver.equalsIgnoreCase("webdriver.edge.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new EdgeDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.ie.driver"))
		{
			System.setProperty(sDriver, sDriverpath);
			driver=new InternetExplorerDriver();
		}
		else
		{
			/*System.setProperty(sDriver, sDriverpath);
			//driver = new FirefoxDriver();
			FirefoxOptions options = new FirefoxOptions();
			options.setCapability("marionette", true);
			driver = new FirefoxDriver(options);*/
			
			System.setProperty(sDriver, sDriverpath);       
			
			FirefoxOptions options = new FirefoxOptions();
			options.setCapability("marionette", true);
			options.addPreference("browser.download.folderList", 2);
			options.addPreference("browser.download.dir", DownloadPath);
			options.addPreference("browser.download.useDownloadDir", true);
			options.addPreference("browser.helperApps.neverAsk.saveToDisk", "text/plain");
			options.addPreference("pdfjs.disabled", true);  // disable the built-in PDF viewer
			options.addPreference("jsonjs.disabled", true);

			driver = new FirefoxDriver(options);
		}
		
		//Enter the URL into browser
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		
		//Login details
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button.btn-submit")).click();
		Thread.sleep(15000);
		
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
		dd.selectByIndex(wgs);
		
		//Selection of Node
		driver.findElement(By.cssSelector(".field-queuem-input")).click();
		driver.findElement(By.cssSelector(".field-queuem-input")).sendKeys(Node);
		
		//Selectiom of Queue manager
		driver.findElement(By.cssSelector(".field-node-input")).click();
		driver.findElement(By.cssSelector(".field-node-input")).sendKeys(Queuemanager);*/
			
		//Create viewlet button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(2000);
		
		//Create local queue viewlet		
		Viewlets obj=new Viewlets();
		obj.EMSViewlet(driver, ViewletValue, ViewletName, EMS_WGSNAME, EMSNode);
		
		/*//Check Show Empty Queues check box
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//div[2]/input")).click();
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(2000);
		
		//put the messages into empty queues for testing
		for(int m=1; m<=3; m++)
		{
			//Select the put new message option
			driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper["+ m +"]/datatable-body-row/div[2]/datatable-body-cell[1]/div/input")).click();
			Actions PutMessagesMousehour=new Actions(driver);
			PutMessagesMousehour.moveToElement(driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[6]"))).perform();
			driver.findElement(By.xpath("//app-dropdown[@id='dropdown-block']/div/ul/li[6]/ul/li")).click();
			Thread.sleep(1000);
			
			//Select the number of messages
			driver.findElement(By.name("generalNumberOfMsgs")).click();
			driver.findElement(By.name("generalNumberOfMsgs")).clear();
			driver.findElement(By.name("generalNumberOfMsgs")).sendKeys("4");
			
			//Put a message data
			driver.findElement(By.xpath("//*[@id=\"9\"]")).click();
			driver.findElement(By.xpath("//*[@id=\"9\"]")).sendKeys(MessageData);
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
		}*/
		
		//Restoring the Default Settings
		driver.findElement(By.cssSelector(".fa-cog")).click();
		driver.findElement(By.xpath("//button[contains(.,'Restore Default')]")).click();
		Thread.sleep(4000);
		driver.findElement(By.id("accept-true")).click();
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(6000);
		
		//UnCheck Show Empty Queues check box
		driver.findElement(By.cssSelector(".fa-cog")).click();
		Thread.sleep(LowSleep);
		driver.findElement(By.id("empty-queues")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//button[contains(.,'Save Changes')]")).click();
		Thread.sleep(6000);
	}
	
	@Parameters({"Dashboardname", "MessageData"})
	@TestRail(testCaseId=300)
	@Test(priority=1)
	public static void PutAMessageUsingPutNewIcon(String Dashboardname, String MessageData, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Find the queue current depth
		WebElement Depth=driver.findElement(By.xpath("//datatable-body-cell[8]/div/span"));
		String depthbefore=Depth.getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		
		//Select Browse Message option
		WebElement Queuecheckbox=driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input"));
		Queuecheckbox.click();
		driver.findElement(By.linkText("Browse messages")).click();
		Thread.sleep(1000);
		
		//Select put New symbole
		driver.findElement(By.xpath("//img[@title='Put New']")).click();
		
		//Add count
		driver.findElement(By.id("numberOfMessagesToPut")).clear();
		driver.findElement(By.id("numberOfMessagesToPut")).sendKeys("6");
		
		//Message data
		//driver.findElement(By.id("encoding-text-9")).click();
		driver.findElement(By.xpath("//textarea")).sendKeys(MessageData);
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		Thread.sleep(8000);
		
		 for(int i=0; i<=2; i++)
		 {
			 driver.findElement(By.cssSelector(".no-loading-spinner")).click();
			 Thread.sleep(4000);
		 }
		
		//Store the depth value into string after adding the message into queue
		WebElement Depthafter=driver.findElement(By.xpath("//datatable-body-cell[8]/div/span"));
		String depthafter=Depthafter.getText();
		
		int result1 = Integer.parseInt(depthafter);
		int Final=result1-6;
		System.out.println(Final);
		
		//Message increment condition
		if(Final==result)
		{
			System.out.println("The new message was successfully added into the Queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "The new message was successfully added into the Queue");
		}
		else
		{
			System.out.println("The new message was not added into the Queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add new message into the Queue");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
	}
	@Parameters({"Dashboardname"})
	@Test(priority=2)
	@TestRail(testCaseId=301)
	public static void DeleteMessageUsingDeleteIcon(String Dashboardname, ITestContext context) throws Exception
	{				
		//Find the queue current depth
		WebElement Depth=driver.findElement(By.xpath("//datatable-body-cell[8]/div/span"));
		String depthbeforeDelete=Depth.getText();
		int result = Integer.parseInt(depthbeforeDelete);
		System.out.println(result);
				
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Delete Icon
		driver.findElement(By.cssSelector("img[title=\"Delete\"]")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(6000);
		
		 for(int i=0; i<=2; i++)
		 {
			 driver.findElement(By.cssSelector(".no-loading-spinner")).click();
			 Thread.sleep(4000);
		 }
		
		//Store the depth value into string after deleting the message from the queue
		WebElement DepthafterDelete=driver.findElement(By.xpath("//datatable-body-cell[8]/div/span"));
		String depthafter=DepthafterDelete.getText();
		
		int result1 = Integer.parseInt(depthafter);
		int Final=result1+1;
		System.out.println(Final);
		
		//Message decrement condition
		if(Final==result)
		{
			System.out.println("The new message was successfully deleted from the Queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "The new message was successfully deleted from the Queue");
		}
		else
		{
			System.out.println("The new message was not deleted from the Queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "The new message was not deleted from the Queue");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
	}
	@Parameters({"Dashboardname"})
	@Test(priority=3)
	@TestRail(testCaseId=302)
	public static void CopyMessageUsingCopyIcon(String Dashboardname, ITestContext context) throws InterruptedException
	{			
		//Second Queue name
		String SecondQueueName=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println(SecondQueueName);                
				
		//store the target queue name into string
		String TargetQueueDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[8]/div/span")).getText();
		int TargetCopy=Integer.parseInt(TargetQueueDepth);
		System.out.println("before moving: " +TargetCopy);
		
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Copy Icon
		driver.findElement(By.xpath("//img[@title='Copy message']")).click();
		
		driver.findElement(By.xpath("//div[2]/div/div/div/input")).sendKeys(SecondQueueName);
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//td/div/span/input")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		 for(int i=0; i<=4; i++)
		 {
			 driver.findElement(By.cssSelector(".no-loading-spinner")).click();
			 Thread.sleep(4000);
		 }
				
		//Getting the Second Queue depth after copying the message
		String FinalDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[8]/div/span")).getText();
		int FinalResultAfterCopy=Integer.parseInt(FinalDepth);
		System.out.println("After copy: " +FinalResultAfterCopy);
		
		int Final=TargetCopy+1;
		
		//Verification
		if(Final==FinalResultAfterCopy)
		{
			System.out.println("Message is copied to destination queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Message is copied to destination queue");
		}
		else
		{
			System.out.println("Message is not copied to destination queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Message is not copied to destination queue");
			driver.findElement(By.xpath("Copy message is Failed")).click();
		}
		Thread.sleep(1000);
		
	}
	@Parameters({"Dashboardname"})
	@Test(priority=4)
	@TestRail(testCaseId=303)
	public static void MoveMessageUsingMoveIcon(String Dashboardname, ITestContext context) throws InterruptedException
	{	
			
		//Second Queue name
		String SecondQueueName=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println(SecondQueueName);
				
		//Store the target queue name into string
		String TargetQueueDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[8]/div/span")).getText();
		int TargetCopy=Integer.parseInt(TargetQueueDepth);
		
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Copy Icon
		driver.findElement(By.xpath("//img[@title='Move message']")).click();
		
		driver.findElement(By.xpath("//div[2]/div/div/div/input")).sendKeys(SecondQueueName);
		Thread.sleep(LowSleep);
		driver.findElement(By.xpath("//td/div/span/input")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		//Refresh the viewlet
		for(int i=0; i<=4; i++)
		{
			driver.findElement(By.cssSelector(".no-loading-spinner")).click();
			Thread.sleep(4000);
		}
				
		//Getting the Second Queue depth after copying the message
		String FinalDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[8]/div/span")).getText();
		int FinalResultAfterCopy=Integer.parseInt(FinalDepth);
		System.out.println(FinalResultAfterCopy);
		
		int Final=TargetCopy+1;
		
		//Verification
		if(Final==FinalResultAfterCopy)
		{
			System.out.println("Message is moved to destination queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Message is moved to destination queue");
		}
		else
		{
			System.out.println("Message is not moved to destination queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to move Message in to destination queue");
			driver.findElement(By.xpath("Move message is Failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=6)
	@TestRail(testCaseId=304)
	public static void LoadMessageFromFileUsingIcon(String Dashboardname, ITestContext context) throws InterruptedException, AWTException
	{			
		//Find the queue current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell[8]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		Thread.sleep(3000);
		
		//Edit Icon
		driver.findElement(By.xpath("//img[@title='Load from file']")).click();
		Thread.sleep(3000);
		
		driver.findElement(By.xpath("//button[contains(.,'Load from file')]"));
		Thread.sleep(4000);

		//Loading a file from the load file option
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
	    Thread.sleep(15000);
	    
	    for(int i=0; i<=2; i++)
		 {
	    	driver.findElement(By.cssSelector(".no-loading-spinner")).click();
			 Thread.sleep(4000);
		 }
	        
	    //verification of message
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell[8]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		System.out.println(result1);
				
		//Message increment condition
		if(result1==result)
		{
			System.out.println("The file is not uploaded successfully from icon");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to upload file using upload icon");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
		else
		{
			System.out.println("The file is uploaded successfully from icon");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "The file is uploaded successfully using upload icon");
		}
		Thread.sleep(2000); 
	        
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=7)
	@TestRail(testCaseId=305)
	public static void ExportSelectedMessageUsingExportIcon(String Dashboardname, ITestContext context) throws InterruptedException
	{		
		try {
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		Thread.sleep(2000);
				
		//Export Icon (MMF Export)
		driver.findElement(By.xpath("//button/img")).click();
		driver.findElement(By.xpath("//button[contains(.,'Export in MMF')]")).click();
		
		driver.findElement(By.cssSelector(".btn-group > .ng-star-inserted")).click();
		Thread.sleep(2000);
		Actions a=new Actions(driver);
		a.sendKeys(Keys.ENTER).perform();
		Thread.sleep(6000);
		
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		Thread.sleep(2000);
		
		//Export Icon (Text Export)
		driver.findElement(By.xpath("//button/img")).click();
		driver.findElement(By.xpath("//button[contains(.,'Export in text')]")).click();
		
		driver.findElement(By.cssSelector(".btn-group > .ng-star-inserted")).click();
		Thread.sleep(2000);
		Actions a1=new Actions(driver);
		a1.sendKeys(Keys.ENTER).perform();
		Thread.sleep(6000);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Messages exported successfully using export icon option");
		}
		catch (Exception e) {
			// TODO: handle exception
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to export Messages using export icon option");
		}
	}
	
	// --------------------- From Message Drop down Options -----------------
	@Parameters({"Dashboardname"})	
	@Test(priority=9)
	@TestRail(testCaseId=306)
	public static void DeleteMessageUsingDeleteOption(String Dashboardname, ITestContext context) throws Exception
	{			
		//Find the queue current depth
		WebElement Depth=driver.findElement(By.xpath("//datatable-body-cell[8]/div/span"));
		String depthbeforeDelete=Depth.getText();
		int result = Integer.parseInt(depthbeforeDelete);
		System.out.println(result);
				
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Delete option
		driver.findElement(By.cssSelector(".item-dropdown:nth-child(2)")).click();
		driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();
		Thread.sleep(4000);
		
		 for(int i=0; i<=2; i++)
		 {
			 driver.findElement(By.cssSelector(".no-loading-spinner")).click();
			 Thread.sleep(4000);
		 }
		 
		//Store the queue depth after deleting the message
		WebElement DepthafterDelete=driver.findElement(By.xpath("//datatable-body-cell[8]/div/span"));
		String depthafter=DepthafterDelete.getText();
		
		int result1 = Integer.parseInt(depthafter);
		int Final=result1+1;
		System.out.println(Final);
		
		//Message decrement condition
		if(Final==result)
		{
			System.out.println("The new message was successfully deleted from the Queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages deleted successfully using delete option");
		}
		else
		{
			System.out.println("The new message was not deleted from the Queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to delete Messages using delete option");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
	}
	@Parameters({"Dashboardname"})
	@Test(priority=10)
	@TestRail(testCaseId=307)
	public static void CopyMessageUsingCopyOption(String Dashboardname, ITestContext context) throws InterruptedException
	{			
		//Second Queue name
		String SecondQueueName=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println(SecondQueueName);
				
		//Store the Target queue name into string
		String TargetQueueDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[8]/div/span")).getText();
		int TargetCopy=Integer.parseInt(TargetQueueDepth);
		System.out.println("Before copy: " +TargetCopy);
		
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Copy option
		driver.findElement(By.cssSelector(".item-dropdown:nth-child(3)")).click();
		
		driver.findElement(By.xpath("//div[2]/div/div/div/input")).sendKeys(SecondQueueName);
		driver.findElement(By.xpath("//td/div/span")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click(); 
		Thread.sleep(8000);
		
		 for(int i=0; i<=4; i++)
		 {
			 driver.findElement(By.cssSelector(".no-loading-spinner")).click();
			 Thread.sleep(4000);
		 }
				
		//Getting the Second Queue depth after copying the message
		String FinalDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[8]/div/span")).getText();
		int FinalResultAfterCopy=Integer.parseInt(FinalDepth);
		System.out.println("after moving: " +FinalResultAfterCopy);
		
		int Final=TargetCopy+1;
		
		//Verification
		if(Final==FinalResultAfterCopy)
		{
			System.out.println("Message is copied to destination queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Message are copied to destination queue");
		}
		else
		{
			System.out.println("Message is not copied to destination queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to copy messages to destination queue");
			driver.findElement(By.xpath("Copy message is Failed")).click();
		}
		Thread.sleep(1000);
		
	}
	@Parameters({"Dashboardname"})
	@Test(priority=11)
	@TestRail(testCaseId=308)
	public static void MoveMessageUsingMoveOption(String Dashboardname,ITestContext context) throws InterruptedException
	{			
		//Second Queue name
		String SecondQueueName=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println(SecondQueueName);
				
		//Store the target queue name into string after moving the message
		String TargetQueueDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[8]/div/span")).getText();
		int TargetCopy=Integer.parseInt(TargetQueueDepth);
		System.out.println("before move: "+TargetCopy);
		
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Move option
		driver.findElement(By.cssSelector(".item-dropdown:nth-child(4)")).click();
		
		driver.findElement(By.xpath("//div[2]/div/div/div/input")).sendKeys(SecondQueueName);
		driver.findElement(By.xpath("//td/div/span/input")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		//Refresh the viewlet
		for(int i=0; i<=4; i++)
		{
			driver.findElement(By.cssSelector(".no-loading-spinner")).click();
			Thread.sleep(4000);
		}
				
		//Getting the Second Queue depth after copying the message
		String FinalDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[8]/div/span")).getText();
		int FinalResultAfterCopy=Integer.parseInt(FinalDepth);
		System.out.println("After moving: " +FinalResultAfterCopy);
		
		int Final=TargetCopy+1;
		
		//Verification
		if(Final==FinalResultAfterCopy)
		{
			System.out.println("Message is moved to destination queue");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Message are moved to destination queue");
		}
		else
		{
			System.out.println("Message is not moved to destination queue");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to move messages to destination queue");
			driver.findElement(By.xpath("Move message is Failed")).click();
		}
		Thread.sleep(1000);
		
	}
	@Parameters({"Dashboardname"})
	@Test(priority=12)
	public void Logout(String Dashboardname) throws InterruptedException 
	{
				
		//Logout and close the browser
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
