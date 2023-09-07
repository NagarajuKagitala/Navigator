package KafkaGUI;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
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
import Common.KafkaLogin;
import Common.LogoutForAll;
import Common.Viewlets;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class KafkaPartitionBrowseMessageOptions 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String DownloadPath;
	static String WGSName;
	static String FinalTopicname="";
	static String UploadFilepath;
	static String KafkaNodeName;
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
		KafkaNodeName =Settings.getKafkaNodeName();
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
		try {
		
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
		
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
			
		 KafkaLogin login=new KafkaLogin(); 
		 boolean islogin=login.KafkaLoginPage(driver, sDriver, sDriverpath); 
		 
		 if(islogin)
		 {
			 System.out.println("login passed");
			 
			   //Delete if dashboard exists with same name
				Dashboard ob=new Dashboard();
				ob.DeleteExistDashboard(driver, Dashboardname);
			
			 //Create New Dashboard
				driver.findElement(By.cssSelector("div.block-with-border")).click();
				driver.findElement(By.name("dashboardName")).sendKeys(Dashboardname);
				/*driver.findElement(By.id("createInitialViewlets")).click();
				
				
				//Work group server selection
				Select dd=new Select(driver.findElement(By.cssSelector("select[name=\"wgsKey\"]")));
				Thread.sleep(2000);
				dd.selectByIndex(wgs);*/
				
				/*//Selection of Node
				driver.findElement(By.cssSelector(".field-topicm-input")).click();
				driver.findElement(By.cssSelector(".field-topicm-input")).sendKeys(Node);
				
				//Selectiom of topic manager
				driver.findElement(By.cssSelector(".field-node-input")).click();
				driver.findElement(By.cssSelector(".field-node-input")).sendKeys(topicmanager);*/
					
				//Create viewlet button
				driver.findElement(By.xpath("//button[@type='submit']")).click();
				Thread.sleep(4000);
				
				// ---- Creating Channel Viewlet ----		
				Viewlets obj=new Viewlets();
				obj.KafkaViewlet(driver, ViewletValue, ViewletName, WGSName, KafkaNodeName);
		 }
		 else
		 {
			 System.out.println("Login done"); 
		 }
		}
		
		catch(Exception ee)
		{
			//System.out.println("Exceptio is: " +ee.getStackTrace());
			ee.printStackTrace();
		}
	}
	
	@Parameters({"Dashboardname", "MessageData"})
	@TestRail(testCaseId = 1078)
	@Test(priority=1)
	public static void PutAMessageUsingPutNewIcon(String Dashboardname, String MessageData, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Find the topic current depth
		WebElement Depth=driver.findElement(By.xpath("//datatable-body-cell[10]/div/span"));
		String depthbefore=Depth.getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Select Browse Message option
		WebElement topiccheckbox=driver.findElement(By.xpath("/html/body/app-root/div/app-main-page/div/div/app-tab/div/div/div[1]/app-viewlet/div/ngx-datatable/div/datatable-body/datatable-selection/datatable-scroller/datatable-row-wrapper[1]/datatable-body-row/div[2]/datatable-body-cell[1]/div/div/input"));
		topiccheckbox.click();
		driver.findElement(By.linkText("Browse messages")).click();
		Thread.sleep(8000);
		
		//Select put New symbole
		driver.findElement(By.xpath("//img[@title='Put New']")).click();
		
		driver.findElement(By.name("generalNumberOfMsgs")).clear();
		driver.findElement(By.name("generalNumberOfMsgs")).sendKeys("1");
		
		//Message data
		//driver.findElement(By.id("encoding-text-9")).click();
		driver.findElement(By.xpath("//textarea")).sendKeys(MessageData);
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		Thread.sleep(8000);
		
		//refresh viewlet
		for(int i=0; i<=1; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);
		}
				
		//Store the depth value into string after adding the message into topic
		WebElement Depthafter=driver.findElement(By.xpath("//datatable-body-cell[10]/div/span"));
		String depthafter=Depthafter.getText();
		
		int result1 = Integer.parseInt(depthafter);
		int Final=result1-1;
		System.out.println(Final);
		
		//Message increment condition
		if(Final==result)
		{
			System.out.println("The new message was successfully added into the topic");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages added to topic successfully");
		}
		else
		{
			System.out.println("The new message was not added into the topic");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to add messages");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=3)
	@TestRail(testCaseId = 1079)
	public static void CopyMessageUsingCopyIcon(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Second topic name
		String SecondTopicName=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println(SecondTopicName);            
				
		//store the target topic name into string
		String TargettopicDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[10]/div/span")).getText();
		int TargetCopy=Integer.parseInt(TargettopicDepth);
		
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Copy Icon
		driver.findElement(By.xpath("//img[@title='Copy message']")).click();
		
		driver.findElement(By.xpath("//div[2]/div/div/div/input")).sendKeys(SecondTopicName);
		Thread.sleep(6000);
		driver.findElement(By.xpath("//td/div/span/input")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		//refresh viewlet
		for(int i=0; i<=1; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);
		}
		
				
		//Getting the Second topic depth after copying the message
		String FinalDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[10]/div/span")).getText();
		int FinalResultAfterCopy=Integer.parseInt(FinalDepth);
		System.out.println(FinalResultAfterCopy);
		
		int Final=TargetCopy+1;
		
		//Verification
		if(Final==FinalResultAfterCopy)
		{
			System.out.println("Message is copied to destination topic");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages copied successfully");
		}
		else
		{
			System.out.println("Message is not copied to destination topic");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to copy messages");
			driver.findElement(By.xpath("Copy message is Failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=6)
	@TestRail(testCaseId = 1080)
	public static void LoadMessageFromFileUsingIcon(String Dashboardname, ITestContext context) throws InterruptedException, AWTException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Find the topic current depth
		String depthbefore=driver.findElement(By.xpath("//datatable-body-cell[10]/div/span")).getText();
		int result = Integer.parseInt(depthbefore);
		System.out.println(result);
		
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Edit Icon
		driver.findElement(By.xpath("//img[@title='Load from file']")).click();
		Thread.sleep(2000);
		
		driver.findElement(By.id("accept-true")).click();
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
	    Thread.sleep(9000);
	    
	    //refresh viewlet
		for(int i=0; i<=6; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);
		}
	        
	    //verification of message
		String depthafter=driver.findElement(By.xpath("//datatable-body-cell[10]/div/span")).getText();	
		int result1 = Integer.parseInt(depthafter);
		System.out.println(result1);
				
		//Message increment condition
		if(result1==result)
		{
			System.out.println("The file is not uploaded successfully from icon");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to upload messages");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
		else
		{
			System.out.println("The file is uploaded successfully from icon");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "File uploaded successfully using icon");
		}
		Thread.sleep(1000); 
	    
	    
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=7)
	@TestRail(testCaseId = 1081)
	public static void ExportSelectedMessageUsingExportIcon(String Dashboardname, ITestContext context) throws InterruptedException
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		try {
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
				
		//Export Icon (MMF Export)
		driver.findElement(By.xpath("//button/img")).click();
		driver.findElement(By.xpath("//button[contains(.,'Export in MMF')]")).click();
		
		driver.findElement(By.cssSelector(".btn-group > .ng-star-inserted")).click();
		Thread.sleep(2000);
		Actions a=new Actions(driver);
		a.sendKeys(Keys.ENTER).perform();
		Thread.sleep(4000);
		
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Export Icon (Text Export)
		driver.findElement(By.xpath("//button/img")).click();
		driver.findElement(By.xpath("//button[contains(.,'Export in text')]")).click();
		
		driver.findElement(By.cssSelector(".btn-group > .ng-star-inserted")).click();
		Thread.sleep(2000);
		Actions a1=new Actions(driver);
		a1.sendKeys(Keys.ENTER).perform();
		
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "Exporting Messages is working fine");
	
		Thread.sleep(4000);
		}catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to export message, check details: "+ e.getMessage());
			driver.findElement(By.id("Export failed")).click();
		}
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=10)
	@TestRail(testCaseId = 1082)
	public static void CopyMessageUsingCopyOption(String Dashboardname, ITestContext context) throws InterruptedException
	{	
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Second topic name
		String SecondTopicName=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[4]/div/span")).getText();
		System.out.println(SecondTopicName);
				
		//Store the Target topic name into string
		String TargettopicDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[10]/div/span")).getText();
		int TargetCopy=Integer.parseInt(TargettopicDepth);
		
		//Select Message check box
		driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Copy option
		driver.findElement(By.xpath("//app-dropdown-console/div/div/div/div")).click();
		
		driver.findElement(By.xpath("//div[2]/div/div/div/input")).sendKeys(SecondTopicName);
		Thread.sleep(6000);
		driver.findElement(By.xpath("//td/div/span/input")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		//refresh viewlet
		for(int i=0; i<=1; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);
		}
				
		//Getting the Second topic depth after copying the message
		String FinalDepth=driver.findElement(By.xpath("//datatable-row-wrapper[2]/datatable-body-row/div[2]/datatable-body-cell[10]/div/span")).getText();
		int FinalResultAfterCopy=Integer.parseInt(FinalDepth);
		System.out.println(FinalResultAfterCopy);
		
		int Final=TargetCopy+1;
		
		//Verification
		if(Final==FinalResultAfterCopy)
		{
			System.out.println("Message is copied to destination topic");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages copied to destination topic successfully");
		
		}
		else
		{
			System.out.println("Message is not copied to destination topic");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to copy messages");
			driver.findElement(By.xpath("Copy message is Failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=12)
	@TestRail(testCaseId = 1083)
	public static void ClearMessagesUsingClearAllIcon(String Dashboardname, ITestContext context) throws Exception
	{
		//Clearing selection of object
		ClearSelectionofCheckbox che=new ClearSelectionofCheckbox();
		che.Deselectcheckbox(Dashboardname, driver);
		
		//Find the topic current depth
		WebElement Depth=driver.findElement(By.xpath("//datatable-body-cell[10]/div/span"));
		String depthbeforeDelete=Depth.getText();
		int result = Integer.parseInt(depthbeforeDelete);
		System.out.println(result);
				
		//Select Message check box
		//driver.findElement(By.xpath("//input[@name='name']")).click();
		
		//Delete Icon
		driver.findElement(By.xpath("//img[@title='Clear All']")).click();
		driver.findElement(By.cssSelector(".btn-primary")).click();
		Thread.sleep(8000);
		
		//refresh viewlet
		for(int i=0; i<=1; i++)
		{
			driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/div/div/i")).click();
			Thread.sleep(4000);
		}
		
			
		//Store the depth value into string after deleting the message from the topic
		WebElement DepthafterDelete=driver.findElement(By.xpath("//datatable-body-cell[10]/div/span"));
		String depthafter=DepthafterDelete.getText();
		
		int result1 = Integer.parseInt(depthafter);
		System.out.println("Output is: " +result1);
		//int Final=result1+1;
		//System.out.println(Final);
		
		//Message decrement condition
		if(result1==0)
		{
			System.out.println("The new message was successfully deleted from the topic");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Messages deleted from topic successfully");
		}
		else
		{
			System.out.println("The new message was not deleted from the topic");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to delete messages");
			driver.findElement(By.xpath("Condition is Failed")).click();
		}
	}
	
	@Parameters({"Dashboardname"})
	@Test(priority=27)
	public static void Logout(String Dashboardname) throws InterruptedException
	{
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
				FileHandler.copy(scrFile, new File(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png"));
				//FileUtils.copyFile(scrFile, new File(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png"));
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
